package com.example.study.service;

import com.example.study.ifs.CrudInterface;
import com.example.study.model.entity.AdminUser;
import com.example.study.model.entity.OrderGroup;
import com.example.study.model.enumclass.AdminUserStatus;
import com.example.study.model.enumclass.UserStatus;
import com.example.study.model.network.Header;
import com.example.study.model.network.Pagination;
import com.example.study.model.network.request.AdminUserApiRequest;
import com.example.study.model.network.request.UserApiRequest;
import com.example.study.model.network.response.*;
import com.example.study.repository.AdminUserRepository;
import com.example.study.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
/*import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;*/
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AdminUserApiLogicService implements CrudInterface<AdminUserApiRequest, AdminUserApiResponse> {

    @Autowired
    private AdminUserRepository adminUserRepository;

    // 1. request data
    // 2. user 생성
    // 3. 생성된 데이터 -> UserApiResponse return
    @Override
    public Header<AdminUserApiResponse> create(Header<AdminUserApiRequest> request) {

        // 1. request data
        AdminUserApiRequest adminUserApiRequest = request.getData();

        // 2. User 생성
        AdminUser adminUser = AdminUser.builder()
                .account(adminUserApiRequest.getAccount())
                .password(adminUserApiRequest.getPassword())
                .status(AdminUserStatus.REGISTERED)
                .role(adminUserApiRequest.getRole())
                .loginFailCount(0)
                .registeredAt(LocalDateTime.now())
                .build();
        AdminUser newAdminUser = adminUserRepository.save(adminUser);

        // 3. 생성된 데이터 -> userApiResponse return
        return Header.OK(response(newAdminUser));
    }

    @Override
    public Header<AdminUserApiResponse> read(Long id) {

        return adminUserRepository.findById(id)
            .map(adminUser -> response(adminUser))
            .map(Header::OK)
            .orElseGet(
                    ()->Header.ERROR("데이터 없음")
            );
    }

    @Override
    public Header<AdminUserApiResponse> update(Header<AdminUserApiRequest> request) {
        // 1. data
        AdminUserApiRequest adminUserApiRequest = request.getData();

        // 2. id -> user 데이터 를 찾고
        Optional<AdminUser> optional = adminUserRepository.findById(adminUserApiRequest.getId());

        return optional.map(adminUser -> {
            // 3. data -> update
            // id
            adminUser.setAccount(adminUserApiRequest.getAccount())
                    .setPassword(adminUserApiRequest.getPassword())
                    .setStatus(adminUserApiRequest.getStatus())
                    .setRole(adminUserApiRequest.getRole())
                    .setRegisteredAt(adminUserApiRequest.getRegisteredAt())
                    .setUnregisteredAt(adminUserApiRequest.getUnregisteredAt())
                    ;
            return adminUser;

        })
        .map(adminUser -> adminUserRepository.save(adminUser))             // update -> newUser
        .map(adminUser -> response(adminUser))                        // userApiResponse
        .map(Header::OK)
        .orElseGet(()->Header.ERROR("데이터 없음"));
    }

    @Override
    public Header delete(Long id) {
        // 1. id -> repository -> user
        Optional<AdminUser> optional = adminUserRepository.findById(id);

        // 2. repository -> delete
        return optional.map(adminUser ->{
            adminUserRepository.delete(adminUser);
            return Header.OK();
        })
        .orElseGet(()->Header.ERROR("데이터 없음"));
    }

    public Header<List<AdminUserApiResponse>> search(Pageable pageable) {
        Page<AdminUser> adminUsers = adminUserRepository.findAll(pageable);
        List<AdminUserApiResponse> adminUserApiResponseList = adminUsers.stream()
                .map(adminUser -> response(adminUser))
                .collect(Collectors.toList());

        Pagination pagination = Pagination.builder()
                .totalPages(adminUsers.getTotalPages())
                .totalElements(adminUsers.getTotalElements())
                .currentPage(adminUsers.getNumber())
                .currentElements(adminUsers.getNumberOfElements())
                .build();

        return Header.OK(adminUserApiResponseList,pagination);
    }

    private AdminUserApiResponse response(AdminUser adminUser){
        // user -> userApiResponse

        AdminUserApiResponse adminUserApiResponse = AdminUserApiResponse.builder()
                .id(adminUser.getId())
                .account(adminUser.getAccount())
                .password(adminUser.getPassword()) // todo 암호화, 길이
                .status(adminUser.getStatus())
                .role(adminUser.getRole())
                .registeredAt(adminUser.getRegisteredAt())
                .unregisteredAt(adminUser.getUnregisteredAt())
                .build();

        return adminUserApiResponse;
    }


    /*public UserDetails loadUserByUsername(String accrount, String password) throws UsernameNotFoundException {
        Optional<AdminUser> adminUserEntityWrapper = Optional.ofNullable(adminUserRepository.findFirstByAccountAndPassword(accrount, password));
        AdminUser AdminUserEntity = adminUserEntityWrapper.get();

        List<GrantedAuthority> authorities = new ArrayList<>();

        if (("ADMIN").equals(AdminUserEntity.getRole())) {
            authorities.add(new SimpleGrantedAuthority("ADMIN"));
        } else {
            authorities.add(new SimpleGrantedAuthority("MEMBER"));
        }

        return new User(AdminUserEntity.getAccount(), AdminUserEntity.getPassword(), authorities);
    }*/

}
