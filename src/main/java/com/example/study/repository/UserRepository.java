package com.example.study.repository;

import com.example.study.model.entity.User;
import com.example.study.model.enumclass.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    User findFirstByPhoneNumberOrderByIdDesc(String phoneNumber);

    Page<User> findAllByAccount(Pageable pageable, String account);

    Page<User> findAllByStatus(Pageable pageable, UserStatus status);

    Page<User> findAllByAccountAndStatus(Pageable pageable, String account, UserStatus status);
}
