package com.example.study.repository;

import com.example.study.model.entity.OrderDetail;
import com.example.study.model.entity.OrderGroup;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail,Long> {

    List<OrderDetail> findByOrderGroup_Id(Long orderGroupId);

    @Transactional
    int deleteByOrderGroup(OrderGroup orderGroup);
}
