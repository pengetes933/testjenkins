package id.overridestudio.tixfestapi.feature.transactionprocessing.service.impl;

import id.overridestudio.tixfestapi.feature.ticketmanagement.entity.Ticket;
import id.overridestudio.tixfestapi.feature.ticketmanagement.repository.TicketRepository;
import id.overridestudio.tixfestapi.feature.ticketmanagement.service.TicketService;
import id.overridestudio.tixfestapi.feature.transactionprocessing.dto.request.OrderDetailRequest;
import id.overridestudio.tixfestapi.feature.transactionprocessing.dto.request.OrderRequest;
import id.overridestudio.tixfestapi.feature.transactionprocessing.dto.response.OrderResponse;
import id.overridestudio.tixfestapi.feature.transactionprocessing.entity.Order;
import id.overridestudio.tixfestapi.feature.transactionprocessing.repository.OrderRepository;
import id.overridestudio.tixfestapi.feature.usermanagement.entity.Customer;
import id.overridestudio.tixfestapi.feature.usermanagement.entity.UserAccount;
import id.overridestudio.tixfestapi.util.ValidationUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ValidationUtil validationUtil;

    @Mock
    private TicketService ticketService;

    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private OrderServiceImpl orderService;
    private Customer customer;
    private UserAccount userAccount;
    private Ticket ticket;

    private OrderRequest orderRequest;

    @BeforeEach
    void setUp() {
        userAccount = new UserAccount();
        customer = new Customer();
        userAccount.setCustomer(customer);

        ticket = new Ticket();
        ticket.setId("1L");
        ticket.setPrice(10000L);
        ticket.setQuota(10);

        OrderDetailRequest orderDetailRequest = new OrderDetailRequest();
        orderDetailRequest.setTicketId("1L");
        orderDetailRequest.setQuantity(2);

        orderRequest = new OrderRequest();
        orderRequest.setOrderDetails(Collections.singletonList(orderDetailRequest));

        // Mock SecurityContext
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userAccount);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void createOrder_WhenValidRequest_ReturnsOrderResponse() {
        // Arrange
        when(ticketService.getById("1L")).thenReturn(ticket);
        when(ticketRepository.save(ticket)).thenReturn(ticket);
        when(orderRepository.saveAndFlush(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        OrderResponse response = orderService.createOrder(orderRequest);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getTotalPrice()).isEqualTo("20000"); // 2 tickets x 10000
        verify(validationUtil, times(1)).validate(orderRequest);
        verify(ticketService, times(1)).getById("1L");
        verify(ticketRepository, times(1)).save(ticket);
        verify(orderRepository, times(1)).saveAndFlush(any(Order.class));
    }
}