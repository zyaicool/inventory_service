package com.example.inventory_service.services;

import com.example.inventory_service.dtos.ItemDtos;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemService {
    @Autowired
    ItemRepository itemRepository;

    @Autowired
    InventoryRepository inventoryRepository;

    public SimpleResponse createItem(ItemDtos.CreateUpdateItem request){
        try{
            Item item = new Item();
            item.setName(request.getName());
            item.setPrice(request.getPrice());

            itemRepository.save(item);
            return new SimpleResponse(GeneralConstants.SUCCESS_CODE, GeneralConstants.SUCCESS, new HashMap<String, Object>());
        }catch (Exception e){
            return new SimpleResponse(GeneralConstants.FAIL_CODE, e.getMessage(), new HashMap<String, Object>());
        }

    }

    public SimpleResponse updateItem(Long id, ItemDtos.CreateUpdateItem request){
        try{
            Item getItem = itemRepository.findById(id).orElse(null);
            if (getItem == null) {
                return new SimpleResponse(GeneralConstants.BAD_REQUEST, "Data Not Exist !!!!", new HashMap<String, Object>());
            }

            getItem.setName(request.getName());
            getItem.setPrice(request.getPrice());

            itemRepository.save(getItem);
            return new SimpleResponse(GeneralConstants.SUCCESS_CODE, GeneralConstants.SUCCESS, new HashMap<String, Object>());
        }catch (Exception e){
            return new SimpleResponse(GeneralConstants.FAIL_CODE, e.getMessage(), new HashMap<String, Object>());
        }

    }

    public SimpleResponse detailItem(Long id){
        try{
            Item getItem = itemRepository.findById(id).orElse(null);
            if (getItem == null) {
                return new SimpleResponse(GeneralConstants.BAD_REQUEST, "Data Not Exist !!!!", new HashMap<String, Object>());
            }

            return new SimpleResponse(GeneralConstants.SUCCESS_CODE, GeneralConstants.SUCCESS, getItem);
        }catch (Exception e){
            return new SimpleResponse(GeneralConstants.FAIL_CODE, e.getMessage(), new HashMap<String, Object>());
        }

    }

    public SimpleResponse listItem(int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Item> itemPage = itemRepository.findAll(pageable);

            if (itemPage.isEmpty()) {
                return new SimpleResponse(
                        GeneralConstants.BAD_REQUEST,
                        "Data Not Exist !!!!",
                        new HashMap<String, Object>()
                );
            }

            List<ItemDtos.MapItem> mapListItem = new ArrayList<>();
            for(Item itm:itemPage.getContent()){
                ItemDtos.MapItem mapItem = new ItemDtos.MapItem();
                mapItem.setId(itm.getId());
                mapItem.setName(itm.getName());
                mapItem.setPrice(itm.getPrice());

                Integer qty = getQuantityForItem(itm.getId());
                mapItem.setQty(qty);
                mapListItem.add(mapItem);
            }

            HashMap<String, Object> response = new HashMap<>();
            response.put("data", mapListItem);
            response.put("currentPage", itemPage.getNumber());
            response.put("totalItems", itemPage.getTotalElements());
            response.put("totalPages", itemPage.getTotalPages());

            return new SimpleResponse(
                    GeneralConstants.SUCCESS_CODE,
                    GeneralConstants.SUCCESS,
                    response
            );
        } catch (Exception e) {
            return new SimpleResponse(GeneralConstants.FAIL_CODE, e.getMessage(), new HashMap<>());
        }
    }

    public SimpleResponse deleteItem(Long id){
        try{
            Item getItem = itemRepository.findById(id).orElse(null);
            if (getItem == null) {
                return new SimpleResponse(GeneralConstants.BAD_REQUEST, "Data Not Exist !!!!", new HashMap<String, Object>());
            }

            itemRepository.deleteById(id);
            return new SimpleResponse(GeneralConstants.SUCCESS_CODE, GeneralConstants.SUCCESS, new HashMap<String, Object>());
        }catch (Exception e){
            return new SimpleResponse(GeneralConstants.FAIL_CODE, e.getMessage(), new HashMap<String, Object>());
        }

    }

    public Integer getQuantityForItem(Long itemId) {
        List<Inventory> inventories = inventoryRepository.findAllByItemId(itemId);
        Integer countQty = 0;
        for(Inventory inv : inventories){
            if(inv.getType().equals("T")){
                countQty += inv.getQty();
            }else if(inv.getType().equals("W")){
                countQty -= inv.getQty();
            }
        }
        if (countQty < 0) {
            countQty = 0;
        }
        return countQty;
    }
}
