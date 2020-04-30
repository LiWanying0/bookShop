package com.nit.book.shop.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nit.book.shop.common.JsonResult;
import com.nit.book.shop.entity.Book;
import com.nit.book.shop.entity.BookDTO;
import com.nit.book.shop.entity.BookImage;
import com.nit.book.shop.entity.BookIndexVO;
import com.nit.book.shop.mapper.BookImageMapper;
import com.nit.book.shop.mapper.BookMapper;
import com.nit.book.shop.service.BookImageService;
import com.nit.book.shop.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.interceptor.TransactionalProxy;
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

//    @Autowired
//    private BookDAO bookDAO;
//    @Autowired
//    private CategoryDAO categoryDAO;
//    @Autowired
//    private BookImageDAO bookImageDAO;
//
//    @Override
//    public Map<Category, List<Book>> listByCategory() {
//        // 获取所有Category
//        List<Category> categories = categoryDAO.list();
//        // 使用LinkedHashMap存储，若使用HashMap则无序
//        Map<Category, List<Book>> booksMap = new LinkedHashMap<>();
//        for (Category category : categories) {
//            List<Book> books = bookDAO.getListByCategoryId(0, 5, 1, category.getId());
//            if (books.size() > 0) {
//                // 当前的Book对象无BookImage，遍历每个Book对象并放入相应的BookImage
//                for (Book book : books) {
//                    book.setBookImage(bookImageDAO.getByBookId(book.getId()));
//                }
//            }
//            booksMap.put(category, books);
//        }
//        return booksMap;
//    }
//
//    @Override
//    public List<Book> listByUserId(int uid, int bookType) {
//        List<Book> books = bookDAO.getListByUserId(uid, bookType);
//        for (Book book : books) {
//            book.setBookImage(bookImageDAO.getByBookId(book.getId()));
//        }
//        return books;
//    }
//
//    @Override
//    public List<Book> listByCategoryId(int bookType, int cid) {
//        List<Book> books = bookDAO.getListByCategoryId(-1, -1, bookType, cid);
//        for (Book book : books) {
//            book.setBookImage(bookImageDAO.getByBookId(book.getId()));
//        }
//        return books;
//    }
//
//    @Override
//    public Book get(int id) {
//        Book book = bookDAO.get(id);
//        book.setBookImage(bookImageDAO.getByBookId(id));
//        return book;
//    }
//
//    @Override
//    public int getUserId(int id) {
//        return bookDAO.getUserId(id);
//    }
//
//    @Override
//    public void add(Book book) {
//        bookDAO.add(book);
//    }
//
//    @Override
//    public int count() {
//        return bookDAO.count();
//    }
//
//    @Override
//    public void delete(int id) {
//        bookDAO.delete(id);
//    }
//
//    @Override
//    public List<Book> list() {
//        List<Book> books = bookDAO.list();
//        for (Book book : books) {
//            book.setBookImage(bookImageDAO.getByBookId(book.getId()));
//        }
//        return books;
//    }
//
//    @Override
//    public List<Book> listByBookType(int bookType) {
//        List<Book> books = bookDAO.listByBookType(bookType);
//        for (Book book : books) {
//            book.setBookImage(bookImageDAO.getByBookId(book.getId()));
//        }
//        return books;
//    }
//
//    @Override
//    public void update(Book book) {
//        bookDAO.update(book);
//    }


    @Autowired
    private BookMapper bookMapper;

    @Autowired
    private BookImageMapper bookImageMapper;


    @Override
    public List<BookIndexVO> findBooksForIndex() {
        List<BookIndexVO> bookIndexVOList = new ArrayList<>();

        List<Book> books = bookMapper.selectBooksByCategoryLimit(5);

        List<Integer> bookIdList = books.stream().map(Book::getId).collect(Collectors.toList());
        QueryWrapper<BookImage> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("bid", bookIdList);
        List<BookImage> bookImages = bookImageMapper.selectList(queryWrapper);
        Map<Integer, Integer> bookImageMap = bookImages.stream().collect(Collectors.toMap(BookImage::getBid, BookImage::getId, (oldObj, newObj) -> newObj));

        List<BookDTO> bookDTOS = books.stream().map(b -> {
            BookDTO bookDTO = new BookDTO();
            BeanUtils.copyProperties(b, bookDTO);
            bookDTO.setImageId(bookImageMap.getOrDefault(b.getId(), 0));
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
        IPage<BookDTO> videoList = bookMapper.selectBookDTOPage(page, search);
        return JsonResult.success(videoList);
    }

    @Override
    public JsonResult<Integer> deleteBook(Integer bookId) {
        int res = bookMapper.deleteById(bookId);
        return JsonResult.success(res);
    }

    @Value("${image.save-path}")
    private String imagePath;

    @Autowired
    private BookImageService bookImageService;

    @Transactional
    @Override
    public JsonResult<Integer> uploadBook(Book book, MultipartFile[] images) {

        QueryWrapper<Book> bookQueryWrapper = new QueryWrapper<>();
        bookQueryWrapper.eq("uid", 1).eq("name", book.getName());
        Book oldBook = bookMapper.selectOne(bookQueryWrapper);
        if (oldBook !=null){
            return JsonResult.error("书名不能和已发布的书名相同");
        }
        book.setUid(1);
        book.setDate(LocalDateTime.now());
        int res = bookMapper.insert(book);
        if (res < 1) {
            return JsonResult.error("保存失败，请稍后再试");
        }

        if (images.length <= 1 && StringUtils.isEmpty(images[0].getOriginalFilename())){
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
        boolean success =  bookImageService.saveBatch(bookImages);
        if (!success){
            TransactionAspectSupport.currentTransactionStatus().isRollbackOnly();
            return JsonResult.error("发布失败，请稍后再试");
        }
        return JsonResult.success(res);
    }

    @Override
    public JsonResult<Book> findBookById(Integer bookId) {
        Book book = bookMapper.selectById(bookId);
        if (book!=null){
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
        if (res > 0){
            return JsonResult.success(res);
        }
        return JsonResult.error("修改失败，请稍后再试");
    }

}
