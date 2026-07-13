package com.tonny.ecommerce.controller;

import com.tonny.ecommerce.DTO.PatchProductRequestDTO;
import com.tonny.ecommerce.DTO.PostProductRequestDTO;
import com.tonny.ecommerce.DTO.ProductDTO;
import com.tonny.ecommerce.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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

    @PatchMapping("/patch-product/{id}")
    public ResponseEntity<ProductDTO> patchProduct(@PathVariable UUID id, @RequestBody PatchProductRequestDTO productRequestDTO) {
        return ResponseEntity.ok(productService.patchProduct(id, productRequestDTO));
    }

    @DeleteMapping("/delete-product/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable UUID id) {
        productService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
