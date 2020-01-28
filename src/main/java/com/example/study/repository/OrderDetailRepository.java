package com.example.study.repository;

import com.example.study.model.entity.OrderDetail;
import com.example.study.model.entity.OrderGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail,Long> {

    List<OrderDetail> findByOrderGroup_Id(Long orderGroupId);

}
