package com.nit.book.shop.service;


import com.nit.book.shop.entity.User;

public interface UserService {
    User findByStudentId(String username);

}
