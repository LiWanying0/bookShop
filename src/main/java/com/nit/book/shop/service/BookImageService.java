package com.nit.book.shop.service;

import com.baomidou.mybatisplus.extension.service.IService;

import com.nit.book.shop.entity.BookImage;

import java.util.List;

public interface BookImageService extends IService<BookImage> {

    List<BookImage> findImagesByBookId(Integer bookId);
}
