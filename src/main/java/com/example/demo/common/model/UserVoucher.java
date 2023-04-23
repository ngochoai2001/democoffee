package com.example.demo.common.model;

import com.example.demo.user.model.Users;
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
    @JoinColumn(name = "user_id")
    private Users user;
    private boolean isUsed;

}
