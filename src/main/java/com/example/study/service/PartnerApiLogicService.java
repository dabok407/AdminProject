package com.example.study.service;

import com.example.study.model.entity.Partner;
import com.example.study.model.network.Header;
import com.example.study.model.network.request.PartnerApiRequest;
import com.example.study.model.network.response.PartnerApiResponse;
import com.example.study.repository.CategoryRepository;
import com.example.study.repository.PartnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PartnerApiLogicService extends BaseService<PartnerApiRequest, PartnerApiResponse, Partner>{

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Header<PartnerApiResponse> create(Header<PartnerApiRequest> request) {

        return Optional.ofNullable(request.getData())
                .map(body ->{
                    Partner partner = Partner.builder()
                            .status(body.getStatus())
                            .name(body.getName())
                            .partnerNumber(body.getPartnerNumber())
                            .address(body.getAddress())
                            .businessNumber(body.getBusinessNumber())
                            .callCenter(body.getCallCenter())
                            .ceoName(body.getCeoName())
                            .registeredAt(LocalDateTime.now())
                            .category(categoryRepository.getOne(body.getCategoryId()))
                            .build();
                    return partner;
                })
                .map(newPartner -> baseRepository.save(newPartner))
                .map(newPartner -> response(newPartner))
                .orElseGet(()->Header.ERROR("데이터 없음"));
    }

    @Override
    public Header<PartnerApiResponse> read(Long id) {

        return baseRepository.findById(id)
                .map(partner -> response(partner))
                .orElseGet(()->Header.ERROR("데이터 없음"));
    }

    @Override
    public Header<PartnerApiResponse> update(Header<PartnerApiRequest> request) {
        PartnerApiRequest partnerApiRequest = request.getData();
        Optional<Partner> optional = baseRepository.findById(partnerApiRequest.getId());

        return optional.map(partner -> {
            partner.setStatus(partnerApiRequest.getStatus())
                    .setName(partnerApiRequest.getName())
                    .setCeoName(partnerApiRequest.getCeoName())
                    .setBusinessNumber(partnerApiRequest.getBusinessNumber())
                    .setPartnerNumber(partnerApiRequest.getPartnerNumber())
                    .setCallCenter(partnerApiRequest.getCallCenter())
                    .setAddress(partnerApiRequest.getAddress())
                    .setCategory(categoryRepository.getOne(partnerApiRequest.getCategoryId()))
                    .setRegisteredAt(partnerApiRequest.getRegisteredAt())
                    .setUnregisteredAt(partnerApiRequest.getUnregisteredAt());
            return partner;
        })
                .map(changePartner -> baseRepository.save(changePartner))
                .map(changePartner -> response(changePartner))
                .orElseGet(() -> Header.ERROR("데이터 없음"));
    }

    @Override
    public Header delete(Long id) {
        Optional<Partner> optional = baseRepository.findById(id);

        return optional.map(partner -> {
            baseRepository.delete(partner);
            return Header.OK();
        })
                .orElseGet(() -> Header.ERROR("데이터 없음"));
    }

    private Header<PartnerApiResponse> response(Partner partner){

        PartnerApiResponse body = PartnerApiResponse.builder()
                .id(partner.getId())
                .name(partner.getName())
                .status(partner.getStatus())
                .address(partner.getAddress())
                .callCenter(partner.getCallCenter())
                .partnerNumber(partner.getPartnerNumber())
                .businessNumber(partner.getBusinessNumber())
                .ceoName(partner.getCeoName())
                .registeredAt(partner.getRegisteredAt())
                .unregisteredAt(partner.getUnregisteredAt())
                .categoryId(partner.getCategory().getId())
                .build();

        return Header.OK(body);
    }
}
