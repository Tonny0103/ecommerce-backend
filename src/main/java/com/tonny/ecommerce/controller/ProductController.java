package com.tonny.ecommerce.controller;

import com.tonny.ecommerce.DTO.PostProductRequestDTO;
import com.tonny.ecommerce.DTO.ProductDTO;
import com.tonny.ecommerce.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/create-product")
    public ResponseEntity<ProductDTO> createProduct(@RequestBody PostProductRequestDTO productRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(productRequestDTO));
    }

    @GetMapping("/get-all-products")
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }
}
