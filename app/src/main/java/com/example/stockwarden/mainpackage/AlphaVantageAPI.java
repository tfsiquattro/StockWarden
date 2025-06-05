package com.example.stockwarden.mainpackage;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

// Интерфейс для работы с API Alpha Vantage
public interface AlphaVantageAPI {

    // Базовый URL для API
    String BASE_URL = "https://www.alphavantage.co/";

    // Ваш API ключ
    String API_KEY = "641N4WPUK48735GE";

    // Метод для получения цен на акции
    @GET("query")
    Call<StockResponse> getStockPrices(
            @Query("function") String function, // Функция API, по умолчанию GLOBAL_QUOTE
            @Query("symbol") String symbol,     // Символ акции, например, IBM для International Business Machines
            @Query("apikey") String apiKey     // API ключ
    );

    // Метод по умолчанию для удобства вызова с заранее установленной функцией
    default Call<StockResponse> getStockPrices(String symbol) {
        return getStockPrices("GLOBAL_QUOTE", symbol, API_KEY);
    }
}
