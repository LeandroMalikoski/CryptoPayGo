package com.cryptopaygo.controller;

import com.cryptopaygo.dto.*;
import com.cryptopaygo.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.DoubleSummaryStatistics;
import java.util.List;

@RestController
@RequestMapping("/product")
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

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> findProductById(@PathVariable Long id) {

        return ResponseEntity.status(HttpStatus.OK).body(productService.getProduct(id));

    }

    // Retorna a quantidade de produtos cadastrados, a soma de seus preços, menor valor, maior valor e média
    @GetMapping("/details")
    public ResponseEntity<StatisticsDTO> getStatistics() {

        DoubleSummaryStatistics summaryStatistics = productService.getStatistics();

        return ResponseEntity.status(HttpStatus.OK).body(new StatisticsDTO((int) summaryStatistics.getCount(), summaryStatistics.getSum(),
                summaryStatistics.getMin(), summaryStatistics.getMax(), summaryStatistics.getAverage()));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GeneralResponseDTO> deleteProduct(@PathVariable Long id) {

        productService.deleteProduct(id);

        return ResponseEntity.status(HttpStatus.OK).body(new GeneralResponseDTO("Product deleted successfully", true));

    }

    @PatchMapping("{id}")
    public ResponseEntity<ProductResponseDTO> updateProduct(@PathVariable Long id, @RequestBody ProductUpdateDTO dto, BindingResult bindingResult) {

        var product = productService.updateProduct(id, dto, bindingResult);

        return ResponseEntity.status(HttpStatus.OK).body(new ProductResponseDTO(product));
    }
}