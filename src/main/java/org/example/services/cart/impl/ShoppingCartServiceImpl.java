package org.example.services.cart.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.dto.cart.AddToCartRequestDto;
import org.example.dto.cart.CartItemDto;
import org.example.dto.cart.ShoppingCartDto;
import org.example.dto.cart.UpdateCartItemRequestDto;
import org.example.exceptions.EntityNotFoundException;
import org.example.mappers.ShoppingCartMapper;
import org.example.model.ShoppingCart;
import org.example.model.User;
import org.example.repositories.ShoppingCartRepository;
import org.example.services.cart.ShoppingCartService;
import org.example.services.cart.item.CartItemService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final ShoppingCartRepository cartRepository;
    private final CartItemService cartItemService;
    private final ShoppingCartMapper cartMapper;

    @Override
    public ShoppingCartDto getCartByUserEmail(String email) {
        return cartMapper.toDto(findCartByUserEmail(email));
    }

    @Override
    public CartItemDto addBookToCartByUserEmail(String email, AddToCartRequestDto dto) {
        ShoppingCart cart = findCartByUserEmail(email);
        return cartItemService.addOrUpdateCartItem(cart, dto);
    }

    @Override
    public CartItemDto updateCartItem(Long cartItemId, UpdateCartItemRequestDto dto) {
        return cartItemService.updateCartItem(cartItemId, dto);
    }

    @Override
    public void removeCartItem(Long cartItemId) {
        cartItemService.removeCartItem(cartItemId);
    }

    @Override
    public ShoppingCart createCartForUser(User user) {
        ShoppingCart cart = new ShoppingCart();
        cart.setUser(user);
        return cartRepository.save(cart);
    }

    private ShoppingCart findCartByUserEmail(String email) {
        return cartRepository.findByUserEmail(email)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Cart not found for user " + email
                ));
    }
}
