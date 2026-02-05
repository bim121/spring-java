package org.example.services.cart;

import org.example.dto.cart.AddToCartRequestDto;
import org.example.dto.cart.CartItemDto;
import org.example.dto.cart.ShoppingCartDto;
import org.example.dto.cart.UpdateCartItemRequestDto;
import org.example.model.ShoppingCart;
import org.example.model.User;

public interface ShoppingCartService {
    ShoppingCartDto getCart(Long userId);

    CartItemDto addBookToCart(Long userId, AddToCartRequestDto dto);

    CartItemDto updateCartItem(Long cartItemId, UpdateCartItemRequestDto dto);

    void removeCartItem(Long cartItemId);

    ShoppingCart createCartForUser(User user);

    ShoppingCart getCartByUser(User user);
}
