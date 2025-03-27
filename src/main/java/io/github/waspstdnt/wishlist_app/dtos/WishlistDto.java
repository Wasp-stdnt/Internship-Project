package io.github.waspstdnt.wishlist_app.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WishlistDto {

    @NotBlank(message = "Назва є обов'язковою")
    private String title;

    private Long templateId;
}
