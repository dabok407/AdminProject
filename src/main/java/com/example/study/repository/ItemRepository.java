package com.example.study.repository;

import com.example.study.model.entity.Item;
import com.example.study.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item,Long> {

    Page<Item> findAll(Specification<Item> spec, Pageable pageable);

}
