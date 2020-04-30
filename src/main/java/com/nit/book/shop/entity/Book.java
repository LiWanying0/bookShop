package com.nit.book.shop.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class Book implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String name;

    private Integer cid;

    private Double price;

    private Double originalPrice;

    private Integer uid;

    private String author;

    private String press;

    private String description;

    private LocalDateTime date;


}
