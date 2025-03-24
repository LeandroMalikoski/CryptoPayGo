package com.cryptopaygo.controller;

import com.cryptopaygo.dto.GeneralResponseDTO;
import com.cryptopaygo.dto.ProductNewDTO;
import com.cryptopaygo.dto.ProductResponseDTO;
import com.cryptopaygo.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/new")
    public ResponseEntity<GeneralResponseDTO> newProduct(@Valid @RequestBody ProductNewDTO dto, BindingResult bindingResult) {

        productService.newProduct(dto, bindingResult);

        return ResponseEntity.status(HttpStatus.CREATED).body(new GeneralResponseDTO("Product created successfully", true));

    }

    @GetMapping("/list")
    public ResponseEntity<List<ProductResponseDTO>> listProducts() {

        return ResponseEntity.status(HttpStatus.OK).body(productService.findAll());

    }
}