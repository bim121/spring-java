package org.example.services.cart.item;

import org.example.dto.cart.AddToCartRequestDto;
import org.example.dto.cart.CartItemDto;
import org.example.dto.cart.UpdateCartItemRequestDto;
import org.example.model.ShoppingCart;

public interface CartItemService {
    CartItemDto updateCartItem(Long cartItemId, UpdateCartItemRequestDto dto);

    void removeCartItem(Long cartItemId);

    CartItemDto addOrUpdateCartItem(ShoppingCart cart, AddToCartRequestDto dto);
}
