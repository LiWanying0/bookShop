package com.nit.book.shop.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nit.book.shop.common.JsonResult;
import com.nit.book.shop.entity.*;
import com.nit.book.shop.mapper.CategoryMapper;
import com.nit.book.shop.service.BookService;
import com.nit.book.shop.service.PurchaseService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private BookService bookService;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private PurchaseService purchaseService;

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

        List<Category> categories = categoryMapper.selectList(null);
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

    @GetMapping("publishPurchase")
    public String need() {
        return "admin/publishPurchase";
    }

    @GetMapping("purchase")
    public String purchase() {
        return "admin/purchase";
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

    @GetMapping("purchaseList")
    @ResponseBody
    public JsonResult<IPage<Purchase>> purchaseList(
        @RequestParam(value = "pageNum", defaultValue = "0")
            Integer pageNum,
        @RequestParam(value = "search", required = false)
            String search) {
        return purchaseService.purchaseList(pageNum, search);
    }

    @GetMapping("bookHistory")
    @ResponseBody
    public JsonResult<IPage<HistoryVO>> bookHistory(
        @RequestParam(value = "pageNum", defaultValue = "0")
            Integer pageNum,
        @RequestParam(value = "search", required = false)
            String search) {
        return bookService.bookHistory(pageNum, search);
    }

    @GetMapping("findPurchaseById")
    @ResponseBody
    public JsonResult<Purchase> findPurchaseById(@RequestParam("purchaseId") Integer purchaseId) {
        return purchaseService.findPurchaseById(purchaseId);
    }

    @PostMapping("updatePurchase")
    @ResponseBody
    public JsonResult<Integer> updatePurchase(String purchaseId, String title, String content) {
        return purchaseService.updatePurchase(purchaseId, title, content);
    }

    @PostMapping("deleteHistory")
    @ResponseBody
    public JsonResult<Integer> deleteHistory(
        @RequestParam(value = "hid")
            Integer hid
    ) {
        return bookService.deleteHistory(hid);
    }

    @PostMapping("cleanHistory")
    @ResponseBody
    public JsonResult<Integer> cleanHistory() {
        return bookService.cleanHistory();
    }

    @PostMapping("deleteBook")
    @ResponseBody
    public JsonResult<Integer> deleteBook(
        @RequestParam(value = "bookId")
            Integer bookId
    ) {
        return bookService.deleteBook(bookId);
    }

    @PostMapping("deletePurchase")
    @ResponseBody
    public JsonResult<Integer> deletePurchase(
        @RequestParam(value = "purchaseId")
            Integer purchaseId
    ) {
        return purchaseService.deletePurchase(purchaseId);
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

    @PostMapping("/book/purchase")
    @ResponseBody
    public JsonResult<Integer> purchase(
        @RequestParam("title") String title,
        @RequestParam("content") String content
    ) {
        return bookService.purchase(title, content);
    }
}
