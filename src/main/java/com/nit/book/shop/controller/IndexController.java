package com.nit.book.shop.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nit.book.shop.entity.*;
import com.nit.book.shop.mapper.BulletinMapper;
import com.nit.book.shop.mapper.CategoryMapper;
import com.nit.book.shop.service.BookService;
import com.nit.book.shop.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class IndexController {

    @Autowired
    private BookService bookService;

    @Autowired
    private PurchaseService purchaseService;

    @Autowired
    private CategoryMapper categoryMapper;


    @Autowired
    private BulletinMapper bulletinMapper;


    @GetMapping("")
    public String index(Model model) {
        List<BookIndexVO> bookIndexVOList = bookService.findBooksForIndex();
        List<Bulletin> bulletinList = bulletinMapper.selectList(null);
        Bulletin bulletin = null;
        if (StringUtils.isEmpty(bulletinList)){
            bulletin = new Bulletin();
            bulletin.setDate(LocalDateTime.now());
            bulletin.setTitle("欢迎");
            bulletin.setContent("");
        }else{
            bulletin  = bulletinList.get(bulletinList.size()-1);
        }

        model.addAttribute("categoryBooks", bookIndexVOList);
        model.addAttribute("bulletin", bulletin);
        return "index";
    }

    @GetMapping("books")
    public String books(
        @RequestParam(value = "categoryId", required = false, defaultValue = "0") Integer categoryId,
        @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
        @RequestParam(value = "search", required = false) String search,
        Model model) {

        List<Category> categories = categoryMapper.selectList(null);
        IPage<BookImageVO> bookImageVOS = bookService.findBookImageVOList(page, categoryId, search);

        model.addAttribute("categories", categories);
        model.addAttribute("books", bookImageVOS.getRecords());
        model.addAttribute("total", bookImageVOS.getTotal());
        model.addAttribute("pages", bookImageVOS.getPages());
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("current", bookImageVOS.getCurrent());
        return "books";
    }

    @GetMapping("purchases")
    public String purchase(
        @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
        @RequestParam(value = "search", required = false) String search,
        Model model) {
        IPage<Purchase> purchases = purchaseService.findAllPurchases(page, search);
        model.addAttribute("purchases", purchases.getRecords());
        model.addAttribute("total", purchases.getTotal());
        model.addAttribute("pages", purchases.getPages());
        model.addAttribute("current", purchases.getCurrent());
        return "purchases";
    }
}
