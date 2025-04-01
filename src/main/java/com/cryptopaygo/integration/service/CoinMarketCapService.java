package com.cryptopaygo.integration.service;

import org.springframework.stereotype.Service;

@Service
public class CoinMarketCapService {

    private final CoinMarketCapAPI coinMarketCapAPI;

    public CoinMarketCapService(CoinMarketCapAPI coinMarketCapAPI) {
        this.coinMarketCapAPI = coinMarketCapAPI;
    }

//    public String getCrypto(String apiKey) {
//
//        var endpoint = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest";
//
//        return coinMarketCapAPI.getResponse(endpoint, apiKey);
//    }

    public double getCurrency(String apiKey, String currencyType, String convertCoin) {

        var endpoint = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/quotes/latest?symbol=" + currencyType + "&convert=" + convertCoin;

        return coinMarketCapAPI.getResponse(endpoint, apiKey, currencyType, convertCoin);
    }
}