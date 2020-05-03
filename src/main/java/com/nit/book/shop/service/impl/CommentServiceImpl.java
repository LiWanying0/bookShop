package com.nit.book.shop.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nit.book.shop.entity.BookComment;
import com.nit.book.shop.mapper.BookCommentMapper;
import com.nit.book.shop.service.CommentService;
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
public class CommentServiceImpl extends ServiceImpl<BookCommentMapper, BookComment> implements CommentService {

}
