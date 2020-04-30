package com.nit.book.shop.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class BookDTO extends Book {
    private Integer imageId;
}
