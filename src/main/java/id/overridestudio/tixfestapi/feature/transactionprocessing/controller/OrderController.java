package id.overridestudio.tixfestapi.feature.transactionprocessing.controller;

import id.overridestudio.tixfestapi.constant.SuccessMessage;
import id.overridestudio.tixfestapi.core.dto.response.WebResponse;
import id.overridestudio.tixfestapi.feature.transactionprocessing.dto.request.OrderRequest;
import id.overridestudio.tixfestapi.feature.transactionprocessing.dto.response.OrderDetailResponse;
import id.overridestudio.tixfestapi.feature.transactionprocessing.dto.response.OrderResponse;
import id.overridestudio.tixfestapi.feature.transactionprocessing.service.OrderService;
import id.overridestudio.tixfestapi.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<WebResponse<OrderResponse>> createOrder(@RequestBody OrderRequest request) {
        OrderResponse response = orderService.createOrder(request);
        return ResponseUtil.buildResponse(HttpStatus.CREATED, SuccessMessage.SUCCESS_CREATE_ORDER,response);
    }

    @GetMapping(path = "{orderId}")
    public ResponseEntity<WebResponse<OrderResponse>> getOrderById(@PathVariable String orderId) {
        OrderResponse response = orderService.getOne(orderId);
        return ResponseUtil.buildResponse(HttpStatus.OK, SuccessMessage.SUCCESS_GET_ORDER, response);
    }

    @GetMapping(path = "{orderId}/details")
    public ResponseEntity<WebResponse<List<OrderDetailResponse>>> getOrderDetails(@PathVariable String orderId) {
        List<OrderDetailResponse> orderDetails = orderService.getOrderDetails(orderId);
        return ResponseUtil.buildResponse(HttpStatus.OK, SuccessMessage.SUCCESS_GET_ORDER_DETAILS, orderDetails);
    }


}
