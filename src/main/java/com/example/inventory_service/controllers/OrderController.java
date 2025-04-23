package com.example.inventory_service.controllers;

import com.example.inventory_service.dtos.OrderDtos;
import com.example.inventory_service.services.OrderService;
import com.example.inventory_service.utilities.SimpleResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    OrderService orderService;

    @PostMapping
    public SimpleResponse createOrder(@Valid @RequestBody OrderDtos.CreateUpdateOrder request) {
        return orderService.createOrder(request);
    }

    @PutMapping("/{id}")
    public SimpleResponse updateOrder(@PathVariable Long id, @Valid @RequestBody OrderDtos.CreateUpdateOrder request) {
        return orderService.updateOrder(id, request);
    }

    @GetMapping("/{id}")
    public SimpleResponse detailInventory(@PathVariable Long id) {
        return orderService.detailOrder(id);
    }

    @GetMapping
    public SimpleResponse listOrder(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return orderService.listOrder(page, size);
    }

    @DeleteMapping("/{id}")
    public SimpleResponse deleteOrder(@PathVariable Long id) {
        return orderService.deleteOrder(id);
    }
}
