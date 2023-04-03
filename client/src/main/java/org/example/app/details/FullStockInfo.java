package org.example.app.details;

import java.util.Objects;

public class FullStockInfo {
    public String company;
    public long amount;
    public double price;

    public FullStockInfo(String company, long amount, double price) {
        this.company = company;
        this.amount = amount;
        this.price = price;
    }

    @Override
    public int hashCode() {
        return Objects.hash(company, amount, price);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        FullStockInfo info = (FullStockInfo) obj;
        return company.equals(info.company) && amount == info.amount && Double.compare(info.price, price) == 0;
    }
}
