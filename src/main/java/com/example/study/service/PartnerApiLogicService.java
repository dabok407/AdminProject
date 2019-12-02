package com.example.study.service;

import com.example.study.ifs.CrudInterface;
import com.example.study.model.entity.Partner;
import com.example.study.model.network.Header;
import com.example.study.model.network.request.PartnerApiRequest;
import com.example.study.model.network.response.PartnerApiResponse;
import com.example.study.repository.PartnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PartnerApiLogicService extends BaseService<PartnerApiRequest, PartnerApiResponse, Partner> {

    /*@Autowired
    private PartnerRepository partnerRepository;*/

    @Override
    public Header<PartnerApiResponse> create(Header<PartnerApiRequest> request) {
        PartnerApiRequest body = request.getData();
        Partner orderGroup = Partner.builder()
                .status(body.getStatus())
                .name(body.getName())
                .address(body.getAddress())
                .businessNumber(body.getBusinessNumber())
                .callCenter(body.getCallCenter())
                .ceoName(body.getCeoName())
                .partnerNumber(body.getPartnerNumber())
                .build();

        /*OrderGroup newOrderGroup = orderGroupRepository.save(orderGroup);*/
        Partner newPartner = baseRepository.save(orderGroup);

        return response(newPartner);
    }

    @Override
    public Header<PartnerApiResponse> read(Long id) {

        return baseRepository.findById(id)
                .map(partner -> response(partner))
                .orElseGet(()->Header.ERROR("데이터 없음"));
    }

    @Override
    public Header<PartnerApiResponse> update(Header<PartnerApiRequest> request) {

        PartnerApiRequest body = request.getData();

        /*return orderGroupRepository.findById(body.getId())*/
        return baseRepository.findById(body.getId())
                .map(partner -> {
                    partner
                            .setStatus(body.getStatus())
                            .setName(body.getName())
                            .setAddress(body.getAddress())
                            .setBusinessNumber(body.getBusinessNumber())
                            .setCallCenter(body.getCallCenter())
                            .setCeoName(body.getCeoName())
                            .setPartnerNumber(body.getPartnerNumber());

                    return partner;
                })
                /*.map(changeOrderGroup -> orderGroupRepository.save(changeOrderGroup))*/
                .map(changePartner -> baseRepository.save(changePartner))
                .map(newPartner -> response(newPartner))
                .orElseGet(() -> Header.ERROR("데이터 없음"));
    }

    @Override
    public Header delete(Long id) {
        // 1. id -> repository -> user
        /*Optional<OrderGroup> optional = orderGroupRepository.findById(id);*/
        Optional<Partner> optional = baseRepository.findById(id);
        // 2. repository -> delete
        return optional.map(item -> {
            /*orderGroupRepository.delete(item);*/
            baseRepository.delete(item);
            return Header.OK();
        }).orElseGet(() -> Header.ERROR("데이터 없읍."));
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
