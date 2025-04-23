package com.example.inventory_service.services;

import com.example.inventory_service.dtos.InventoryDtos;
import com.example.inventory_service.models.Inventory;
import com.example.inventory_service.models.Item;
import com.example.inventory_service.repositories.InventoryRepository;
import com.example.inventory_service.repositories.ItemRepository;
import com.example.inventory_service.utilities.GeneralConstants;
import com.example.inventory_service.utilities.SimpleResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class InventoryService {

    @Autowired
    InventoryRepository inventoryRepository;

    @Autowired
    ItemRepository itemRepository;

    public SimpleResponse createInventory(InventoryDtos.CreateUpdateInventory request){
        try{
            Item getItem = itemRepository.findById(request.getItemId()).orElse(null);
            if (getItem == null) {
                return new SimpleResponse(GeneralConstants.BAD_REQUEST, "Data Not Exist !!!!", new HashMap<String, Object>());
            }

            Inventory inv = new Inventory();
            inv.setQty(request.getQty());
            inv.setType(request.getType());
            inv.setItem(getItem);

            inventoryRepository.save(inv);
            return new SimpleResponse(GeneralConstants.SUCCESS_CODE, GeneralConstants.SUCCESS, new HashMap<String, Object>());
        }catch (Exception e){
            return new SimpleResponse(GeneralConstants.FAIL_CODE, e.getMessage(), new HashMap<String, Object>());
        }

    }

    public SimpleResponse updateInventory(Long id, InventoryDtos.CreateUpdateInventory request){
        try{
            Inventory getInventory = inventoryRepository.findById(id).orElse(null);
            if (getInventory == null) {
                return new SimpleResponse(GeneralConstants.BAD_REQUEST, "Data Not Exist !!!!", new HashMap<String, Object>());
            }

            getInventory.setQty(request.getQty());

            inventoryRepository.save(getInventory);
            return new SimpleResponse(GeneralConstants.SUCCESS_CODE, GeneralConstants.SUCCESS, new HashMap<String, Object>());
        }catch (Exception e){
            return new SimpleResponse(GeneralConstants.FAIL_CODE, e.getMessage(), new HashMap<String, Object>());
        }

    }

    public SimpleResponse detailInventory(Long id){
        try{
            Inventory getInventory = inventoryRepository.findById(id).orElse(null);
            if (getInventory == null) {
                return new SimpleResponse(GeneralConstants.BAD_REQUEST, "Data Not Exist !!!!", new HashMap<String, Object>());
            }

            return new SimpleResponse(GeneralConstants.SUCCESS_CODE, GeneralConstants.SUCCESS, getInventory);
        }catch (Exception e){
            return new SimpleResponse(GeneralConstants.FAIL_CODE, e.getMessage(), new HashMap<String, Object>());
        }

    }

    public SimpleResponse listInventory(int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Inventory> inventoryPage = inventoryRepository.findAll(pageable);

            if (inventoryPage.isEmpty()) {
                return new SimpleResponse(
                        GeneralConstants.BAD_REQUEST,
                        "Data Not Exist !!!!",
                        new HashMap<String, Object>()
                );
            }

            HashMap<String, Object> response = new HashMap<>();
            response.put("data", inventoryPage.getContent());
            response.put("currentPage", inventoryPage.getNumber());
            response.put("totalItems", inventoryPage.getTotalElements());
            response.put("totalPages", inventoryPage.getTotalPages());

            return new SimpleResponse(
                    GeneralConstants.SUCCESS_CODE,
                    GeneralConstants.SUCCESS,
                    response
            );
        } catch (Exception e) {
            return new SimpleResponse(GeneralConstants.FAIL_CODE, e.getMessage(), new HashMap<>());
        }
    }

    public SimpleResponse deleteInventory(Long id){
        try{
            Inventory getInventory = inventoryRepository.findById(id).orElse(null);
            if (getInventory == null) {
                return new SimpleResponse(GeneralConstants.BAD_REQUEST, "Data Not Exist !!!!", new HashMap<String, Object>());
            }

            inventoryRepository.deleteById(id);
            return new SimpleResponse(GeneralConstants.SUCCESS_CODE, GeneralConstants.SUCCESS, new HashMap<String, Object>());
        }catch (Exception e){
            return new SimpleResponse(GeneralConstants.FAIL_CODE, e.getMessage(), new HashMap<String, Object>());
        }

    }
}
