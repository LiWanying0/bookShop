package com.nit.book.shop.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.nit.book.shop.common.JsonResult;
import com.nit.book.shop.entity.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BookService extends IService<Book> {

//    Book get(int id);
//
//    int getUserId(int id);
//
//    void add(Book book);
//
//    int count();
//
//    void delete(int id);
//
//    List<Book> list();

    /**
     * 用于myBookshelf页面，展示当前用户的图书
     * @param uid 用户Id
     * @param bookType 书的类型（图书信息1/求书信息0）
     * @return 图书List
     */
//    List<Book> listByUserId(int uid,int bookType);

    /**
     * 用于展示具体某类图书
     * @param bookType 书的类型（图书信息1/求书信息0）
     * @param cid CategoryId
     * @return 图书List
     */
//    List<Book> listByCategoryId(int bookType,int cid);
//
//    List<Book> listByBookType(int bookType);

    /**
     * @return 获取一个Key为Category，Value为对应当前Category的存放Book的List
     */
//    Map<Category,List<Book>> listByCategory();
//
//    void update(Book book);

    /**
     * 获取所有书籍，每个类型5本，用于首页展示
     * @return
     */
    List<BookIndexVO> findBooksForIndex();

    JsonResult<IPage<BookDTO>> findBookList(Integer pageNum, String search);

    /**
     * 删除指定书
     *
     * @param bookId
     * @return
     */
    JsonResult<Integer> deleteBook(Integer bookId);

    /**
     * 修改书籍信息
     *
     * @param book
     * @param images
     * @return
     */
    JsonResult<Integer> uploadBook(Book book, MultipartFile[] images);

    JsonResult<Book> findBookById(Integer bookId);

    JsonResult<Integer> updateBook(Book book);

    BookAndImagesVO findBookAndImages(Integer bookId);

    JsonResult<Boolean> commentBook(Integer bookId, String comment);

    JsonResult<List<BookMessageAndCommentVO>> findComment(Integer bookId);

    IPage<BookImageVO> findBookImageVOList(Integer page, Integer categoryId, String search);

    JsonResult<Boolean> contact(Integer bookId);

    JsonResult<Integer> purchase(String title, String content);

    JsonResult<Boolean> contactPurchaser(Integer purchaseId);

    JsonResult<IPage<HistoryVO>> bookHistory(Integer pageNum, String search);

    JsonResult<Integer> deleteHistory(Integer hid);

    JsonResult<Integer> cleanHistory();
}
