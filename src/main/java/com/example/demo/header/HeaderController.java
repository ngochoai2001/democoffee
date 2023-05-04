package com.example.demo.header;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/home")
public class HeaderController {

    @Autowired
    HeaderService headerService;

    @GetMapping("/headers")
    public List<Header> getHeaders(){
        return headerService.getHeaders();
    }
}
