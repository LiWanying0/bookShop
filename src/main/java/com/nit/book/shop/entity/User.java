package com.nit.book.shop.entity;

import lombok.Data;

@Data
public class User {

    private int id;
    private String studentid;
    private String name;
    private String password;
    private char sex;
    private String tel;
    private String address;
    private String major;
}
