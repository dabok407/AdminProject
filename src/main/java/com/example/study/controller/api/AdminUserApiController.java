package com.example.study.controller.api;

import com.example.study.ifs.CrudInterface;
import com.example.study.model.network.Header;
import com.example.study.model.network.request.AdminUserApiRequest;
import com.example.study.model.network.request.UserApiRequest;
import com.example.study.model.network.response.AdminUserApiResponse;
import com.example.study.model.network.response.UserApiResponse;
import com.example.study.model.network.response.UserOrderInfoApiResponse;
import com.example.study.service.AdminUserApiLogicService;
import com.example.study.service.UserApiLogicService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/adminUser")
public class AdminUserApiController implements CrudInterface<AdminUserApiRequest, AdminUserApiResponse> {

    @Autowired
    private AdminUserApiLogicService adminUserApiLogicService;


    @Override
    @PostMapping("")
    public Header<AdminUserApiResponse> create(@RequestBody Header<AdminUserApiRequest> request) {
        log.info("{}",request);
        return adminUserApiLogicService.create(request);
    }

    @Override
    @GetMapping("/{id}")
    public Header<AdminUserApiResponse> read(@PathVariable(name = "id") Long id) {
        log.info("read id : {}",id);
        return adminUserApiLogicService.read(id);
    }

    @Override
    @PutMapping("")
    public Header<AdminUserApiResponse> update(@RequestBody Header<AdminUserApiRequest> request) {
        return adminUserApiLogicService.update(request);
    }

    @Override
    @DeleteMapping("{id}")
    public Header delete(@PathVariable Long id) {
        log.info("delete : {}",id);
        return adminUserApiLogicService.delete(id);
    }

    @GetMapping("")
    public Header<List<AdminUserApiResponse>> findAll(@PageableDefault(sort = { "id" }, direction = Sort.Direction.ASC)Pageable pageable){
        log.info("{}",pageable);
        return adminUserApiLogicService.search(pageable);
    }
}
