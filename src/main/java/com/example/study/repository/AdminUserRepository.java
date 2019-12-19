package com.example.study.repository;

import com.example.study.model.entity.AdminUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminUserRepository extends JpaRepository<AdminUser, Long> {

    Optional<AdminUser> findByAccount(String account);

    AdminUser findFirstByAccount(String account);

    AdminUser findFirstByAccountAndPassword(String account, String password);
}
