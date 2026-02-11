package org.example.mappers;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;
import org.example.dto.order.CreateOrderRequestDto;
import org.example.dto.order.OrderDto;
import org.example.dto.order.OrderItemDto;
import org.example.enums.OrderStatus;
import org.example.model.Order;
import org.example.model.OrderItem;
import org.example.model.ShoppingCart;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderDto toDto(Order order);

    OrderItemDto toDto(OrderItem item);

    default Order toModel(ShoppingCart cart, CreateOrderRequestDto dto) {
        Order order = new Order();
        order.setUser(cart.getUser());
        order.setStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDateTime.now());
        order.setShippingAddress(dto.getShippingAddress());

        Set<OrderItem> items = cart.getCartItems().stream()
                .map(cartItem -> {
                    OrderItem item = new OrderItem();
                    item.setOrder(order);
                    item.setBook(cartItem.getBook());
                    item.setQuantity(cartItem.getQuantity());
                    item.setPrice(cartItem.getBook().getPrice());
                    return item;
                })
                .collect(Collectors.toSet());

        order.setOrderItems(items);

        return order;
    }
}
