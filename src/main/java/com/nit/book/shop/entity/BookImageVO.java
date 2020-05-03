package com.nit.book.shop.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class BookImageVO extends Book implements Serializable {
    private String imageName;
}
