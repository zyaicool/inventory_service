package com.example.inventory_service.repositories;

import com.example.inventory_service.models.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    List<Inventory> findAllByItemId(Long itemId);
}
