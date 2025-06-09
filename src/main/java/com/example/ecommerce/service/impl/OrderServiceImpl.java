package com.example.ecommerce.service.impl;

import com.example.ecommerce.dto.request.OrderRequest;
import com.example.ecommerce.entity.Order;
import com.example.ecommerce.entity.OrderItem;
import com.example.ecommerce.entity.Payment;
import com.example.ecommerce.entity.User;
import com.example.ecommerce.enums.OrderStatus;
import com.example.ecommerce.enums.PaymentStatus;
import com.example.ecommerce.exception.ResourceNotFound;
import com.example.ecommerce.repository.OrderRepository;
import com.example.ecommerce.repository.UserRepository;
import com.example.ecommerce.service.OrderItemService;
import com.example.ecommerce.service.OrderService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderItemService orderItemService;

    @Override
    @Transactional
    public Order createOrder(OrderRequest request) {
        try {
            // Get user from security context
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new ResourceNotFound("User not found."));

            // Create Payment

            // Create empty Order
            Order order = new Order();
            order.setUser(user);
            order.setOrderStatus(OrderStatus.PENDING);
            order.setPayment(null);
            order.setPaymentStatus(PaymentStatus.FAILED);
            order.setTotalAmount(BigDecimal.ZERO);
            order.setOrderItems(new ArrayList<>());

            Order savedOrder = orderRepository.save(order);

            // Create OrderItems via service
            List<OrderItem> items = orderItemService.createOrderItem(request.getOrderItems(), savedOrder);

            // Calculate total
            BigDecimal totalAmount = items.stream()
                    .map(OrderItem::getPrice)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            savedOrder.setOrderItems(items);
            savedOrder.setTotalAmount(totalAmount);
            savedOrder.setPaymentStatus(PaymentStatus.SUCCESS);
            savedOrder = orderRepository.save(savedOrder);

            // Update payment amount


            return savedOrder;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public Order getOrderDetail(UUID id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Order not found with the id: " + id));
    }

    @Override
    public Order updateOrderStatus(UUID id, OrderStatus status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Order not found with id: " + id));

        order.setOrderStatus(status);

        return orderRepository.save(order);
    }
}

//@Transactional
//public OrderResponseDTO createOrder(OrderRequestDTO dto) {
//    User user = userRepository.findById(dto.getUserId())
//            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
//
//    // Payment
//    Payment payment = new Payment();
//    payment.setAmount(BigDecimal.ZERO); // will update later
//    payment.setPaymentMethod(dto.getPayment().getMethod());
//    payment.setPaymentStatus(PaymentStatus.PENDING);
//    payment.setUser(user);
//    payment.setCreatedAt(LocalDateTime.now());
//    payment = paymentRepository.save(payment);
//
//    // Create empty order
//    Order order = new Order();
//    order.setUser(user);
//    order.setOrderStatus("PENDING");
//    order.setPaymentStatus("PENDING");
//    order.setPayment(payment);
//    order.setTotalAmount(BigDecimal.ZERO); // will update later
//    order.setOrderItems(new ArrayList<>());
//
//    Order savedOrder = orderRepository.save(order);
//
//    // Delegate to OrderItemService
//    List<OrderItem> items = orderItemService.createOrderItems(dto.getItems(), savedOrder);
//
//    BigDecimal totalAmount = items.stream()
//            .map(OrderItem::getPrice)
//            .reduce(BigDecimal.ZERO, BigDecimal::add);
//
//    savedOrder.setOrderItems(items);
//    savedOrder.setTotalAmount(totalAmount);
//    savedOrder = orderRepository.save(savedOrder);
//
//    // Update payment amount too
//    payment.setAmount(totalAmount);
//    paymentRepository.save(payment);
//
//    return mapToResponse(savedOrder);
//}