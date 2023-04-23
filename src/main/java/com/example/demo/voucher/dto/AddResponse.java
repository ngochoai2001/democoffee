package com.example.demo.voucher.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddResponse {
    private String message;
    private int statusCode;
}
