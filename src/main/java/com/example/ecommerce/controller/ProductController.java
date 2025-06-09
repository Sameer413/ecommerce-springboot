package com.example.ecommerce.controller;

import com.example.ecommerce.dto.reponse.ProductResponse;
import com.example.ecommerce.dto.request.ProductRequest;
import com.example.ecommerce.entity.Product;
import com.example.ecommerce.payload.ApiResponse;
import com.example.ecommerce.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @PostMapping(value = "/create",produces = "application/json")
    public ResponseEntity<ApiResponse<ProductResponse>> createProduct(@Valid @RequestBody ProductRequest product){
        Product createdProduct = productService.createProduct(product);

        ProductResponse response = ProductResponse.mapTo(createdProduct);

        return ResponseEntity.ok(new ApiResponse<>(true,"Product created successfully.", response));
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProduct(){
        List<Product> products = productService.getAllProducts();

        List<ProductResponse> productResponses = products.stream().map(ProductResponse::mapTo).toList();
        return ResponseEntity.ok(productResponses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> getProductDetails(@PathVariable UUID id){
        Product product = productService.getProductById(id);

        return ResponseEntity.ok(new ApiResponse<>(true,"Product fetched successfully.", ProductResponse.mapTo(product)));
    }

    @PutMapping("{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> updateProductDetails(@PathVariable UUID id, @RequestBody ProductRequest productRequest){
        Product product = productService.updateProduct(id, productRequest);

        return ResponseEntity.ok(new ApiResponse<>(true, "Product fetched successfully.", ProductResponse.mapTo(product)));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse<?>> deleteProduct(@PathVariable UUID id){
        String message = productService.deleteProduct(id);

        return ResponseEntity.ok(new ApiResponse<>(true, message));
    }

    @PostMapping("/upload-image")
    public ResponseEntity<ApiResponse<String>> uploadFileHandler(@RequestParam("file")MultipartFile file) throws IOException {
        String url = productService.uploadFile(file);
        return ResponseEntity.ok(new ApiResponse<>(true, "", url));
    }
}
