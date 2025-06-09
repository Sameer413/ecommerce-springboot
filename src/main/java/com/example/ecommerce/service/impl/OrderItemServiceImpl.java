package com.example.ecommerce.service.impl;

import com.example.ecommerce.dto.request.OrderItemRequest;
import com.example.ecommerce.entity.Order;
import com.example.ecommerce.entity.OrderItem;
import com.example.ecommerce.entity.Product;
import com.example.ecommerce.exception.ResourceNotFound;
import com.example.ecommerce.repository.OrderItemRepository;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderItemServiceImpl implements OrderItemService {

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<OrderItem> createOrderItem(List<OrderItemRequest> request, Order order) {
        try {
            List<OrderItem> orderItems = new ArrayList<>();

            for(OrderItemRequest req : request){
                Product product = productRepository.findById(req.getProductId())
                        .orElseThrow(()-> new ResourceNotFound("Product not found."));

                BigDecimal price = product.getPrice().multiply(BigDecimal.valueOf(req.getQuantity()));

                OrderItem item = new OrderItem();
                item.setOrder(order);
                item.setProduct(product);
                item.setQuantity(req.getQuantity());
                item.setPrice(price);

                orderItems.add(item);
            }

            return orderItemRepository.saveAll(orderItems);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }
}
