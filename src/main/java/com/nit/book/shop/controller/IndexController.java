package com.nit.book.shop.controller;

import com.nit.book.shop.entity.BookIndexVO;
import com.nit.book.shop.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class IndexController {

    @Autowired
    private BookService bookService;

    @GetMapping("")
    public String index(Model model){
       List<BookIndexVO> bookIndexVOList =  bookService.findBooksForIndex();
       model.addAttribute("categoryBooks", bookIndexVOList);
        return "index";
    }
}
