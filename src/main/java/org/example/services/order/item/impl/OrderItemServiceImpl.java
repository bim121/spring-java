package org.example.services.order.item.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.dto.order.OrderItemDto;
import org.example.exceptions.EntityNotFoundException;
import org.example.mappers.OrderMapper;
import org.example.model.OrderItem;
import org.example.repositories.OrderItemRepository;
import org.example.services.order.item.OrderItemService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final OrderMapper orderMapper;

    @Override
    public Page<OrderItemDto> getOrderItems(Long orderId, Pageable pageable) {
        Page<OrderItem> items = orderItemRepository
                .findAllByOrderId(orderId, pageable);
        if (items.isEmpty()) {
            throw new EntityNotFoundException(
                    "Order items not found for orderId: " + orderId);
        }
        return items.map(orderMapper::toDto);
    }

    @Override
    public OrderItemDto getOrderItem(Long orderId, Long itemId) {
        OrderItem item = orderItemRepository
                .findByIdAndOrderId(itemId, orderId)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                String.format(
                                        "OrderItem not found. orderId=%s, itemId=%s",
                                        orderId,
                                        itemId
                                )
                        )
                );
        return orderMapper.toDto(item);
    }
}
