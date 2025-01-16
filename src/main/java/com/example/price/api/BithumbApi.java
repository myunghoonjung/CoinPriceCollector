package com.example.price.api;

import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class BithumbApi implements ExchangeApi {
    private static final String SERVER_URL = "https://api.bithumb.com/public/ticker/";

    @Override
    public BigDecimal getPrice(String symbol) {
        String url = SERVER_URL + symbol.toUpperCase() + "_KRW";

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            JsonObject json = JsonParser.parseString(response.body()).getAsJsonObject();
            String closingPrice = json.getAsJsonObject("data").get("closing_price").getAsString();
            return new BigDecimal(closingPrice);
        } catch (Exception e) {
            e.printStackTrace();
            return BigDecimal.ZERO;
        }
    }
}

