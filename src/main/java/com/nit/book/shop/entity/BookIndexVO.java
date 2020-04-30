package com.nit.book.shop.entity;

import lombok.Data;

import java.util.List;

@Data
public class BookIndexVO {

    private String categoryName;

    private List<BookDTO> books;
}
