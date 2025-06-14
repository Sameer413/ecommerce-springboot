package com.example.ecommerce.controller;

import com.example.ecommerce.dto.reponse.ProductResponse;
import com.example.ecommerce.dto.request.ProductRequest;
import com.example.ecommerce.entity.Product;
import com.example.ecommerce.payload.ApiResponse;
import com.example.ecommerce.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import java.util.Set;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @Autowired
    private Validator validator;

    @PostMapping(value = "/create", consumes = "multipart/form-data", produces = "application/json")
    public ResponseEntity<ApiResponse<ProductResponse>> createProduct(
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("price") BigDecimal price,
            @RequestParam("stock") Integer stock,
            @RequestParam("categoryId") UUID categoryId,
            @RequestParam("file") MultipartFile file){

        ProductRequest productRequest = new ProductRequest();
        productRequest.setName(name);
        productRequest.setDescription(description);
        productRequest.setPrice(price);
        productRequest.setStock(stock);
        productRequest.setCategoryId(categoryId);

        // Validate the product request
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(productRequest);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        Product createdProduct = productService.createProduct(productRequest, file);
        ProductResponse response = ProductResponse.mapTo(createdProduct);

        return ResponseEntity.ok(new ApiResponse<>(true,"Product created successfully.", response));
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProduct(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ){
        Pageable pageable = PageRequest.of(
                page,
                size,
                sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending()
        );

        List<Product> products = productService.getAllProducts(pageable);

        List<ProductResponse> productResponses = products.stream().map(ProductResponse::mapTo).toList();
        return ResponseEntity.ok(productResponses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> getProductDetails(@PathVariable UUID id){
        Product product = productService.getProductById(id);

        return ResponseEntity.ok(new ApiResponse<>(true,"Product fetched successfully.", ProductResponse.mapTo(product)));
    }

//    @PutMapping("{id}")
//    public ResponseEntity<ApiResponse<ProductResponse>> updateProductDetails(@PathVariable UUID id, @RequestBody ProductRequest productRequest){
//        Product product = productService.updateProduct(id, productRequest);
//
//        return ResponseEntity.ok(new ApiResponse<>(true, "Product fetched successfully.", ProductResponse.mapTo(product)));
//    }

    @PutMapping("{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> updateProductDetails(
            @PathVariable UUID id,
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("price") BigDecimal price,
            @RequestParam("stock") Integer stock,
            @RequestParam("categoryId") UUID categoryId,
            @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {

        ProductRequest productRequest = new ProductRequest();
        productRequest.setName(name);
        productRequest.setDescription(description);
        productRequest.setPrice(price);
        productRequest.setStock(stock);
        productRequest.setCategoryId(categoryId);

        // Validate the product request
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(productRequest);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        Product product = productService.updateProduct(id, productRequest, file);

        return ResponseEntity.ok(new ApiResponse<>(true, "Product updated successfully.", ProductResponse.mapTo(product)));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse<?>> deleteProduct(@PathVariable UUID id) throws IOException{
        String message = productService.deleteProduct(id);

        return ResponseEntity.ok(new ApiResponse<>(true, message));
    }

    @PostMapping("/upload-image")
    public ResponseEntity<ApiResponse<String>> uploadFileHandler(@RequestParam("file")MultipartFile file) throws IOException {
        String url = productService.uploadFile(file);
        return ResponseEntity.ok(new ApiResponse<>(true, "", url));
    }
}
