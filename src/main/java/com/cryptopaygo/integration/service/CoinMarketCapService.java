package com.cryptopaygo.integration.service;

import org.springframework.stereotype.Service;

@Service
public class CoinMarketCapService {

    private final CoinMarketCapAPI coinMarketCapAPI;

    public CoinMarketCapService(CoinMarketCapAPI coinMarketCapAPI) {
        this.coinMarketCapAPI = coinMarketCapAPI;
    }

    public String getCurrency(String apiKey) {

        var endpoint = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest";

        return coinMarketCapAPI.getResponse(endpoint, apiKey);
    }

}