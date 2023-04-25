package com.example.demo.voucher.model;

import com.example.demo.user.model.Users;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name="user_voucher")
public class UserVoucher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "vch_id")
    private Voucher vch;
    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "user_id")
    private Users user;
    private boolean isUsed;

}