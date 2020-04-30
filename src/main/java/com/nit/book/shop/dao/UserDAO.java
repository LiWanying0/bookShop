package com.nit.book.shop.dao;

import com.nit.book.shop.entity.User;

public interface UserDAO {

    User get(int id);

    void update(User user);

    int checkPassword(User user);

    User getByStudentid(String studentid);

}
