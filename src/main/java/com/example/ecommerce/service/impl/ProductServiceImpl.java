package com.example.ecommerce.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.ecommerce.dto.request.ProductRequest;
import com.example.ecommerce.entity.Category;
import com.example.ecommerce.entity.Product;
import com.example.ecommerce.exception.ResourceNotFound;
import com.example.ecommerce.repository.CategoryRepository;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private Cloudinary cloudinary;

    @Override
    public Product createProduct(ProductRequest product) {
        try {
            Product createdProduct = new Product();
            createdProduct.setName(product.getName());
            createdProduct.setDescription(product.getDescription());
            createdProduct.setStock(product.getStock());
            createdProduct.setPrice(product.getPrice());
            createdProduct.setCreatedAt(LocalDateTime.now());
            System.out.println("herer");

            if(product.getCategoryId() != null){
                Category category = categoryRepository.findById(product.getCategoryId())
                        .orElseThrow(()-> new ResourceNotFound("Category not found with the id: " + product.getCategoryId()));

                createdProduct.setCategory(category);
            }

            return productRepository.save(createdProduct);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getProductById(UUID id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Product not found with the id: " + id));
    }

    @Override
    public Product updateProduct(UUID id, ProductRequest productRequest) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Product not found with id: " + id));

        if (productRequest.getName() != null && !productRequest.getName().isBlank()) {
            product.setName(productRequest.getName());
        }

        if (productRequest.getDescription() != null) {
            product.setDescription(productRequest.getDescription());
        }

        if (productRequest.getPrice() != null) {
            product.setPrice(productRequest.getPrice());
        }

        if (productRequest.getStock() != null) {
            product.setStock(productRequest.getStock());
        }

        if (productRequest.getImageUrl() != null && !productRequest.getImageUrl().isBlank()) {
            product.setImage_url(productRequest.getImageUrl());
        }

        // Optional: Update category if provided
        if (productRequest.getCategoryId() != null) {
            Category category = categoryRepository.findById(productRequest.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFound("Category not found with id: " + productRequest.getCategoryId()));
            product.setCategory(category);
        }

        return productRepository.save(product);
    }

    @Override
    public String deleteProduct(UUID id) {

        if(!productRepository.existsById(id)){
            throw new ResourceNotFound("Product not found with the id: " + id);
        }

        productRepository.deleteById(id);
        return "Product deleted successfully.";
    }

    @Override
    public String uploadFile(MultipartFile file) throws IOException {
        String folderName = "product";
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                "folder", folderName
        ));
        return uploadResult.get("secure_url").toString();
    }
}
