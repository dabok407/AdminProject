package com.example.study.repository;

import com.example.study.model.entity.AdminUser;
import com.example.study.model.network.request.AdminUserApiRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminUserRepository extends JpaRepository<AdminUser, Long> {

    Optional<AdminUser> findByAccount(String account);

    AdminUser findFirstByAccount(String account);

    AdminUser findFirstByAccountAndPassword(String account, String password);

    Page<AdminUser> findAllByAccount(Pageable pageable, String account);

    Page<AdminUser> findAllByRole(Pageable pageable, String role);

    Page<AdminUser> findAllByAccountAndRole(Pageable pageable, String account, String role);

}
