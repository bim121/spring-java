package org.example.services.order.item;

import java.util.List;
import org.example.dto.order.OrderItemDto;

public interface OrderItemService {

    List<OrderItemDto> getOrderItems(Long orderId);

    OrderItemDto getOrderItem(Long orderId, Long itemId);
}
