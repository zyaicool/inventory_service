package com.example.inventory_service.dtos;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

public class OrderDtos {
    @Getter
    @Setter
    public static class CreateUpdateOrder {
        @NotNull(message = "Quantity is required")
        @Min(value = 0, message = "Quantity must be greater than or equal to 0")
        private Integer qty;

        @NotNull(message = "Id from item is required")
        private Long itemId;
    }
}
