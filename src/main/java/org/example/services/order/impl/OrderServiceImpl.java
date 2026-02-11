package org.example.services.order.impl;

import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Set;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        validateCart(cart);
        Order order = createOrder(cart, dto);
        orderRepository.save(order);
        shoppingCartService.clearCart(cart);
        return orderMapper.toDto(order);
    }

    @Override
    public Page<OrderDto> getOrders(String userEmail, Pageable pageable) {
        return orderRepository.findAllByUserEmail(userEmail, pageable)
                .map(orderMapper::toDto);
    }

    @Override
    public OrderDto updateStatus(Long orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Order not found: " + orderId));
        order.setStatus(status);
        return orderMapper.toDto(order);
    }

    private BigDecimal calculateTotal(Set<OrderItem> orderItems) {
        return orderItems.stream()
                .map(item -> item.getPrice()
                        .multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private void validateCart(ShoppingCart cart) {
        if (cart.getCartItems().isEmpty()) {
            throw new IllegalStateException("Shopping cart is empty");
        }
    }

    private Order createOrder(ShoppingCart cart, CreateOrderRequestDto dto) {
        Order order = orderMapper.toModel(cart, dto);
        order.setTotal(calculateTotal(order.getOrderItems()));
        return order;
    }
}
