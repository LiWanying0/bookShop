package com.nit.book.shop.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nit.book.shop.common.JsonResult;
import com.nit.book.shop.entity.*;
import com.nit.book.shop.service.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/message")
public class MessageController {


    @Autowired
    private MessageService messageService;


//    @GetMapping("")
//    @ResponseBody
//    public JsonResult<IPage<Message>> findMessages(
//        @RequestParam(value = "pageNum", required = false, defaultValue = "1")Integer pageNum,
//        @RequestParam(value = "type", required = false, defaultValue = "1")Integer type
//    ){
//        return messageService.findMessages(pageNum, type);
//    }

    @GetMapping("")
    @ResponseBody
    public JsonResult<IPage<BookMessageAndCommentVO>> findMessagesVO(
        @RequestParam(value = "pageNum", required = false, defaultValue = "1")Integer pageNum,
        @RequestParam(value = "type", required = false, defaultValue = "1")Integer type
    ){
        return messageService.findMessagesVO(pageNum, type);
    }


    @PostMapping("/reply")
    @ResponseBody
    public JsonResult<Boolean> reply(
        @RequestParam("messageId") Integer messageId,
        @RequestParam("content")String content) {
        return messageService.reply(messageId, content);
    }
}
