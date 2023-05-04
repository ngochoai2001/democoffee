package com.example.demo.header;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HeaderService {

    @Autowired
    HeaderRepository headerRepository;
    public List<Header> getHeaders(){
        return headerRepository.getHeaders();
    }

}
