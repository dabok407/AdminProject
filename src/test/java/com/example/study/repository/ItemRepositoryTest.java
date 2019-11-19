package com.example.study.repository;

import com.example.study.StudyApplicationTests;
import com.example.study.model.entity.Item;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Optional;

public class ItemRepositoryTest extends StudyApplicationTests {

    @Autowired
    private ItemRepository itemRepository;

    @Test
    public void create(){

        Item item = new Item();
        item.setStatus("UNREGISTERED");
        item.setName("삼성 노트북");
        item.setTitle("삼성 노트북 TEST01");
        item.setPrice(100000);
        item.setContent("Samsung NoteBook - TEST01");
        item.setBrandName("Samsung");
        item.setRegisteredAt(LocalDateTime.now());
        item.setCreatedAt(LocalDateTime.now());
        item.setCreatedBy("Partner01");
        //item.setPartnerId(1L);

        Item resultItem = itemRepository.save(item);
        Assert.assertNotNull(resultItem);
    }

    @Test
    public void read(){
        Long id = 1L;

        Optional<Item> item = itemRepository.findById(id);
        Assert.assertTrue(item.isPresent());

        /*item.ifPresent(i -> {
            System.out.println(i);
        });*/
    }

}
