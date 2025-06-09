package com.example.ecommerce.dto.reponse;

import com.example.ecommerce.entity.OrderItem;

import java.math.BigDecimal;
import java.util.UUID;

public class OrderItemResponse {
    private UUID orderItemId;
    private UUID orderId;
    private UUID productId;
    private int quantity;
    private BigDecimal price;

    // Getters and Setters

    public UUID getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(UUID orderItemId) {
        this.orderItemId = orderItemId;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    // Mapping method
    public static OrderItemResponse mapTo(OrderItem item) {
        OrderItemResponse response = new OrderItemResponse();

        response.setOrderItemId(item.getOrderItemId());

        if (item.getOrder() != null) {
            response.setOrderId(item.getOrder().getOrderId());
        }

        if (item.getProduct() != null) {
            response.setProductId(item.getProduct().getProduct_id());
        }

        response.setQuantity(item.getQuantity());
        response.setPrice(item.getPrice());

        return response;
    }
}
