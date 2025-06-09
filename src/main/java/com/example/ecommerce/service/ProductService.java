package com.example.ecommerce.service;

import com.example.ecommerce.dto.request.ProductRequest;
import com.example.ecommerce.entity.Product;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface ProductService {
    Product createProduct(ProductRequest product);
    List<Product> getAllProducts();
    Product getProductById(UUID id);
    Product updateProduct(UUID id, ProductRequest productRequest);
    String deleteProduct(UUID id);
    String uploadFile(MultipartFile file) throws IOException;
}
