package com.ssafy.cadang.controller;


import com.ssafy.cadang.dto.order.CustomerOrderDto;
import com.ssafy.cadang.dto.order.OrderSaveDto;
import com.ssafy.cadang.dto.order.OrderUpdateDto;
import com.ssafy.cadang.dto.order.StoreOrderDto;
import com.ssafy.cadang.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "주문", description = "주문 관련 api 입니다.")
@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final Logger logger = LoggerFactory.getLogger(OrderController.class);

    private final OrderService orderService;

    @PostMapping
    @Operation(summary = "주문 등록", description = "신규 주문을 등록합니다.")
    public ResponseEntity<Long> saveOrder(HttpServletRequest request, @RequestBody OrderSaveDto orderSaveDto){

        logger.info("saveOrder - 호출 {} ", orderSaveDto);

        Long userId = Long.valueOf(request.getAttribute("userId").toString());
        orderSaveDto.setUserId(userId);

        Long orderId = orderService.saveOrder(orderSaveDto);

        return new ResponseEntity<Long>(orderId, HttpStatus.ACCEPTED);
    }

    @GetMapping
    @Operation(summary = "주문 현황 조회(고객)", description = "현재 진행 중인 주문을 조회합니다.")
    public ResponseEntity<List<CustomerOrderDto>> getCustomerNowOrderList(HttpServletRequest request){

        Long userId = Long.valueOf(request.getAttribute("userId").toString());
        List<CustomerOrderDto> CustomerOrderDtoList = orderService.getCustomerNowOrderById(userId);

        return new ResponseEntity<List<CustomerOrderDto>>(CustomerOrderDtoList, HttpStatus.ACCEPTED);
    }

    @GetMapping("/list")
    @Operation(summary = "주문 내역 조회(고객)", description = "고객의 전체 주문 내역을 조회합니다.")
    public ResponseEntity<List<CustomerOrderDto>> getCustomerOrderList(HttpServletRequest request){

        Long userId = Long.valueOf(request.getAttribute("userId").toString());
        List<CustomerOrderDto> CustomerOrderDtoList = orderService.getCustomerOrderById(userId);

        return new ResponseEntity<List<CustomerOrderDto>>(CustomerOrderDtoList, HttpStatus.ACCEPTED);
    }

    @GetMapping("/store-list/{storeId}")
    @Operation(summary = "주문 내역 조회(가게)", description = "가게의 진행 중인 주문 내역을 조회합니다.")
    public ResponseEntity<List<StoreOrderDto>> getStoreOrderList(@PathVariable Long storeId){

        List<StoreOrderDto> StoreOrderDtoList = orderService.getStoreOrderById(storeId);

        return new ResponseEntity<List<StoreOrderDto>>(StoreOrderDtoList, HttpStatus.ACCEPTED);
    }

    @GetMapping("/store-new/{storeId}")
    @Operation(summary = "신규 주문 조회(가게)", description = "가게의 주문 내역 중 상태가 REQUEST인 신규 주문을 조회합니다.")
    public ResponseEntity<List<StoreOrderDto>> getStoreNewOrderList(@PathVariable Long storeId){

        List<StoreOrderDto> StoreNewOrderDtoList = orderService.getStoreNewOrderById(storeId);

        return new ResponseEntity<List<StoreOrderDto>>(StoreNewOrderDtoList, HttpStatus.ACCEPTED);
    }

    @PutMapping
    @Operation(summary = "주문 상태 수정", description = "주문 상태를 수락/거절/제조완료/픽업완료로 수정합니다")
    public ResponseEntity<Map<String, Long>> updateOrder(@RequestBody OrderUpdateDto orderUpdateDto) {

        logger.info("updateOrder - 호출 {} ", orderUpdateDto);

        Long orderId = orderService.updateOrderByOrderIdAndOrderStatus(orderUpdateDto);
        Long customerId = orderUpdateDto.getCustomerId();
        Map<String, Long> orderAndCustomerId = new HashMap<>();
        orderAndCustomerId.put("orderId", orderId);
        orderAndCustomerId.put("customerId", customerId);

        return new ResponseEntity<Map<String, Long>>(orderAndCustomerId, HttpStatus.ACCEPTED);
    }
}
