package com.nit.book.shop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nit.book.shop.common.JsonResult;
import com.nit.book.shop.entity.*;
import com.nit.book.shop.mapper.BookImageMapper;
import com.nit.book.shop.mapper.MessageMapper;
import com.nit.book.shop.service.AuthenticationService;
import com.nit.book.shop.service.BookImageService;
import com.nit.book.shop.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author JiaMengwei
 * @since 2020-05-01
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private AuthenticationService authenticationService;


    @Override
    public JsonResult<IPage<Message>> findMessages(Integer pageNum, Integer type) {
        User user = authenticationService.findCurrentUser();
        Page<Message> pageObj = new Page<>(pageNum, 10);
        IPage<Message> messageVOList = messageMapper.findMessages(pageObj, user.getId(), type);
        return JsonResult.success(messageVOList);
    }

    @Override
    public JsonResult<Boolean> reply(Integer messageId, String content) {
        User user = authenticationService.findCurrentUser();
        Message message = messageMapper.selectById(messageId);
        if (message == null) {
            return JsonResult.error("消息不存在");
        }
        Integer parentId = null;
        if (message.getParentId().equals(0)) {
            parentId = message.getId();
        } else {
            parentId = message.getParentId();
        }
        Message reply = new Message();
        reply
            .setParentId(parentId)
            .setBid(message.getBid())
            .setBname(message.getBname())
            .setContent(content)
            .setIsRead(0)
            .setDate(LocalDateTime.now())
            .setSendUid(user.getId())
            .setSender(user.getName())
            .setReceiveUid(message.getSendUid())
            .setReceiver(message.getSender())
            .setType(message.getType());

        int res = messageMapper.insert(reply);
        if (res > 0) {
            message.setIsRead(1);
            messageMapper.updateById(message);
            return JsonResult.success(true);
        }
        return JsonResult.success(false);
    }

    @Override
    public JsonResult<IPage<BookMessageAndCommentVO>> findMessagesVO(Integer pageNum, Integer type) {
        User user = authenticationService.findCurrentUser();
        Page<Message> pageObj = new Page<>(pageNum, 10);
        IPage<Message> messageVOList = messageMapper.findMessages(pageObj, user.getId(), type);
        List<BookMessageAndCommentVO> voList = messageVOList.getRecords().stream().map(m -> {
            BookMessageAndCommentVO vo = new BookMessageAndCommentVO();
            QueryWrapper<Message> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("parent_id", m.getId());
            List<Message> subMessageList = messageMapper.selectList(queryWrapper);
            vo.setCommentDetails(m);
            vo.setSubCommentDetails(subMessageList);
            return vo;
        }).collect(Collectors.toList());

        IPage<BookMessageAndCommentVO> messageAndCommentVOIPage = new Page();
        messageAndCommentVOIPage.setCurrent(messageVOList.getCurrent());
        messageAndCommentVOIPage.setPages(messageVOList.getPages());
        messageAndCommentVOIPage.setRecords(voList);
        messageAndCommentVOIPage.setSize(messageVOList.getSize());
        messageAndCommentVOIPage.setTotal(messageVOList.getTotal());
        messageAndCommentVOIPage.setPages(messageVOList.getPages());
        return JsonResult.success(messageAndCommentVOIPage);
    }
}
