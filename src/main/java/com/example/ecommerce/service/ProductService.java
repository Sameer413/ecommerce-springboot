package com.example.ecommerce.service;

import com.example.ecommerce.dto.request.ProductRequest;
import com.example.ecommerce.entity.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface ProductService {
    Product createProduct(ProductRequest product, MultipartFile file);
    List<Product> getAllProducts(Pageable pageable);
    Product getProductById(UUID id);
    Product updateProduct(UUID id, ProductRequest productRequest, MultipartFile file) throws IOException;
    String deleteProduct(UUID id) throws IOException;
    String uploadFile(MultipartFile file) throws IOException;
}
