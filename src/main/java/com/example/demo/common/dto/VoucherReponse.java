package com.example.demo.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VoucherReponse {
    int id;
    String image;
    String name;
    String quantity;
    String date;
    private long expireDateLeft;
    String description;

}
