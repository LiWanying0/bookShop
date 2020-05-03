package com.nit.book.shop.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nit.book.shop.common.JsonResult;
import com.nit.book.shop.entity.*;
import com.nit.book.shop.mapper.BookCommentMapper;
import com.nit.book.shop.mapper.PurchaseMapper;
import com.nit.book.shop.service.AuthenticationService;
import com.nit.book.shop.service.CommentService;
import com.nit.book.shop.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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

    @Autowired
    private AuthenticationService authenticationService;

    @Override
    public IPage<Purchase> findAllPurchases(Integer page, String search) {
        Page<Purchase> pageObj = new Page<>(page, 20);
        IPage<Purchase> purchases = purchaseMapper.findPurchasePage(pageObj, search);
        return purchases;
    }

    @Override
    public JsonResult<IPage<Purchase>> purchaseList(Integer pageNum, String search) {
        Page<Purchase> page = new Page(pageNum, 15);
        User user = authenticationService.findCurrentUser();
        IPage<Purchase> videoList = purchaseMapper.findPurchasePageByUserId(page, search, user.getId());
        return JsonResult.success(videoList);
    }

    @Override
    public JsonResult<Integer> deletePurchase(Integer purchaseId) {
        int res = purchaseMapper.deleteById(purchaseId);
        return JsonResult.success(res);
    }

    @Override
    public JsonResult<Purchase> findPurchaseById(Integer purchaseId) {
        Purchase purchase = purchaseMapper.selectById(purchaseId);
        if (purchase != null) {
            return JsonResult.success(purchase);
        }
        return JsonResult.error("找不到求购信息");
    }

    @Override
    public JsonResult<Integer> updatePurchase(String purchaseId, String title, String content) {
        Purchase purchase = purchaseMapper.selectById(purchaseId);
        if (purchase == null) {
            return JsonResult.error("找不到求购信息");
        }
        purchase.setTitle(title);
        purchase.setContent(content);
        purchase.setDate(LocalDateTime.now());
        int res = purchaseMapper.updateById(purchase);
        return JsonResult.success(res);
    }
}
