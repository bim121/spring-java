package org.example.mappers;

import org.example.dto.cart.AddToCartRequestDto;
import org.example.dto.cart.CartItemDto;
import org.example.dto.cart.ShoppingCartDto;
import org.example.model.Book;
import org.example.model.CartItem;
import org.example.model.ShoppingCart;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ShoppingCartMapper {

    ShoppingCartDto toDto(ShoppingCart cart);

    CartItemDto toDto(CartItem item);

    @Named("bookFromId")
    default Book bookFromId(Long id) {
        Book book = new Book();
        book.setId(id);
        return book;
    }

    default void updateCartItemFromDto(AddToCartRequestDto dto, @MappingTarget CartItem item) {
        if (dto.getBookId() != null) {
            item.setBook(bookFromId(dto.getBookId()));
        }
        item.setQuantity(dto.getQuantity());
    }
}
