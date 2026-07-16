package org.example.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.order.CreateOrderRequestDto;
import org.example.dto.order.OrderDto;
import org.example.dto.order.OrderItemDto;
import org.example.enums.OrderStatus;
import org.example.services.order.OrderService;
import org.example.services.order.item.OrderItemService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
@Tag(name = "Orders", description = "API for managing orders")
public class OrderController {
    private final OrderService orderService;
    private final OrderItemService orderItemService;

    @Operation(
            summary = "Create order",
            description = "Creates a new order based on user's shopping cart"
    )
    @PostMapping
    public OrderDto placeOrder(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody @Valid CreateOrderRequestDto dto) {

        return orderService.placeOrder(userDetails.getUsername(), dto);
    }

    @Operation(
            summary = "Get user orders",
            description = "Returns paginated list of user orders"
    )
    @GetMapping
    public Page<OrderDto> getOrders(
            @AuthenticationPrincipal UserDetails userDetails,
            Pageable pageable) {

        return orderService.getOrders(userDetails.getUsername(), pageable);
    }

    @Operation(summary = "Update order status")
    @PatchMapping("/{orderId}")
    public OrderDto updateStatus(
            @PathVariable Long orderId,
            @RequestParam OrderStatus status) {

        return orderService.updateStatus(orderId, status);
    }

    @Operation(
            summary = "Get order items",
            description = "Returns paginated list of items for specific order"
    )
    @GetMapping("/{orderId}/items")
    public Page<OrderItemDto> getOrderItems(
            @PathVariable Long orderId,
            Pageable pageable) {

        return orderItemService.getOrderItems(orderId, pageable);
    }

    @Operation(
            summary = "Get order item",
            description = "Returns specific item from order"
    )
    @GetMapping("/{orderId}/items/{itemId}")
    public OrderItemDto getOrderItem(
            @PathVariable Long orderId,
            @PathVariable Long itemId) {

        return orderItemService.getOrderItem(orderId, itemId);
    }
}
