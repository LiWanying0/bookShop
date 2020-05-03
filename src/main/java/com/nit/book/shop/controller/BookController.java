package com.nit.book.shop.controller;

import com.nit.book.shop.common.JsonResult;
import com.nit.book.shop.entity.Book;
import com.nit.book.shop.entity.BookMessageAndCommentVO;
import com.nit.book.shop.entity.BookImage;
import com.nit.book.shop.service.BookImageService;
import com.nit.book.shop.service.BookService;
import com.nit.book.shop.service.CategoryService;
import com.nit.book.shop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
        @RequestParam("bookId") Integer bookId,
        @RequestParam("content")String content) {
        return bookService.contact(bookId, content);
    }

    @PostMapping("/contactPurchaser")
    @ResponseBody
    public JsonResult<Boolean> contactPurchaser (
        @RequestParam("purchaseId") Integer purchaseId,
        @RequestParam("content")String content) {
        return bookService.contactPurchaser(purchaseId, content);
    }
}
