package com.example.study.model.network.request;

import com.example.study.model.enumclass.AdminUserStatus;
import com.example.study.model.enumclass.UserStatus;
import lombok.*;
import org.springframework.boot.context.properties.bind.DefaultValue;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminUserApiRequest {

    private Long id;

    @NotBlank(message = "이름을 작성해주세요.")
    private String account;

    @NotBlank(message = "비밀번호를 작성해주세요.")
    private String password;

    private AdminUserStatus status;

    @NotBlank(message = "권한을 선택해주세요.")
    private String role;

    private LocalDateTime registeredAt;

    private LocalDateTime unregisteredAt;

    private LocalDateTime lastLoginAt;

    private LocalDateTime passwordUpdatedAt;

    /*private int loginFailCount;*/
}
