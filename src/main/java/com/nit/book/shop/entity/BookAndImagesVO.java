package com.nit.book.shop.entity;

import lombok.Data;

import java.util.List;

@Data
public class BookAndImagesVO {
    private Book book;
    private List<BookImage> bookImages;
}
