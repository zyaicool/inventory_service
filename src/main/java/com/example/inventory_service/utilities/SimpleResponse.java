package com.example.inventory_service.utilities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SimpleResponse {
    private Long status;
    private String message;
    private Object data;

}
