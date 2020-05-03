package com.nit.book.shop.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class HistoryVO implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer bid;

    private String bname;

    private Integer price;

    private Integer uid;

    private LocalDateTime date;
}