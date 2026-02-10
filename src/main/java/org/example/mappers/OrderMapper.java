package org.example.mappers;

import org.example.dto.order.OrderDto;
import org.example.dto.order.OrderItemDto;
import org.example.model.Order;
import org.example.model.OrderItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderDto toDto(Order order);

    OrderItemDto toDto(OrderItem item);
}
