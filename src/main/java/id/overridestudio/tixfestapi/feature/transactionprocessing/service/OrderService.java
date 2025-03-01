package id.overridestudio.tixfestapi.feature.transactionprocessing.service;

import id.overridestudio.tixfestapi.feature.transactionprocessing.dto.request.OrderRequest;
import id.overridestudio.tixfestapi.feature.transactionprocessing.dto.response.OrderDetailResponse;
import id.overridestudio.tixfestapi.feature.transactionprocessing.dto.response.OrderResponse;
import id.overridestudio.tixfestapi.feature.transactionprocessing.entity.Order;
import id.overridestudio.tixfestapi.feature.transactionprocessing.entity.OrderStatus;

import java.util.List;

public interface OrderService {

    OrderResponse createOrder(OrderRequest request);

    Order getById(String id);
    OrderResponse getOne(String id);
    List<OrderDetailResponse> getOrderDetails(String orderId);
    OrderResponse updateOrderStatus(String orderId, OrderStatus status);
}
