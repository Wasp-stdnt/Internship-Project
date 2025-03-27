package io.github.waspstdnt.wishlist_app.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EntryDto {

    @NotBlank(message = "Назва є обов'язковою")
    private String title;

    @NotNull(message = "Data is required")
    private Map<String, Object> data = new HashMap<>();
}
