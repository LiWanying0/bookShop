package com.nit.book.shop.service;


import com.nit.book.shop.entity.User;

public interface AuthenticationService {

    /**
     * 获取当前登录用户
     *
     * @return
     */
    User findCurrentUser();

    Boolean isLogin();
}
