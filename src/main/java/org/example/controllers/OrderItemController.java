package org.example.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.dto.order.OrderItemDto;
import org.example.services.order.item.OrderItemService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders/{orderId}/items")
@Tag(name = "Order Items", description = "API for managing order items")
public class OrderItemController {

    private final OrderItemService orderItemService;

    @Operation(summary = "Get all items of an order",
            description = "Returns a list of all items for a given order ID")
    @GetMapping
    public List<OrderItemDto> getItems(
            @PathVariable @Parameter(description = "ID of the order") Long orderId) {
        return orderItemService.getOrderItems(orderId);
    }

    @Operation(summary = "Get a specific order item",
            description = "Returns detailed information about a specific order item by "
                    + "order ID and item ID")
    @GetMapping("/{itemId}")
    public OrderItemDto getItem(
            @PathVariable @Parameter(description = "ID of the order") Long orderId,
            @PathVariable @Parameter(description = "ID of the order item") Long itemId) {
        return orderItemService.getOrderItem(orderId, itemId);
    }
}
