package com.nit.book.shop.service.impl;


import com.nit.book.shop.service.AuthenticationService;
import com.nit.book.shop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    private UserService userService;


    @Override
    public com.nit.book.shop.entity.User findCurrentUser() {
        com.nit.book.shop.entity.User currentUser = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        if (principal != null && principal instanceof UserDetails) {
            User user = (User) authentication.getPrincipal();
            String username = user.getUsername();
            currentUser = userService.findByStudentId(username);
        } else {
            throw new AuthenticationCredentialsNotFoundException("请登录");
        }
        if (currentUser == null) {
            throw new AuthenticationCredentialsNotFoundException("用户状态异常");
        }
        return currentUser;
    }

    @Override
    public Boolean isLogin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        if (principal != null && principal instanceof UserDetails) {
            return true;
        }
        return false;
    }
}
