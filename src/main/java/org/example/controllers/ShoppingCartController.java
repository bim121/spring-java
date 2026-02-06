package org.example.controllers;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.example.dto.cart.AddToCartRequestDto;
import org.example.dto.cart.CartItemDto;
import org.example.dto.cart.ShoppingCartDto;
import org.example.dto.cart.UpdateCartItemRequestDto;
import org.example.services.cart.ShoppingCartService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class ShoppingCartController {

    private final ShoppingCartService cartService;

    @Operation(summary = "Get current user's shopping cart")
    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ShoppingCartDto getCart(Principal principal) {
        return cartService.getCartByUserEmail(principal.getName());
    }

    @Operation(summary = "Add book to shopping cart")
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public CartItemDto addBookToCart(@RequestBody @Valid AddToCartRequestDto dto,
                                     Principal principal) {
        return cartService.addBookToCartByUserEmail(principal.getName(), dto);
    }

    @Operation(summary = "Update quantity of book in cart")
    @PutMapping("/items/{cartItemId}")
    @PreAuthorize("hasRole('USER')")
    public CartItemDto updateCartItem(@PathVariable Long cartItemId,
                                      @RequestBody @Valid UpdateCartItemRequestDto dto) {
        return cartService.updateCartItem(cartItemId, dto);
    }

    @Operation(summary = "Remove book from shopping cart")
    @DeleteMapping("/items/{cartItemId}")
    @PreAuthorize("hasRole('USER')")
    public void removeCartItem(@PathVariable Long cartItemId) {
        cartService.removeCartItem(cartItemId);
    }
}
