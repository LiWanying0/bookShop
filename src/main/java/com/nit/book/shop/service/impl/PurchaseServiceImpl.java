package com.nit.book.shop.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nit.book.shop.entity.BookComment;
import com.nit.book.shop.entity.BookImageVO;
import com.nit.book.shop.entity.Purchase;
import com.nit.book.shop.mapper.BookCommentMapper;
import com.nit.book.shop.mapper.PurchaseMapper;
import com.nit.book.shop.service.CommentService;
import com.nit.book.shop.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author JiaMengwei
 * @since 2020-05-01
 */
@Service
public class PurchaseServiceImpl extends ServiceImpl<PurchaseMapper, Purchase> implements PurchaseService {

    @Autowired
    private PurchaseMapper purchaseMapper;

    @Override
    public IPage<Purchase> findAllPurchases(Integer page, String search) {
        Page<Purchase> pageObj = new Page<>(page, 20);
        IPage<Purchase> purchases = purchaseMapper.findPurchasePage(pageObj, search);
        return purchases;
    }
}
