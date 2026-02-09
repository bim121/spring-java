package org.example.services.cart.item.impl;

import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.example.dto.cart.AddToCartRequestDto;
import org.example.dto.cart.CartItemDto;
import org.example.dto.cart.UpdateCartItemRequestDto;
import org.example.exceptions.EntityNotFoundException;
import org.example.mappers.ShoppingCartMapper;
import org.example.model.CartItem;
import org.example.model.ShoppingCart;
import org.example.repositories.CartItemRepository;
import org.example.services.cart.item.CartItemService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class CartItemServiceImpl implements CartItemService {

    private final CartItemRepository cartItemRepository;
    private final ShoppingCartMapper cartMapper;

    @Override
    public CartItemDto updateCartItem(Long cartItemId, UpdateCartItemRequestDto dto) {
        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "CartItem not found with id " + cartItemId
                ));
        item.setQuantity(dto.getQuantity());
        return cartMapper.toDto(item);
    }

    @Override
    public void removeCartItem(Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }

    @Override
    public CartItemDto addOrUpdateCartItem(ShoppingCart cart, AddToCartRequestDto dto) {
        Optional<CartItem> existingItem = cart.getCartItems().stream()
                .filter(item -> item.getBook().getId().equals(dto.getBookId()))
                .findFirst();

        CartItem cartItem;
        if (existingItem.isPresent()) {
            cartItem = existingItem.get();
            return updateCartItem(
                    cartItem.getId(),
                    new UpdateCartItemRequestDto(cartItem.getQuantity() + dto.getQuantity())
            );
        } else {
            cartItem = new CartItem();
            cartItem.setShoppingCart(cart);
            cartMapper.updateCartItemFromDto(dto, cartItem);
            cart.getCartItems().add(cartItem);
            cartItemRepository.save(cartItem);
            return cartMapper.toDto(cartItem);
        }
    }
}

