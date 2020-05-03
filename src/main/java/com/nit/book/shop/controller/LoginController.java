package com.nit.book.shop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @RequestMapping("login")
    public String login(String error){
        System.out.println(error);
        return "login";
    }
}
