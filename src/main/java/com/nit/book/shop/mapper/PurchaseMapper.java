package com.nit.book.shop.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nit.book.shop.entity.Bulletin;
import com.nit.book.shop.entity.Purchase;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author JiaMengwei
 * @since 2020-05-01
 */
public interface PurchaseMapper extends BaseMapper<Purchase> {

    IPage<Purchase> findPurchasePage(@Param("page") Page<Purchase> page, @Param("search") String search);
}
