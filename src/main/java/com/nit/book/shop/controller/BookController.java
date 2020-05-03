package com.nit.book.shop.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.nit.book.shop.common.JsonResult;
import com.nit.book.shop.entity.*;
import com.nit.book.shop.mapper.HistoryMapper;
import com.nit.book.shop.mapper.PurchaseMapper;
import com.nit.book.shop.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.html.HTMLIsIndexElement;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/book")
public class BookController {

    @Autowired
    private UserService userService;
    @Autowired
    private BookService bookService;
    @Autowired
    private BookImageService bookImageService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private HistoryMapper historyMapper;


    @GetMapping("findBookById")
    @ResponseBody
    public JsonResult<Book> findBookById(Integer bookId) {
        return bookService.findBookById(bookId);
    }

    @PostMapping("updateBook")
    @ResponseBody
    public JsonResult<Integer> updateBook(Book book) {
        return bookService.updateBook(book);
    }

    @GetMapping("/{bookId}")
    public String bookDetails(@PathVariable("bookId") Integer bookId, Model model) {
        JsonResult<Book> result = bookService.findBookById(bookId);
        if (authenticationService.isLogin()){
            User currentUser = authenticationService.findCurrentUser();
            QueryWrapper<History> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("uid", currentUser.getId()).eq("bid", bookId);
            History history = historyMapper.selectOne(queryWrapper);
            if (history != null){
                history.setDate(LocalDateTime.now());
                historyMapper.updateById(history);
            }else{
                history = new History();
                history.setBid(bookId).setUid(currentUser.getId()).setDate(LocalDateTime.now());
                historyMapper.insert(history);
            }
        }

        model.addAttribute("book", result.getData());
        return "bookDetails";
    }

    @GetMapping("/images/{bookId}")
    @ResponseBody
    public JsonResult<List<BookImage>> bookImages(@PathVariable("bookId") Integer bookId) {
        List<BookImage> bookImages = bookImageService.findImagesByBookId(bookId);
        return JsonResult.success(bookImages);
    }

    @GetMapping("/comments/{bookId}")
    @ResponseBody
    public JsonResult<List<BookMessageAndCommentVO>> findComment(
        @PathVariable("bookId") Integer bookId) {
        return bookService.findComment(bookId);
    }

    @PostMapping("/comment")
    @ResponseBody
    public JsonResult<Boolean> commentBook(
        @RequestParam("bookId") Integer bookId,
        @RequestParam("comment")String comment) {
        return bookService.commentBook(bookId, comment);
    }

    @PostMapping("/contact")
    @ResponseBody
    public JsonResult<Boolean> contact (
        @RequestParam("bookId") Integer bookId) {
        return bookService.contact(bookId);
    }

    @PostMapping("/contactPurchaser")
    @ResponseBody
    public JsonResult<Boolean> contactPurchaser (
        @RequestParam("purchaseId") Integer purchaseId) {
        return bookService.contactPurchaser(purchaseId);
    }
}
