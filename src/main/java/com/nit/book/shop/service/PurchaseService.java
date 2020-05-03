package com.nit.book.shop.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.nit.book.shop.entity.BookComment;
import com.nit.book.shop.entity.Purchase;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author JiaMengwei
 * @since 2020-05-01
 */
public interface PurchaseService extends IService<Purchase> {


    IPage<Purchase> findAllPurchases(Integer page, String search);
}
