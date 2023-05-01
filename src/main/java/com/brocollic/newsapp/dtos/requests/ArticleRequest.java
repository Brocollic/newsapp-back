package com.brocollic.newsapp.dtos.requests;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleRequest {

    @NotBlank(message = "title cannot be null and it's trimmed length must greater than zero.")
    @Size(max = 120, message = "title cannot be longer than 120 characters.")
    private String title;

    @NotBlank(message = "content cannot be null and it's trimmed length must greater than zero.")
    @Size(max = 60000, message = "title cannot be longer than 60000 characters.")
    private String content;

    @Digits(integer = 15, fraction = 0, message = "categoryId cannot be null and must an integer.")
    @PositiveOrZero(message = "categoryId must be positive integer or zero.")
    @NotNull(message = "categoryId must not be null")
    private Long categoryId;

}
