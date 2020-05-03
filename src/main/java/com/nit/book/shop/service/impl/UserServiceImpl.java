package com.nit.book.shop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.nit.book.shop.entity.User;
import com.nit.book.shop.mapper.UserMapper;
import com.nit.book.shop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserMapper userMapper;

    @Override
    public User findByStudentId(String username) {
        QueryWrapper<com.nit.book.shop.entity.User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("studentid", username);
        return userMapper.selectOne(queryWrapper);

    }

//    @Autowired
//    private UserDAO userDAO;
//
//    @Override
//    public boolean checkUser(User user) {
//        int flag = userDAO.checkPassword(user);
//        return flag==1;
//    }
//
//    @Override
//    public User get(int id) {
//        return userDAO.get(id);
//    }
//
//    @Override
//    public User getByStudentid(String studnetid) {
//        return userDAO.getByStudentid(studnetid);
//    }

}
