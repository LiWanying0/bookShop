package com.nit.book.shop.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.nit.book.shop.common.JsonResult;
import com.nit.book.shop.entity.BookComment;
import com.nit.book.shop.entity.BookMessageAndCommentVO;
import com.nit.book.shop.entity.Message;
import com.nit.book.shop.entity.MessageVO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author JiaMengwei
 * @since 2020-05-01
 */
public interface MessageService extends IService<Message> {

    JsonResult<IPage<Message>> findMessages(Integer pageNum, Integer type);

    JsonResult<Boolean> reply(Integer messageId, String content);

    JsonResult<IPage<BookMessageAndCommentVO>> findMessagesVO(Integer pageNum, Integer type);
}
