package com.nit.book.shop.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nit.book.shop.common.JsonResult;
import com.nit.book.shop.entity.Book;
import com.nit.book.shop.entity.BookDTO;
import com.nit.book.shop.entity.Category;
import com.nit.book.shop.service.BookService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private BookService bookService;

    @GetMapping("")
    public String admin() {
        return "admin/admin";
    }

    @GetMapping("personZone")
    public String personZone() {
        return "admin/personZone";
    }

   @GetMapping("publish")
    public String publish(Model model) {

       List<Category> categories = new ArrayList<>();
       Category category = new Category();
       category.setId(1);
       category.setName("社会社会");
       categories.add(category);

       Category category2 = new Category();
       category2.setId(1333);
       category2.setName("哈哈");
       categories.add(category2);
        model.addAttribute("categories", categories);
        return "admin/publish";
    }

    @GetMapping("book")
    public String book() {
        return "admin/book";
    }

    @GetMapping("collection")
    public String collection() {
        return "admin/collection";
    }


    @GetMapping("history")
    public String history() {
        return "admin/history";
    }

    @GetMapping("need")
    public String need() {
        return "admin/need";
    }

    @GetMapping("message")
    public String message() {
        return "admin/message";
    }


    @GetMapping("bookList")
    @ResponseBody
    public JsonResult<IPage<BookDTO>> bookList(
        @RequestParam(value = "pageNum", defaultValue = "0")
            Integer pageNum,
        @RequestParam(value = "search", required = false)
            String search) {
        return bookService.findBookList(pageNum, search);
    }

    @PostMapping("deleteBook")
    @ResponseBody
    public JsonResult<Integer> deleteBook(
        @RequestParam(value = "bookId")
            Integer bookId
    ) {
        return bookService.deleteBook(bookId);
    }



    @SneakyThrows
    @PostMapping("/book/upload")
    @ResponseBody
    public JsonResult<Integer> uploadBook(
        Book book,
        HttpServletRequest request,
        @RequestBody
        @RequestParam("images") MultipartFile[] images
    ) {
        return bookService.uploadBook(book, images);
    }
}
