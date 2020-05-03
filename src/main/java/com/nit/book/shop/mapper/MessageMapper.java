package com.nit.book.shop.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nit.book.shop.entity.Bulletin;
import com.nit.book.shop.entity.Message;
import com.nit.book.shop.entity.MessageVO;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author JiaMengwei
 * @since 2020-05-01
 */
public interface MessageMapper extends BaseMapper<Message> {

    IPage<Message> findMessages(@Param("page") Page<Message> page, @Param("uid") Integer uid, @Param("type") Integer type);
}
