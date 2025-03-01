package id.overridestudio.tixfestapi.feature.transactionprocessing.service.impl;

import id.overridestudio.tixfestapi.constant.ErrorMessage;
import id.overridestudio.tixfestapi.feature.ticketmanagement.entity.Ticket;
import id.overridestudio.tixfestapi.feature.ticketmanagement.repository.TicketRepository;
import id.overridestudio.tixfestapi.feature.ticketmanagement.service.TicketService;
import id.overridestudio.tixfestapi.feature.transactionprocessing.dto.request.OrderRequest;
import id.overridestudio.tixfestapi.feature.transactionprocessing.dto.response.OrderDetailResponse;
import id.overridestudio.tixfestapi.feature.transactionprocessing.dto.response.OrderResponse;
import id.overridestudio.tixfestapi.feature.transactionprocessing.entity.Order;
import id.overridestudio.tixfestapi.feature.transactionprocessing.entity.OrderDetails;
import id.overridestudio.tixfestapi.feature.transactionprocessing.entity.OrderStatus;
import id.overridestudio.tixfestapi.feature.transactionprocessing.repository.OrderRepository;
import id.overridestudio.tixfestapi.feature.transactionprocessing.service.OrderService;
import id.overridestudio.tixfestapi.feature.usermanagement.entity.Customer;
import id.overridestudio.tixfestapi.feature.usermanagement.entity.UserAccount;
import id.overridestudio.tixfestapi.util.DateTimeUtil;
import id.overridestudio.tixfestapi.util.ValidationUtil;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ValidationUtil validationUtil;
    private final TicketService ticketService;
    private final TicketRepository ticketRepository;
    private final EntityManagerFactory entityManagerFactory;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public OrderResponse createOrder(OrderRequest request) {
        validationUtil.validate(request);
        UserAccount userAccount = (UserAccount) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Customer customer = userAccount.getCustomer();

        List<OrderDetails> orderDetailsList = request.getOrderDetails().stream().map(detail -> {
            Ticket ticket = ticketService.getById(detail.getTicketId());

            // validate minimum buy 1 ticket
            if (detail.getQuantity() <= 0)
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "quantity must be greater than 0");

            // validate quota/stock availability
            if (ticket.getQuota() < detail.getQuantity())
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "insufficient ticket stock");

            // update quota ticket
            ticket.setQuota(ticket.getQuota() - detail.getQuantity());
            ticketRepository.save(ticket);

            long subTotal = ticket.getPrice() * detail.getQuantity();

            return OrderDetails.builder()
                    .ticket(ticket)
                    .quantity(detail.getQuantity())
                    .price(ticket.getPrice())
                    .subtotal(subTotal)
                    .build();
        }).collect(Collectors.toList());

        // get total price
        long totalPrice = orderDetailsList.stream()
                .mapToLong(OrderDetails::getSubtotal)
                .sum();

        Order order = Order.builder()
                .customer(customer)
                .totalPrice(totalPrice)
                .orderDate(DateTimeUtil.getCurrentTimeFormatted("GMT+7"))
                .status(OrderStatus.PENDING)
                .orderDetails(orderDetailsList)
                .build();

        // set relation between order and order detail
        orderDetailsList.forEach(orderDetails -> orderDetails.setOrder(order));
        orderRepository.saveAndFlush(order);

        return toOrderResponse(order);
    }

    @Transactional(readOnly = true)
    @Override
    public Order getById(String id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessage.ERROR_ORDER_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    @Override
    public OrderResponse getOne(String id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessage.ERROR_ORDER_NOT_FOUND));
        return toOrderResponse(order);
    }

    @Transactional(readOnly = true)    @Override
    public List<OrderDetailResponse> getOrderDetails(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessage.ERROR_ORDER_NOT_FOUND));
        return order.getOrderDetails().stream()
                .map((this::toOrderDetailResponse))
                .toList();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public OrderResponse updateOrderStatus(String orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessage.ERROR_ORDER_NOT_FOUND));
        order.setStatus(status);
        Order saved = orderRepository.save(order);
        return toOrderResponse(saved);
    }

    private OrderResponse toOrderResponse(Order order) {
        List<OrderDetailResponse> orderDetailResponses = order.getOrderDetails().stream()
                .map(this::toOrderDetailResponse)
                .toList();

        return OrderResponse.builder()
                .id(order.getId())
                .customerId(order.getCustomer().getId())
                .customerName(order.getCustomer().getFirstName() + " " + order.getCustomer().getLastName())
                .orderDetails(orderDetailResponses)
                .orderDate(order.getOrderDate().toString())
                .totalPrice(order.getTotalPrice().toString())
                .status(order.getStatus().getDescription())
                .build();
    }

    private OrderDetailResponse toOrderDetailResponse(OrderDetails orderDetails) {
        return OrderDetailResponse.builder()
                .id(orderDetails.getId())
                .ticketId(orderDetails.getTicket().getId())
                .quantity(orderDetails.getQuantity().toString())
                .subtotal(orderDetails.getSubtotal().toString())
                .build();
    }
}
