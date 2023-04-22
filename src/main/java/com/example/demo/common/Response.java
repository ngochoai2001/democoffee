package com.example.demo.common;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Data
public class Response {
    Object data;
    int status;
    String message;

    public Response(Object data, int status, String message) {
        this.data = data;
        this.status = status;
        this.message = message;
    }


    public static ResponseEntity<Response> response(Object data, int status, String message){
        return ResponseEntity.ok(new Response(data, status, message));
    }
}
