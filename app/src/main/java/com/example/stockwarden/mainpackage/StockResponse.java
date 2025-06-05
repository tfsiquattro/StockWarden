package com.example.stockwarden.mainpackage;

import com.google.gson.annotations.SerializedName;

// Класс для представления ответа от API, содержащего данные об акции
public class StockResponse {

    // Поле для хранения данных об акции
    // Используется аннотация @SerializedName для указания соответствия с JSON полем
    @SerializedName("Global Quote")
    private Stock stock;

    // Конструктор класса
    public StockResponse(Stock stock) {
        this.stock = stock;
    }

    // Геттер для получения данных об акции
    public Stock getStock() {
        return stock;
    }

    // Сеттер для установки данных об акции
    public void setStock(Stock stock) {
        this.stock = stock;
    }
}
