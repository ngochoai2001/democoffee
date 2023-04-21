package com.example.demo.address.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
public class Address {
    // api get, add, edit, delete
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String address;
}
