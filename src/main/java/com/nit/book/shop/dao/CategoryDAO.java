package com.nit.book.shop.dao;

import java.util.List;

import com.nit.book.shop.entity.Category;

public interface CategoryDAO {

    void add(Category category);

    void delete(int id);

    Category get(int id);

    void update(Category category);

    // 获取所有Category
    List<Category> list();

    int count();

}
