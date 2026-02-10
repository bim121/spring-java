package org.example.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.dto.order.CreateOrderRequestDto;
import org.example.dto.order.OrderDto;
import org.example.enums.OrderStatus;
import org.example.services.order.OrderService;
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

    @Operation(summary = "Place a new order",
            description = "Creates a new order based on the user's shopping cart")
    @PostMapping
    public OrderDto placeOrder(
            Principal principal,
            @RequestBody @Parameter(
                    description = "Order creation request data") CreateOrderRequestDto dto) {

        return orderService.placeOrder(principal.getName(), dto);
    }

    @Operation(summary = "Get all orders",
            description = "Returns a list of all orders for the authenticated user")
    @GetMapping
    public List<OrderDto> getOrders(
            Principal principal) {
        return orderService.getOrders(principal.getName());
    }

    @Operation(summary = "Update order status",
            description = "Updates the status of an order by ID")
    @PatchMapping("/{orderId}")
    public OrderDto updateStatus(
            @PathVariable @Parameter(description = "ID of the order to update") Long orderId,
            @RequestParam @Parameter(description = "New status of the order") OrderStatus status) {

        return orderService.updateStatus(orderId, status);
    }
}
