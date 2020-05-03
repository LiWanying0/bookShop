package com.nit.book.shop.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nit.book.shop.common.JsonResult;
import com.nit.book.shop.entity.*;
import com.nit.book.shop.mapper.*;
import com.nit.book.shop.service.AuthenticationService;
import com.nit.book.shop.service.BookImageService;
import com.nit.book.shop.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BookServiceImpl extends ServiceImpl<BookMapper, Book> implements BookService {

    @Autowired
    private BookImageService bookImageService;

    @Autowired
    private BookCommentMapper bookCommentMapper;

    @Autowired
    private BookMapper bookMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private BookImageMapper bookImageMapper;


    @Autowired
    private PurchaseMapper purchaseMapper;

    @Autowired
    private AuthenticationService authenticationService;


    @Override
    public List<BookIndexVO> findBooksForIndex() {
        List<BookIndexVO> bookIndexVOList = new ArrayList<>();

        List<Book> books = bookMapper.selectBooksByCategoryLimit(5);

        List<Integer> bookIdList = books.stream().map(Book::getId).collect(Collectors.toList());
        QueryWrapper<BookImage> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("bid", bookIdList);
        List<BookImage> bookImages = bookImageMapper.selectList(queryWrapper);
        Map<Integer, BookImage> bookImageMap = bookImages.stream().collect(Collectors.toMap(BookImage::getBid, b -> b, (oldObj, newObj) -> oldObj));

        List<BookDTO> bookDTOS = books.stream().map(b -> {
            BookDTO bookDTO = new BookDTO();
            BeanUtils.copyProperties(b, bookDTO);
            BookImage bi = bookImageMap.get(b.getId());
            if (bi != null) {
                bookDTO.setBookImage(bi);
            }
            return bookDTO;
        }).collect(Collectors.toList());

        Map<Integer, List<BookDTO>> bookDTOMap = bookDTOS.stream().collect(Collectors.groupingBy(BookDTO::getCid));

        bookDTOMap.entrySet().stream().forEach(entry -> {
            BookIndexVO vo = new BookIndexVO();
            vo.setCategoryName(entry.getKey().toString());
            vo.setBooks(entry.getValue());
            bookIndexVOList.add(vo);
        });
        return bookIndexVOList;
    }

    @Override
    public JsonResult<IPage<BookDTO>> findBookList(Integer pageNum, String search) {
        Page<BookDTO> page = new Page(pageNum, 15);
        User user = authenticationService.findCurrentUser();
        IPage<BookDTO> videoList = bookMapper.selectBookDTOPage(page, search, user.getId());
        return JsonResult.success(videoList);
    }

    @Override
    public JsonResult<Integer> deleteBook(Integer bookId) {
        int res = bookMapper.deleteById(bookId);
        return JsonResult.success(res);
    }

    @Value("${image.save-path}")
    private String imagePath;


    @Transactional
    @Override
    public JsonResult<Integer> uploadBook(Book book, MultipartFile[] images) {
        User user = authenticationService.findCurrentUser();

        QueryWrapper<Book> bookQueryWrapper = new QueryWrapper<>();
        bookQueryWrapper.eq("uid", user.getId()).eq("name", book.getName());
        Book oldBook = bookMapper.selectOne(bookQueryWrapper);
        if (oldBook != null) {
            return JsonResult.error("书名不能和已发布的书名相同");
        }
        book.setUid(user.getId());
        book.setDate(LocalDateTime.now());
        int res = bookMapper.insert(book);
        if (res < 1) {
            return JsonResult.error("保存失败，请稍后再试");
        }

        if (images.length <= 1 && StringUtils.isEmpty(images[0].getOriginalFilename())) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return JsonResult.error("请至少上传一张图片");
        }

        List<String> imageNames = new ArrayList<>(images.length);
        for (int i = 0; i < images.length; i++) {
            String imageName = images[i].getOriginalFilename();
            String newFileName = System.currentTimeMillis() + imageName.substring(imageName.lastIndexOf("."));
            File filePath = new File(imagePath, newFileName);
            if (!filePath.getParentFile().exists()) {
                filePath.getParentFile().mkdir();
            }
            try {
                images[i].transferTo(new File(imagePath + File.separator + newFileName));
            } catch (IOException e) {
                e.printStackTrace();
                log.error("图片上传失败:{}", imageName);
            }
            imageNames.add(newFileName);
        }
        List<BookImage> bookImages = imageNames.stream().map(i -> {
            return new BookImage(null, book.getId(), i);
        }).collect(Collectors.toList());
        boolean success = bookImageService.saveBatch(bookImages);
        if (!success) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return JsonResult.error("发布失败，请稍后再试");
        }
        return JsonResult.success(res);
    }

    @Override
    public JsonResult<Book> findBookById(Integer bookId) {
        Book book = bookMapper.selectById(bookId);
        if (book != null) {
            return JsonResult.success(book);
        }
        return JsonResult.error("找不到书籍信息");
    }

    @Override
    public JsonResult<Integer> updateBook(Book book) {
        Book oldBook = bookMapper.selectById(book.getId());
        if (oldBook == null) {
            return JsonResult.error("图书不存在");
        }
        book.setId(oldBook.getId());
        BeanUtils.copyProperties(book, oldBook);
        oldBook.setDate(LocalDateTime.now());
        int res = bookMapper.updateById(oldBook);
        if (res > 0) {
            return JsonResult.success(res);
        }
        return JsonResult.error("修改失败，请稍后再试");
    }

    @Override
    public BookAndImagesVO findBookAndImages(Integer bookId) {
        BookAndImagesVO vo = new BookAndImagesVO();

        JsonResult<Book> result = findBookById(bookId);
        if (!result.getCode().equals(JsonResult.SUCCESS_CODE)) {
            return null;
        }
        vo.setBook(result.getData());
        List<BookImage> bookImages = bookImageService.findImagesByBookId(bookId);
        if (CollectionUtils.isEmpty(bookImages)) {
            vo.setBookImages(Collections.emptyList());
        } else {
            vo.setBookImages(bookImages);
        }
        return vo;
    }

    @Transactional
    @Override
    public JsonResult<Boolean> commentBook(Integer bookId, String comment) {
        Book book = bookMapper.selectById(bookId);
        if (book == null) {
            return JsonResult.error("书籍不存在");
        }
        User currentUser = authenticationService.findCurrentUser();
        User receiver = userMapper.selectById(book.getUid());
        Message message = new Message();
        message.setBid(bookId)
            .setBname(book.getName())
            .setReceiveUid(receiver.getId())
            .setReceiver(receiver.getName())
            .setSendUid(currentUser.getId())
            .setSender(currentUser.getName())
            .setContent(comment)
            .setDate(LocalDateTime.now())
            .setType(1)
            .setParentId(0)
            .setIsRead(0);
        int res2 = messageMapper.insert(message);
        if (res2 < 1) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return JsonResult.error("评论失败，请稍后再试");
        }
        return JsonResult.success(true);
    }

    @Override
    public JsonResult<List<BookMessageAndCommentVO>> findComment(Integer bookId) {
        /**
         * 获取所有评论和对应的用户名信息
         */
        QueryWrapper<Message> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("bid", bookId).eq("type", 1);
        List<Message> messageList = messageMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(messageList)) {
            return JsonResult.success(Collections.emptyList());
        }

        Map<Integer, List<Message>> messageGroup =
            messageList
                .stream()
                .collect(Collectors.groupingBy(Message::getParentId));

        List<Message> parentMessageList = messageGroup.getOrDefault(0, Collections.emptyList());
        List<BookMessageAndCommentVO> voList = parentMessageList.stream().map(p -> {
            BookMessageAndCommentVO vo = new BookMessageAndCommentVO();
            vo.setCommentDetails(p);
            List<Message> replyCommentDetails = messageGroup.get(p.getId());
            if (CollectionUtils.isEmpty(replyCommentDetails)) {
                vo.setSubCommentDetails(Collections.emptyList());
            } else {
                vo.setSubCommentDetails(replyCommentDetails);
            }
            return vo;
        }).collect(Collectors.toList());

        return JsonResult.success(voList);
    }

    @Override
    public IPage<BookImageVO> findBookImageVOList(Integer page, Integer categoryId, String search) {
        Page<BookImageVO> pageObj = new Page<>(page, 20);
        IPage<BookImageVO> bookImageVOList = bookMapper.findBookImageVOList(pageObj, categoryId, search);
        return bookImageVOList;
    }

    @Override
    public JsonResult<Boolean> contact(Integer bookId, String content) {
        Book book = bookMapper.selectById(bookId);
        if (book == null) {
            return JsonResult.error("书籍不存在");
        }
        User currentUser = authenticationService.findCurrentUser();
        User receiver = userMapper.selectById(book.getUid());
        Message message = new Message();
        message.setBid(bookId)
            .setBname(book.getName())
            .setReceiveUid(receiver.getId())
            .setReceiver(receiver.getName())
            .setSendUid(currentUser.getId())
            .setSender(currentUser.getName())
            .setContent(content)
            .setDate(LocalDateTime.now())
            .setType(2)
            .setParentId(0)
            .setIsRead(0);
        int res2 = messageMapper.insert(message);
        if (res2 < 1) {
            return JsonResult.error("发送失败，请稍后再试");
        }
        return JsonResult.success(true);
    }

    @Override
    public JsonResult<Integer> purchase(String title, String content) {
        Purchase purchase = new Purchase();
        User currentUser = authenticationService.findCurrentUser();
        purchase
            .setUid(currentUser.getId())
            .setTitle(title)
            .setUname(currentUser.getName())
            .setContent(content)
            .setDate(LocalDateTime.now());
        Integer res = purchaseMapper.insert(purchase);
        if (res < 1){
            return JsonResult.error("发布失败，请稍后再试");
        }
        return JsonResult.success(res);
    }

    @Override
    public JsonResult<Boolean> contactPurchaser(Integer purchaseId, String content) {
        Purchase purchase = purchaseMapper.selectById(purchaseId);
        if (purchase == null) {
            return JsonResult.error("求购信息不存在");
        }
        User currentUser = authenticationService.findCurrentUser();
        User receiver = userMapper.selectById(purchase.getUid());
        Message message = new Message();
        message.setBid(purchaseId)
            .setBname(purchase.getTitle())
            .setReceiveUid(receiver.getId())
            .setReceiver(receiver.getName())
            .setSendUid(currentUser.getId())
            .setSender(currentUser.getName())
            .setContent(content)
            .setDate(LocalDateTime.now())
            .setType(3)
            .setParentId(0)
            .setIsRead(0);
        int res2 = messageMapper.insert(message);
        if (res2 < 1) {
            return JsonResult.error("发送失败，请稍后再试");
        }
        return JsonResult.success(true);
    }

}
