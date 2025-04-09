package com.cryptopaygo.integration.controller;

import com.cryptopaygo.integration.service.CoinMarketCapService;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("cryptos")
public class CoinMarketCapController {

//    private final CoinMarketCapService coinMarketCapService;
//    private final Environment environment;
//
//    public CoinMarketCapController(CoinMarketCapService coinMarketCapService, Environment environment) {
//        this.coinMarketCapService = coinMarketCapService;
//        this.environment = environment;
//    }

    // Função que exige o envio da Chave da API no header do insomnia, postman, etc.
//    @GetMapping("/cr")
//    public String getCryptos(@RequestHeader("X-CMC_PRO_API_KEY") String apiKey) {
//        return coinMarketCapService.getCurrency(apiKey);
//    }

    // Função que NÂO exige o envio da Chave da API no header do insomnia, postman, etc.
    // A Chave estará no application.properties para facilitar teste e desenvolvimento
//    @GetMapping("/cr")
//    public String getCryptos() {
//        String apiKey = environment.getProperty("coinmarketcap.apikey");
//        return coinMarketCapService.getCrypto(apiKey);
//    }
}
