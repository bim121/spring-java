package org.example.dto.cart;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UpdateCartItemRequestDto {
    @Min(1)
    private int quantity;
}
