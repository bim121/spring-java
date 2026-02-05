package org.example.dto.book;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateBookRequestDto {

    @NotBlank(message = "Title must not be blank")
    @Size(max = 255, message = "Title must be less than 255 characters")
    private String title;

    @NotBlank(message = "Author must not be blank")
    @Size(max = 255, message = "Author must be less than 255 characters")
    private String author;

    @NotBlank(message = "ISBN must not be blank")
    @Pattern(
            regexp = "^[0-9\\-]{10,17}$",
            message = "ISBN must contain 10-17 digits or dashes"
    )
    private String isbn;

    @NotNull(message = "Price must not be null")
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    private BigDecimal price;

    @Size(max = 1000, message = "Description must be less than 1000 characters")
    private String description;

    @Pattern(
            regexp = "^(http|https)://.*$",
            message = "Cover image must be a valid URL"
    )
    private String coverImage;

    @NotEmpty
    private Set<Long> categoryIds;
}
