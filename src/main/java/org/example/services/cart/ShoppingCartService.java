package org.example.services.cart;

import org.example.dto.cart.AddToCartRequestDto;
import org.example.dto.cart.CartItemDto;
import org.example.dto.cart.ShoppingCartDto;
import org.example.dto.cart.UpdateCartItemRequestDto;
import org.example.model.ShoppingCart;
import org.example.model.User;

public interface ShoppingCartService {
    CartItemDto updateCartItem(Long cartItemId, UpdateCartItemRequestDto dto);

    void removeCartItem(Long cartItemId);

    ShoppingCart createCartForUser(User user);

    ShoppingCartDto getCartByUserEmail(String email);

    CartItemDto addBookToCartByUserEmail(String email, AddToCartRequestDto dto);

    void clearCart(ShoppingCart cart);

    ShoppingCart getCartEntityByUserEmail(String email);
}
