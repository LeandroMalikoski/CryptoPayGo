package com.cryptopaygo.integration.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class CoinMarketCapAPI {

    // Principal função para retornar a resposta da API CoinMarketCap
    public double getResponse(String endpoint, String apiKey, String currencyType, String coin) {
        validateParameters(endpoint, apiKey);

        try {
            HttpURLConnection connection = createConnection(endpoint, apiKey);
            return processResponse(connection, currencyType, coin);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Cria a conexão HTTP e define o cabeçalho para a chave da API
    private HttpURLConnection createConnection(String endpoint, String apiKey) throws IOException {
        URI uri = URI.create(endpoint);
        HttpURLConnection connection = (HttpURLConnection) uri.toURL().openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("X-CMC_PRO_API_KEY", apiKey);
        connection.setConnectTimeout(10000);
        connection.setReadTimeout(10000);
        return connection;
    }

    //
    private double processResponse(HttpURLConnection connection, String cryptoCoin, String coin) throws IOException {
        int status = connection.getResponseCode();
        if (status == HttpURLConnection.HTTP_OK) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                var response = reader.lines().collect(Collectors.joining("\n"));

                ObjectMapper mapper = new ObjectMapper();

                var json = mapper.readTree(response);

                return json.get("data")
                        .get(cryptoCoin)
                        .get("quote")
                        .get(coin)
                        .get("price")
                        .asDouble();
            }
        } else {
            throw new RuntimeException("API Error: HTTP " + status + " - " + connection.getResponseMessage());
        }
    }

    private void validateParameters(String endpoint, String apiKey) {
        Objects.requireNonNull(endpoint, "Endpoint is null");
        Objects.requireNonNull(apiKey, "API Key is null");

        if (endpoint.isBlank()) {
            throw new IllegalArgumentException("Endpoint is null or empty");
        }
        if (apiKey.isBlank()) {
            throw new IllegalArgumentException("API Key is null or empty");
        }
    }
}