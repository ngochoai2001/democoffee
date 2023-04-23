package com.example.demo.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VoucherReponse {
    int id;
    String image;
    String name;
    String date;
    private long expireDateLeft;
    private boolean isUsed;
    String description;

}
