package com.example.inventory_service.services;

import com.example.inventory_service.dtos.ItemDtos;
import com.example.inventory_service.models.Item;
import com.example.inventory_service.repositories.ItemRepository;
import com.example.inventory_service.utilities.GeneralConstants;
import com.example.inventory_service.utilities.SimpleResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemService {
    @Autowired
    ItemRepository itemRepository;

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

    public SimpleResponse listItem(){
        try{
            List<Item> getItem = itemRepository.findAll();
            if (getItem.isEmpty()) {
                return new SimpleResponse(GeneralConstants.BAD_REQUEST, "Data Not Exist !!!!", new HashMap<String, Object>());
            }

            return new SimpleResponse(GeneralConstants.SUCCESS_CODE, GeneralConstants.SUCCESS, getItem);
        }catch (Exception e){
            return new SimpleResponse(GeneralConstants.FAIL_CODE, e.getMessage(), new HashMap<String, Object>());
        }

    }

    public SimpleResponse deleteItem(Long id){
        try{
            Item getItem = itemRepository.findById(id).orElse(null);
            if (getItem == null) {
                return new SimpleResponse(GeneralConstants.BAD_REQUEST, "Data Not Exist !!!!", new HashMap<String, Object>());
            }

            itemRepository.deleteById(id);
            return new SimpleResponse(GeneralConstants.SUCCESS_CODE, GeneralConstants.SUCCESS, getItem);
        }catch (Exception e){
            return new SimpleResponse(GeneralConstants.FAIL_CODE, e.getMessage(), new HashMap<String, Object>());
        }

    }
}
