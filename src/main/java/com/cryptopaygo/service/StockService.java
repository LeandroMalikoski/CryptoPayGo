package com.cryptopaygo.service;

import com.cryptopaygo.config.entity.User;
import com.cryptopaygo.config.exception.RequestInvalidException;
import com.cryptopaygo.config.repository.UserRepository;
import com.cryptopaygo.dto.StockBalanceDTO;
import com.cryptopaygo.dto.StockMovementDTO;
import com.cryptopaygo.dto.StockResponseDTO;
import com.cryptopaygo.entity.Product;
import com.cryptopaygo.entity.Stock;
import com.cryptopaygo.enums.MovementType;
import com.cryptopaygo.exception.MovementErrorException;
import com.cryptopaygo.exception.StockRegisterNotFound;
import com.cryptopaygo.integration.service.CoinMarketCapService;
import com.cryptopaygo.repository.ProductRepository;
import com.cryptopaygo.repository.StockRepository;
import jakarta.validation.Valid;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StockService {

    private final StockRepository stockRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CoinMarketCapService coinMarketCapService;
    private final Environment environment;

    public StockService(StockRepository stockRepository, UserRepository userRepository, ProductRepository productRepository, CoinMarketCapService coinMarketCapService, Environment environment) {
        this.stockRepository = stockRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.coinMarketCapService = coinMarketCapService;
        this.environment = environment;
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

    public StockResponseDTO stockMovement(@Valid StockMovementDTO dto, BindingResult bindingResult, User user) {

        bindingResultMaster(bindingResult);

        String apiKey = environment.getProperty("coinmarketcap.apikey");

        var product = productRepository.findById(dto.productId());

        if (product == null) throw new RequestInvalidException("Product not found");

        if (dto.movementType() == MovementType.fromString("ENTRY")) {
            return stockEntry(dto, product, user);
        } else if (dto.movementType() == MovementType.fromString("EXIT")) {
            return stockExit(dto, product, user, apiKey);
        }
        return null;
    }

    public StockResponseDTO stockEntry(StockMovementDTO dto, Product product, User user) {
        try {
            if (product.getQuantity() == null) {
                product.setQuantity(dto.quantity());
            } else {
                product.setQuantity(product.getQuantity() + dto.quantity());
            }

            var movementDate = LocalDateTime.now();

            Stock stock = new Stock(dto.productId(), user.getUserId(), dto.quantity(), dto.movementType(), null,
                    null, dto.purchasePrice(), movementDate, productRepository, userRepository);

            stockRepository.save(stock);

            return new StockResponseDTO(stock.getId(), stock.getProduct().getId(), stock.getUser().getUserId(), stock.getQuantity(), stock.getMovementType(),
                    stock.getCurrencyType(), stock.getCurrencyPaid(), stock.getPurchasePrice(), stock.getMovementDate());
        } catch (MovementErrorException e) {
            throw new MovementErrorException("Error occurred during the entry record");
        }
    }

    public StockResponseDTO stockExit(StockMovementDTO dto, Product product, User user, String apiKey) {
        try {
            if (dto.currencyCoin() == null || dto.convertCoin() == null)
                throw new RequestInvalidException("One output requires type of crypto and type of currency");

            if (product.getQuantity() == null) {
                throw new RequestInvalidException("Stock is null for product " + dto.productId());
            } else {
                product.setQuantity(product.getQuantity() - dto.quantity());
            }
            // Faz a requisição para a API e retorna o preço da crypto moeda na moeda enviada na requisição
            var json = coinMarketCapService.getCurrency(apiKey, dto.currencyCoin(), dto.convertCoin());

            // Calcula a quantidade da crypto moeda usada para a compra
            var currencyPaid = dto.quantity() * (product.getPrice() / json);

            // Calcula o preço total em dólar da compra
            var purchasePrice = dto.quantity() * product.getPrice();

            var movementDate = LocalDateTime.now();

            var stock = new Stock(product.getId(), user.getUserId(), dto.quantity(), dto.movementType(), dto.currencyCoin(),
                    currencyPaid, purchasePrice, movementDate, productRepository, userRepository);

            stockRepository.save(stock);

            return new StockResponseDTO(stock.getId(), stock.getProduct().getId(), stock.getId(), stock.getQuantity(), stock.getMovementType(),
                    stock.getCurrencyType(), stock.getCurrencyPaid(), stock.getPurchasePrice(), stock.getMovementDate());

        } catch (MovementErrorException e) {
            throw new MovementErrorException("Error occurred during the exit record");
        }
    }

    public StockResponseDTO findStockById(Long id) {

        Optional<Stock> stock = stockRepository.findStockById(id);

        if (stock.isEmpty()) throw new StockRegisterNotFound("Stock not found");

        return new StockResponseDTO(stock.get().getId(), stock.get().getProduct().getId(), stock.get().getUser().getUserId(), stock.get().getQuantity(),
                stock.get().getMovementType(), stock.get().getCurrencyType(), stock.get().getCurrencyPaid(), stock.get().getPurchasePrice(), stock.get().getMovementDate());

    }

    public StockBalanceDTO getStockBalance() {
        List<Object[]> results = stockRepository.getBalance();

        // Verifica se a lista não está vazia antes de acessar o primeiro elemento
        if (!results.isEmpty()) {
            Object[] data = results.getFirst(); // Obtém o primeiro resultado

            return new StockBalanceDTO(
                    data[0] != null ? ((Number) data[0]).doubleValue() : 0.0,
                    data[1] != null ? ((Number) data[1]).doubleValue() : 0.0,
                    data[2] != null ? ((Number) data[2]).doubleValue() : 0.0
            );
        }
        return null;
    }
}