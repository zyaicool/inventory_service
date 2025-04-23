package com.example.inventory_service.controllers;

import com.example.inventory_service.dtos.InventoryDtos;
import com.example.inventory_service.services.InventoryService;
import com.example.inventory_service.utilities.SimpleResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequestMapping("/inventories")
public class InventoryController {

    @Autowired
    InventoryService inventoryService;

    @PostMapping
    public SimpleResponse createInventory(@Valid @RequestBody InventoryDtos.CreateUpdateInventory request) {
        return inventoryService.createInventory(request);
    }

    @PutMapping("/{id}")
    public SimpleResponse updateInventory(@PathVariable Long id, @Valid @RequestBody InventoryDtos.CreateUpdateInventory request) {
        return inventoryService.updateInventory(id, request);
    }

    @GetMapping("/{id}")
    public SimpleResponse detailInventory(@PathVariable Long id) {
        return inventoryService.detailInventory(id);
    }

    @GetMapping
    public SimpleResponse listInventory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return inventoryService.listInventory(page, size);
    }

    @DeleteMapping("/{id}")
    public SimpleResponse deleteInventory(@PathVariable Long id) {
        return inventoryService.deleteInventory(id);
    }
}
