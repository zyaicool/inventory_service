package com.example.inventory_service.controllers;

import com.example.inventory_service.dtos.ItemDtos;
import com.example.inventory_service.services.ItemService;
import com.example.inventory_service.utilities.SimpleResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequestMapping("/items")
public class ItemController {

    @Autowired
    ItemService itemService;

    @PostMapping
    public SimpleResponse createItem(@Valid @RequestBody ItemDtos.CreateUpdateItem request) {
        return itemService.createItem(request);
    }

    @PutMapping("/{id}")
    public SimpleResponse updateItem(@PathVariable Long id, @Valid @RequestBody ItemDtos.CreateUpdateItem request) {
        return itemService.updateItem(id, request);
    }

    @GetMapping("/{id}")
    public SimpleResponse detailItem(@PathVariable Long id) {
        return itemService.detailItem(id);
    }

    @GetMapping
    public SimpleResponse listItem(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return itemService.listItem(page, size);
    }

    @DeleteMapping("/{id}")
    public SimpleResponse deleteItem(@PathVariable Long id) {
        return itemService.deleteItem(id);
    }
}
