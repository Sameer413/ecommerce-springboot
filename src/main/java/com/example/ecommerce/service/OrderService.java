package com.example.ecommerce.service;

import com.example.ecommerce.dto.request.OrderRequest;
import com.example.ecommerce.entity.Order;
import com.example.ecommerce.enums.OrderStatus;

import java.util.UUID;

public interface OrderService {
    Order createOrder(OrderRequest request);
    Order getOrderDetail(UUID id);
    Order updateOrderStatus(UUID id, OrderStatus status);
}
