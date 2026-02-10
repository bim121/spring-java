package org.example.services.order.impl;

import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.example.dto.order.CreateOrderRequestDto;
import org.example.dto.order.OrderDto;
import org.example.enums.OrderStatus;
import org.example.exceptions.EntityNotFoundException;
import org.example.mappers.OrderMapper;
import org.example.model.Order;
import org.example.model.OrderItem;
import org.example.model.ShoppingCart;
import org.example.repositories.OrderRepository;
import org.example.services.cart.ShoppingCartService;
import org.example.services.order.OrderService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final ShoppingCartService shoppingCartService;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Override
    public OrderDto placeOrder(String email, CreateOrderRequestDto dto) {
        ShoppingCart cart = shoppingCartService.getCartEntityByUserEmail(email);
        if (cart.getCartItems().isEmpty()) {
            throw new IllegalStateException("Shopping cart is empty");
        }
        Order order = new Order();
        order.setUser(cart.getUser());
        order.setStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDateTime.now());
        order.setShippingAddress(dto.getShippingAddress());
        Set<OrderItem> orderItems = cart.getCartItems().stream()
                .map(cartItem -> {
                    OrderItem item = new OrderItem();
                    item.setOrder(order);
                    item.setBook(cartItem.getBook());
                    item.setQuantity(cartItem.getQuantity());
                    item.setPrice(cartItem.getBook().getPrice());
                    return item;
                })
                .collect(Collectors.toSet());
        order.setOrderItems(orderItems);
        BigDecimal total = orderItems.stream()
                .map(i -> i.getPrice()
                        .multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotal(total);
        Order savedOrder = orderRepository.save(order);
        shoppingCartService.clearCart(cart);
        return orderMapper.toDto(savedOrder);
    }

    @Override
    public List<OrderDto> getOrders(String userEmail) {
        return orderRepository.findAllByUserEmail(userEmail).stream()
                .map(orderMapper::toDto)
                .toList();
    }

    @Override
    public OrderDto updateStatus(Long orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Order not found: " + orderId));
        order.setStatus(status);
        return orderMapper.toDto(order);
    }
}
