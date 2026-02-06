package org.example.dto.category;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryDto {

    private Long id;

    @NotBlank(message = "Category name is mandatory field")
    private String name;

    private String description;
}
