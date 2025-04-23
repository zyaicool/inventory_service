package com.example.inventory_service.repositories;

import com.example.inventory_service.models.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
