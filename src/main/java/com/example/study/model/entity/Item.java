package com.example.study.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.criterion.Order;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String status;

    private String name;

    private String title;

    private String content;

    private Integer price;

    private String brandName;

    private LocalDateTime registeredAt;

    private LocalDateTime unregisteredAt;

    private LocalDateTime createdAt;

    private String createdBy;

    private LocalDateTime updatedAt;

    private String updatedBy;

    private Long partnerId;

    // 1 : N
    // LAZY = 지연로딩(외래키로 연결되어 있는 값에 대한 메서드를 실행 시키지 않는이상 다른 테이블의 데이터를 점근 하지 않음.
    // EAGER = 즉시로딩(외래키로 연결되어 있는 모든 테이블들에 대한 데이터를 조인을 통해 모두 가져옴)
    //@OneToMany(fetch = FetchType.LAZY, mappedBy = "item")
    //private List<OrderDetail> orderDetailList;

}
