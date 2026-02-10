package org.example.services.order;

import java.util.List;
import org.example.dto.order.CreateOrderRequestDto;
import org.example.dto.order.OrderDto;
import org.example.enums.OrderStatus;

public interface OrderService {
    OrderDto placeOrder(String userEmail, CreateOrderRequestDto dto);

    List<OrderDto> getOrders(String userEmail);

    OrderDto updateStatus(Long orderId, OrderStatus status);
}
