package com.cryptopaygo.service;

import com.cryptopaygo.config.exception.ProductNotFoundException;
import com.cryptopaygo.config.exception.RequestInvalidException;
import com.cryptopaygo.dto.ProductNewDTO;
import com.cryptopaygo.dto.ProductResponseDTO;
import com.cryptopaygo.dto.ProductUpdateDTO;
import com.cryptopaygo.entity.Product;
import com.cryptopaygo.repository.ProductRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // Valida os dados da requisição e retorna erros, se houver
    private void bindingResultMaster(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.joining(", "));

            throw new RequestInvalidException(errorMessage);
        }
    }

    @Transactional
    public void newProduct(@Valid ProductNewDTO dto, BindingResult bindingResult) {

        bindingResultMaster(bindingResult);

        if (productRepository.existsByName(dto.name())) throw new RequestInvalidException("Product name already exists");

        if (dto.price() == 0) throw new RequestInvalidException("Price must be greater than 0");

        Product product = new Product(dto.name(), dto.description(), dto.category(), dto.price());

        productRepository.save(product);
    }

    public List<ProductResponseDTO> findAll() {
        List<Product> products = productRepository.findAll();

        // Transforma a lista de Product em uma lista de ProductResponseDTO
        return products.stream()
                .map(product -> new ProductResponseDTO(product.getId(), product.getName(), product.getDescription(),
                        product.getCategory(), product.getPrice(), product.getQuantity()))
                .toList();
    }

    public ProductResponseDTO getProduct(Long id) {

        Product product = productRepository.findById(id);

        if (product == null) throw new RequestInvalidException("Product not found");

        return new ProductResponseDTO(product.getId(), product.getName(), product.getCategory(), product.getDescription(),
                product.getPrice(), product.getQuantity());
    }

    // Utiliza o DoubleSummaryStatistics para trazer detalhes sobre o preço dos produtos cadastrados
    public DoubleSummaryStatistics getStatistics() {

        DoubleSummaryStatistics summaryStatistics = new DoubleSummaryStatistics();

        List<Product> products = productRepository.findAll();

        products.forEach(product -> summaryStatistics.accept(product.getPrice()));

        return summaryStatistics;

    }

    public void deleteProduct(Long id) {

        Optional<Product> product = productRepository.getProductById(id);

        if (product.isPresent()) {
            productRepository.delete(product.get());
        } else {
            throw new ProductNotFoundException("Product not found");
        }

    }

    public ProductResponseDTO updateProduct(Long id, ProductUpdateDTO dto, BindingResult bindingResult) {

        bindingResultMaster(bindingResult);

        var product = productRepository.findById(id);

        if (product == null) throw new ProductNotFoundException("Product not found");

        if (productRepository.existsByName(dto.name())) throw new RequestInvalidException("Product name already exists");

        if (dto.name() != null) product.setName(dto.name());
        if (dto.description() != null) product.setDescription(dto.description());
        if (dto.category() != null) product.setCategory(dto.category());
        if (dto.price() != null) product.setPrice(dto.price());
        if (dto.quantity() != null) product.setQuantity(dto.quantity());

        productRepository.save(product);

        return new ProductResponseDTO(product.getId(), product.getName(), product.getDescription(),
                product.getCategory(), product.getPrice(), product.getQuantity());
    }
}