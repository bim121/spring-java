package org.example.services.order;

import org.example.dto.order.CreateOrderRequestDto;
import org.example.dto.order.OrderDto;
import org.example.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    OrderDto placeOrder(String userEmail, CreateOrderRequestDto dto);

    Page<OrderDto> getOrders(String userEmail, Pageable pageable);

    OrderDto updateStatus(Long orderId, OrderStatus status);
}
