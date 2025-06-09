package com.example.ecommerce.service;

import com.example.ecommerce.dto.request.OrderItemRequest;
import com.example.ecommerce.entity.Order;
import com.example.ecommerce.entity.OrderItem;

import java.util.List;

public interface OrderItemService {
    List<OrderItem> createOrderItem(List<OrderItemRequest> request, Order order);
}
