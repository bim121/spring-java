package org.example.services.order.item.impl;

import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.dto.order.OrderItemDto;
import org.example.exceptions.EntityNotFoundException;
import org.example.mappers.OrderMapper;
import org.example.model.OrderItem;
import org.example.repositories.OrderItemRepository;
import org.example.services.order.item.OrderItemService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final OrderMapper orderMapper;

    @Override
    public List<OrderItemDto> getOrderItems(Long orderId) {
        List<OrderItem> items =
                orderItemRepository.findAllByOrderId(orderId);
        if (items.isEmpty()) {
            throw new EntityNotFoundException(
                    "Order items not found for orderId: " + orderId);
        }
        return items.stream()
                .map(orderMapper::toDto)
                .toList();
    }

    @Override
    public OrderItemDto getOrderItem(Long orderId, Long itemId) {
        OrderItem item = orderItemRepository
                .findByIdAndOrderId(itemId, orderId)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "OrderItem not found. orderId="
                                        + orderId
                                        + ", itemId="
                                        + itemId));

        return orderMapper.toDto(item);
    }
}
