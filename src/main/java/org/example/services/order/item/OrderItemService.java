package org.example.services.order.item;

import org.example.dto.order.OrderItemDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderItemService {

    Page<OrderItemDto> getOrderItems(Long orderId, Pageable pageable);

    OrderItemDto getOrderItem(Long orderId, Long itemId);
}
