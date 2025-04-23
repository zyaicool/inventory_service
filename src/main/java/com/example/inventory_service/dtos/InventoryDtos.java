package com.example.inventory_service.dtos;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

public class InventoryDtos {

    @Getter
    @Setter
    public static class CreateUpdateInventory {

        @NotBlank(message = "type must not be blank")
        @Size(max = 1, message = "type must be at most 100 characters")
        @Pattern(regexp = "^[TW]$", message = "Type must be either 'T' or 'W'")
        private String type;

        @NotNull(message = "Quantity is required")
        @Min(value = 0, message = "Quantity must be greater than or equal to 0")
        private Integer qty;

        @NotNull(message = "Id from item is required")
        private Long itemId;
    }
}
