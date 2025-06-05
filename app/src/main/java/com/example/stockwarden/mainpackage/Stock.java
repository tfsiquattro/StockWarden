package com.example.stockwarden.mainpackage;

import com.google.gson.annotations.SerializedName;

// Класс для представления данных об акции
public class Stock {

    // Название акции. Используется аннотация @SerializedName для указания соответствия с JSON полем
    @SerializedName("01. symbol")
    private String name;

    // Цена акции. Используется аннотация @SerializedName для указания соответствия с JSON полем
    @SerializedName("05. price")
    private String price;

    // Конструктор класса
    public Stock(String name, String price) {
        this.name = name;
        this.price = price;
    }

    // Геттер для получения названия акции
    public String getName() {
        return name;
    }

    // Сеттер для установки названия акции
    public void setName(String name) {
        this.name = name;
    }

    // Геттер для получения цены акции
    public String getPrice() {
        return price;
    }

    // Сеттер для установки цены акции
    public void setPrice(String price) {
        this.price = price;
    }
}
