package com.example.inventory_service.services;

import com.example.inventory_service.dtos.OrderDtos;
import com.example.inventory_service.models.Inventory;
import com.example.inventory_service.models.Item;
import com.example.inventory_service.models.Order;
import com.example.inventory_service.repositories.InventoryRepository;
import com.example.inventory_service.repositories.ItemRepository;
import com.example.inventory_service.repositories.OrderRepository;
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
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {
    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    InventoryRepository inventoryRepository;

    @Autowired
    ItemService itemService;

    public SimpleResponse createOrder(OrderDtos.CreateUpdateOrder request){
        try{
            Item getItem = itemRepository.findById(request.getItemId()).orElse(null);
            if (getItem == null) {
                return new SimpleResponse(GeneralConstants.BAD_REQUEST, "Data Not Exist !!!!", new HashMap<String, Object>());
            }

            if(!checkRemainingQty(request.getItemId(), request.getQty())){
                return new SimpleResponse(GeneralConstants.BAD_REQUEST, "Insufficient Stock !!!!", new HashMap<String, Object>());
            }

            //save to order
            Order ord = new Order();
            ord.setQty(request.getQty());
            ord.setPrice(getItem.getPrice());
            ord.setOrderNo(generateOrderNumber());
            ord.setItem(getItem);
            orderRepository.save(ord);

            //save to inventory
            Inventory inv = new Inventory();
            inv.setQty(request.getQty());
            inv.setType(GeneralConstants.WITHDRAWAL_CODE);
            inv.setItem(getItem);
            inventoryRepository.save(inv);
            return new SimpleResponse(GeneralConstants.SUCCESS_CODE, GeneralConstants.SUCCESS, new HashMap<String, Object>());
        }catch (Exception e){
            return new SimpleResponse(GeneralConstants.FAIL_CODE, e.getMessage(), new HashMap<String, Object>());
        }

    }

    public SimpleResponse updateOrder(Long id, OrderDtos.CreateUpdateOrder request){
        try{
            Order getOrder = orderRepository.findById(id).orElse(null);
            if (getOrder == null) {
                return new SimpleResponse(GeneralConstants.BAD_REQUEST, "Data Not Exist !!!!", new HashMap<String, Object>());
            }

            Item getItem = itemRepository.findById(request.getItemId()).orElse(null);
            if (getItem == null) {
                return new SimpleResponse(GeneralConstants.BAD_REQUEST, "Data Not Exist !!!!", new HashMap<String, Object>());
            }

            if(!checkRemainingQty(request.getItemId(), request.getQty())){
                return new SimpleResponse(GeneralConstants.BAD_REQUEST, "Insufficient Stock !!!!", new HashMap<String, Object>());
            }

            //different qty when update
            Integer diffQty = 0;
            if(request.getQty() > getOrder.getQty()){
                diffQty = request.getQty()-getOrder.getQty();
            }else{
                diffQty = getOrder.getQty()-request.getQty();
            }

            //save to order
            getOrder.setQty(request.getQty());
            orderRepository.save(getOrder);

            //save to inventory
            Inventory inv = new Inventory();
            inv.setQty(diffQty);
            inv.setType(GeneralConstants.WITHDRAWAL_CODE);
            inv.setItem(getItem);
            inventoryRepository.save(inv);
            return new SimpleResponse(GeneralConstants.SUCCESS_CODE, GeneralConstants.SUCCESS, new HashMap<String, Object>());
        }catch (Exception e){
            return new SimpleResponse(GeneralConstants.FAIL_CODE, e.getMessage(), new HashMap<String, Object>());
        }

    }

    public SimpleResponse detailOrder(Long id){
        try{
            Order getOrder = orderRepository.findById(id).orElse(null);
            if (getOrder == null) {
                return new SimpleResponse(GeneralConstants.BAD_REQUEST, "Data Not Exist !!!!", new HashMap<String, Object>());
            }

            return new SimpleResponse(GeneralConstants.SUCCESS_CODE, GeneralConstants.SUCCESS, getOrder);
        }catch (Exception e){
            return new SimpleResponse(GeneralConstants.FAIL_CODE, e.getMessage(), new HashMap<String, Object>());
        }

    }

    public SimpleResponse listOrder(int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Order> orderPage = orderRepository.findAll(pageable);

            if (orderPage.isEmpty()) {
                return new SimpleResponse(
                        GeneralConstants.BAD_REQUEST,
                        "Data Not Exist !!!!",
                        new HashMap<String, Object>()
                );
            }

            HashMap<String, Object> response = new HashMap<>();
            response.put("data", orderPage.getContent());
            response.put("currentPage", orderPage.getNumber());
            response.put("totalItems", orderPage.getTotalElements());
            response.put("totalPages", orderPage.getTotalPages());

            return new SimpleResponse(
                    GeneralConstants.SUCCESS_CODE,
                    GeneralConstants.SUCCESS,
                    response
            );
        } catch (Exception e) {
            return new SimpleResponse(GeneralConstants.FAIL_CODE, e.getMessage(), new HashMap<>());
        }
    }

    public SimpleResponse deleteOrder(Long id){
        try{
            Order getOrder = orderRepository.findById(id).orElse(null);
            if (getOrder == null) {
                return new SimpleResponse(GeneralConstants.BAD_REQUEST, "Data Not Exist !!!!", new HashMap<String, Object>());
            }

            orderRepository.deleteById(id);
            return new SimpleResponse(GeneralConstants.SUCCESS_CODE, GeneralConstants.SUCCESS, new HashMap<String, Object>());
        }catch (Exception e){
            return new SimpleResponse(GeneralConstants.FAIL_CODE, e.getMessage(), new HashMap<String, Object>());
        }

    }

    private String generateOrderNumber(){
        List<Order> orders = orderRepository.findAll();
        return String.format(GeneralConstants.FORMAT_ORDER_NUMBER, orders.size()+1);
    }

    private Boolean checkRemainingQty(Long itemId, Integer qty){
        Integer qtyLeft = itemService.getQuantityForItem(itemId);
        if(qtyLeft == 0 || qtyLeft - qty == 0){
            return Boolean.FALSE;
        }

        return Boolean.TRUE;
    }
}
