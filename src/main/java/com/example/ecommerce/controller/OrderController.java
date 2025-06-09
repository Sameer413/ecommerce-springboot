package com.example.ecommerce.controller;

import com.example.ecommerce.dto.reponse.OrderResponse;
import com.example.ecommerce.dto.request.OrderRequest;
import com.example.ecommerce.entity.Order;
import com.example.ecommerce.enums.OrderStatus;
import com.example.ecommerce.payload.ApiResponse;
import com.example.ecommerce.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<OrderResponse>> createOrder(@RequestBody OrderRequest request){
        Order order = orderService.createOrder(request);

        return ResponseEntity.ok(new ApiResponse<>(true, "Order crated successfully.", OrderResponse.mapTo(order)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrderDetail(@PathVariable UUID id){
        Order order = orderService.getOrderDetail(id);

        return ResponseEntity.ok(new ApiResponse<>(true, "Order details.", OrderResponse.mapTo(order)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderResponse>> updateOrderStatusHandler(@PathVariable UUID id, @RequestParam OrderStatus status){
        Order order = orderService.updateOrderStatus(id, status);


        return ResponseEntity.ok(new ApiResponse<>(true, "Order details.", OrderResponse.mapTo(order)));
    }
}
