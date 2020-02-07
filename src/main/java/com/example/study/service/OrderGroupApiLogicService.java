package com.example.study.service;

import com.example.study.common.CommonFunction;
import com.example.study.model.entity.OrderDetail;
import com.example.study.model.entity.OrderGroup;
import com.example.study.model.network.Header;
import com.example.study.model.network.Pagination;
import com.example.study.model.network.request.OrderDetailApiRequest;
import com.example.study.model.network.request.OrderGroupApiRequest;
import com.example.study.model.network.response.ItemApiResponse;
import com.example.study.model.network.response.OrderDetailApiResponse;
import com.example.study.model.network.response.OrderGroupApiResponse;
import com.example.study.repository.ItemRepository;
import com.example.study.repository.OrderDetailRepository;
import com.example.study.repository.OrderGroupRepository;
import com.example.study.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderGroupApiLogicService extends BaseService<OrderGroupApiRequest, OrderGroupApiResponse,OrderGroup> {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderGroupRepository orderGroupRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private ItemRepository itemRepository;


    @Override
    public Header<OrderGroupApiResponse> create(Header<OrderGroupApiRequest> request) {

        return Optional.ofNullable(request.getData())
                .map(body ->{
                    OrderGroup orderGroup = OrderGroup.builder()
                            .status(body.getStatus())
                            .orderType(body.getOrderType())
                            .revAddress(body.getRevAddress())
                            .revName(body.getRevName())
                            .paymentType(body.getPaymentType())
                            .totalPrice(body.getTotalPrice())
                            .totalQuantity(body.getTotalQuantity())
                            .orderAt(LocalDateTime.now())
                            .user(userRepository.getOne(body.getUserId()))
                            .build();

                    return orderGroup;
                })
                .map(newOrderGroup -> {
                    orderGroupRepository.save(newOrderGroup);
                    for(OrderDetailApiRequest orderDetailList : request.getData().getOrderDetailApiRequestList()){
                        OrderDetail orderDetail = OrderDetail.builder()
                                .status(orderDetailList.getStatus())
                                .quantity(orderDetailList.getQuantity())
                                .totalPrice(orderDetailList.getTotalPrice())
                                .item(itemRepository.getOne(orderDetailList.getItemId()))
                                .arrivalDate(LocalDateTime.now())
                                .orderGroup(newOrderGroup)
                                .build();
                        orderDetailRepository.save(orderDetail);
                    }
                    return newOrderGroup;
                })
                //.map(newOrderGroup -> orderGroupRepository.save(newOrderGroup))
                .map(newOrderGroup -> response(newOrderGroup))
                .map(Header::OK)
                .orElseGet(()->Header.ERROR("데이터 없음"));
    }

    @Override
    public Header<OrderGroupApiResponse> read(Long id) {
        return orderGroupRepository.findById(id)
                .map(this::response)
                .map(Header::OK)
                .orElseGet(()->Header.ERROR("데이터 없음"));
    }

    @Override
    public Header<OrderGroupApiResponse> update(Header<OrderGroupApiRequest> request) {

        return Optional.ofNullable(request.getData())
                .map(body ->{
                    return orderGroupRepository.findById(body.getId())
                        .map(orderGroup -> {
                            orderGroup
                                    .setStatus(body.getStatus())
                                    .setOrderType(body.getOrderType())
                                    .setRevAddress(body.getRevAddress())
                                    .setRevName(body.getRevName())
                                    .setPaymentType(body.getPaymentType())
                                    .setTotalPrice(body.getTotalPrice())
                                    .setTotalQuantity(body.getTotalQuantity())
                                    .setOrderAt(body.getOrderAt())
                                    .setArrivalDate(body.getArrivalDate())
                                    .setUser(userRepository.getOne(body.getUserId()))
                            ;
                            return orderGroup;
                        })
                        .map(changeOrderGroup -> orderGroupRepository.save(changeOrderGroup))
                        .map(this::response)
                        .map(Header::OK)
                        .orElseGet(()->Header.ERROR("데이터 없음"));
                })
                .orElseGet(()->Header.ERROR("데이터 없음"));
    }

    @Override
    public Header delete(Long id) {

        return orderGroupRepository.findById(id)
                .map(orderGroup -> {
                    orderGroupRepository.delete(orderGroup);
                    return Header.OK();
                })
                .orElseGet(()->Header.ERROR("데이터 없음"));
    }

    public Header<List<OrderGroupApiResponse>> search(Pageable pageable, OrderGroupApiRequest orderGroupApiRequest, String initialYn){
        Page<OrderGroup> orderGroups = orderGroupRepository.findAll(pageable);

        List<OrderGroupApiResponse> orderGroupApiResponseList = orderGroups.stream()
                .map(orderGroup -> {
                    OrderGroupApiResponse orderGroupApiResponse = this.response(orderGroup);
                    /*response(orderGroup)*/
                    List<OrderDetailApiResponse> orderDetails = orderGroup.getOrderDetailList().stream()
                            .map(detail -> this.response(detail))
                            .collect(Collectors.toList());

                    orderGroupApiResponse.setOrderDetailList(orderDetails);
                    return orderGroupApiResponse;
                })
                .collect(Collectors.toList());

        Pagination pagination = Pagination.builder()
                .totalPages(orderGroups.getTotalPages())
                .totalElements(orderGroups.getTotalElements())
                .currentPage(orderGroups.getNumber())
                .currentElements(orderGroups.getNumberOfElements())
                .build();

        return Header.OK(orderGroupApiResponseList, pagination);
    }

    public OrderGroupApiResponse response(OrderGroup orderGroup){

        OrderGroupApiResponse orderGroupApiResponse = OrderGroupApiResponse.builder()
                .id(orderGroup.getId())
                .status(orderGroup.getStatus())
                .orderType(orderGroup.getOrderType())
                .revAddress(orderGroup.getRevAddress())
                .revName(orderGroup.getRevName())
                .paymentType(orderGroup.getPaymentType())
                .totalPrice(CommonFunction.convertCommaMoney(String.valueOf(orderGroup.getTotalPrice())))
                .totalQuantity(orderGroup.getTotalQuantity())
                .orderAt(orderGroup.getOrderAt())
                .arrivalDate(orderGroup.getArrivalDate())
                .userId(orderGroup.getUser().getId())
                .userAccount(orderGroup.getUser().getAccount())
                .build();

        return orderGroupApiResponse;
    }

    public OrderDetailApiResponse response(OrderDetail orderDetail){

        OrderDetailApiResponse orderDetailApiResponse = OrderDetailApiResponse.builder()
                .id(orderDetail.getId())
                .status(orderDetail.getStatus())
                .totalPrice(orderDetail.getTotalPrice())
                .quantity(orderDetail.getQuantity())
                .arrivalDate(orderDetail.getArrivalDate())
                .build();

        return orderDetailApiResponse;
    }

}
