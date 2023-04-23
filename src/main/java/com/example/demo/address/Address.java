package com.example.demo.address;

import com.example.demo.user.model.Users;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Address {
    // api get, add, edit, delete
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String address;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

//    private String name_receiver;
//    private String address_receiver;


}
