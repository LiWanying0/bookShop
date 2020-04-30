package com.nit.book.shop.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nit.book.shop.dao.BookDAO;
import com.nit.book.shop.entity.Book;
import com.nit.book.shop.entity.BookDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author JiaMengwei
 * @since 2020-04-29
 */
public interface BookMapper extends BaseMapper<Book> {

    List<Book> selectBooksByCategoryLimit(@Param("limit") Integer limit);

    IPage<BookDTO> selectBookDTOPage(@Param("page") Page<BookDTO> page, @Param("search") String search);
}
