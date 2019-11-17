package com.example.study.repository;

import com.example.study.StudyApplicationTests;
import com.example.study.model.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.junit.*;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

public class UserRepositoryTest extends StudyApplicationTests{

    @Autowired // DI Singleton Pattern
    private UserRepository userRepository;

    @Test
    public void create(){

        String account = "Test01";
        String password = "Test01";
        String status = "REGISTERD";
        String email = "Test01@gmail.com";
        String phoneNumber = "010-1111-2222";
        LocalDateTime registeredAt = LocalDateTime.now();
        LocalDateTime createdAt = LocalDateTime.now();
        String createdBy = "AdminServer";

        User user = new User();
        user.setAccount(account);
        user.setPassword(password);
        user.setStatus(status);
        user.setEmail(email);
        user.setPhoneNumber(phoneNumber);
        user.setRegisteredAt(registeredAt);
        user.setCreatedAt(createdAt);
        user.setCreatedBy(createdBy);

        User resultUser = userRepository.save(user);
        Assert.assertNotNull(resultUser);

        /*User user = new User();
        user.setAccount("TestUser03");
        user.setEmail("TestUser@test.com");
        user.setPhoneNumber("010-3333-3333");
        user.setCreatedBy("admin");
        user.setCreatedAt(LocalDateTime.now());

        User newUser = userRepository.save(user);*/
    }

    @Test
    @Transactional
    public void read(){

        User user = userRepository.findFirstByPhoneNumberOrderByIdDesc("010-1111-2222");
        Assert.assertNotNull(user);

        //Optional<User> user = userRepository.findById(1L);
        //Optional<User> user = userRepository.findByAccount("TestUser03");
        //Optional<User> user = userRepository.findByAccountAndEmail("TestUser03", "test@test.com");

        /*user.ifPresent(selectUser ->{
            //selectUser.getOrderDetailList().stream().forEach(orderDetail -> {
                //System.out.println(orderDetail.getItem());
            });
        });*/
    }

    @Test
    public void update(){
        Optional<User> user = userRepository.findById(2L);

        user.ifPresent(selectUser ->{
            selectUser.setAccount("pppp");
            selectUser.setUpdatedAt(LocalDateTime.now());
            selectUser.setUpdatedBy("update method()");

            userRepository.save(selectUser);
        });
    }

    @Test
    @Transactional
    public void delete(){
        Optional<User> user = userRepository.findById(3L);

        Assert.assertTrue(user.isPresent()); // true

        user.ifPresent(selectUser ->{
            userRepository.delete(selectUser);
        });

        Optional<User> resultUser = userRepository.findById(3L);

        Assert.assertFalse(resultUser.isPresent()); // false
    }

}
