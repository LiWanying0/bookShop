package com.nit.book.shop.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nit.book.shop.dao.BookImageDAO;
import com.nit.book.shop.entity.BookImage;
import com.nit.book.shop.mapper.BookImageMapper;
import com.nit.book.shop.service.BookImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookImageServiceImpl  extends ServiceImpl<BookImageMapper, BookImage> implements BookImageService{

}
