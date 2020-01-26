package com.example.study.model.network.response;

import com.example.study.model.entity.Partner;
import com.example.study.model.enumclass.ItemStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemApiResponse {

    private Long id;

    private ItemStatus status;

    private String name;

    private String title;

    private String content;

    /*private BigDecimal price;*/

    private String price;

    private String brandName;

    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd (HH:mm:ss)", timezone="Asia/Seoul")
    private LocalDateTime registeredAt;

    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd (HH:mm:ss)", timezone="Asia/Seoul")
    private LocalDateTime unregisteredAt;

    private Long partnerId;

    private PartnerApiResponse partner;

}
