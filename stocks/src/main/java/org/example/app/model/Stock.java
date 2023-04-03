package org.example.app.model;

public class Stock {
    public final long amount;
    public final double price;

    public Stock(long amount, double price) {
        this.amount = amount;
        this.price = price;
    }

    @Override
    public String toString() {
        return "{" +
                "\"amount\":" + amount +
                ",\"price\":" + price +
                '}';
    }
}
