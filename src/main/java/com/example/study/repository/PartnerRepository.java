package com.example.study.repository;

import com.example.study.model.entity.Category;
import com.example.study.model.entity.Partner;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface PartnerRepository extends JpaRepository<Partner, Long> {

    List<Partner> findByCategory(Category category);

    @Query(value="select id, name from partner", nativeQuery=true)
    List<Map<String, String>> findCodeAll();
}
