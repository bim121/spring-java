package org.example.services.cart.impl;

import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.example.dto.cart.AddToCartRequestDto;
import org.example.dto.cart.CartItemDto;
import org.example.dto.cart.ShoppingCartDto;
import org.example.dto.cart.UpdateCartItemRequestDto;
import org.example.exceptions.EntityNotFoundException;
import org.example.mappers.ShoppingCartMapper;
import org.example.model.CartItem;
import org.example.model.ShoppingCart;
import org.example.model.User;
import org.example.repositories.BookRepository;
import org.example.repositories.CartItemRepository;
import org.example.repositories.ShoppingCartRepository;
import org.example.services.cart.ShoppingCartService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final ShoppingCartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final BookRepository bookRepository;
    private final ShoppingCartMapper cartMapper;

    @Override
    public ShoppingCartDto getCart(Long userId) {
        ShoppingCart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Cart not found for user "
                        + userId));
        return cartMapper.toDto(cart);
    }

    @Override
    public CartItemDto addBookToCart(Long userId, AddToCartRequestDto dto) {
        ShoppingCart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Cart not found for user "
                        + userId));
        Optional<CartItem> existingItem = cart.getCartItems().stream()
                .filter(item -> item.getBook().getId().equals(dto.getBookId()))
                .findFirst();
        CartItem cartItem;
        if (existingItem.isPresent()) {
            cartItem = existingItem.get();
            AddToCartRequestDto updateDto = new AddToCartRequestDto();
            updateDto.setBookId(dto.getBookId());
            updateDto.setQuantity(cartItem.getQuantity() + dto.getQuantity());
            cartMapper.updateCartItemFromDto(updateDto, cartItem);
        } else {
            cartItem = new CartItem();
            cartMapper.updateCartItemFromDto(dto, cartItem);
            cartItem.setShoppingCart(cart);
            cart.getCartItems().add(cartItem);
        }
        cartItemRepository.save(cartItem);
        return cartMapper.toDto(cartItem);
    }

    @Override
    public CartItemDto updateCartItem(Long cartItemId, UpdateCartItemRequestDto dto) {
        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new EntityNotFoundException("CartItem not found with id "
                        + cartItemId));
        item.setQuantity(dto.getQuantity());
        return cartMapper.toDto(item);
    }

    @Override
    public void removeCartItem(Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }

    @Override
    public ShoppingCart createCartForUser(User user) {
        ShoppingCart cart = new ShoppingCart();
        cart.setUser(user);
        return cartRepository.save(cart);
    }

    @Override
    public ShoppingCart getCartByUser(User user) {
        return cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Cart not found for user "
                        + user.getId()));
    }
}
