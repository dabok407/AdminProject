package com.example.study.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Entity
//@Table(name = "user") // DB테이블명과 클래스명이 동일 하다면 설정 하지 않아도 됨.
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //@Column(name = "account") DB컬럼명과 동일 하다면 설정 하지 않아도 됨.
    private String account;

    private String email;

    private String phoneNumber;

    private LocalDateTime createdAt;

    private  String createBy;

    private LocalDateTime updatedAt;

    private  String updateBy;

}
