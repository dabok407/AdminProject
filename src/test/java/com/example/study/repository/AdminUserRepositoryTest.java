package com.example.study.repository;

import com.example.study.StudyApplicationTests;
import com.example.study.model.entity.AdminUser;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

public class AdminUserRepositoryTest extends StudyApplicationTests {

    @Autowired
    private AdminUserRepository adminUserRepository;

    @Test
    public void create(){

        AdminUser adminUser = new AdminUser();
        adminUser.setAccount("AdminUser03");
        adminUser.setPassword("AdminUser03");
        adminUser.setStatus("REGISTERED");
        adminUser.setRole("SUPER");
        //adminUser.setCreatedAt(LocalDateTime.now());
        //adminUser.setCreatedBy("AdminServer");

        AdminUser resultAdminUser = adminUserRepository.save(adminUser);

        Assert.assertNotNull(resultAdminUser);

        resultAdminUser.setAccount("CHANGE");
        adminUserRepository.save(resultAdminUser);

    }
}
