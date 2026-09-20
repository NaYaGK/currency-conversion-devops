package com.ecommerce.order.service;

import com.ecommerce.order.model.Order;
import com.ecommerce.order.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateOrderWithDiscount() {
        Order inputOrder = new Order(null, "Laptop", 2, 60.0, null); // Total 120
        
        Order savedOrder = new Order(1L, "Laptop", 2, 54.0, "PENDING"); // 10% discount -> 54
        
        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);

        Order result = orderService.createOrder(inputOrder);

        assertNotNull(result);
        assertEquals(54.0, result.getPrice());
        assertEquals("PENDING", result.getStatus());
    }

    @Test
    void testCreateOrderNoDiscount() {
        Order inputOrder = new Order(null, "Mouse", 1, 50.0, null); // Total 50
        
        Order savedOrder = new Order(1L, "Mouse", 1, 50.0, "PENDING");
        
        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);

        Order result = orderService.createOrder(inputOrder);

        assertNotNull(result);
        assertEquals(50.0, result.getPrice());
        assertEquals("PENDING", result.getStatus());
    }

    @Test
    void testCreateOrderInvalidQuantity() {
        Order inputOrder = new Order(null, "Mouse", 0, 50.0, null);
        
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            orderService.createOrder(inputOrder);
        });
        
        assertEquals("Quantity must be greater than zero", exception.getMessage());
    }
}
