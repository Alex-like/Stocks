package org.example.app.details;

import java.util.Objects;

public class ShortStockInfo {
    public String company;
    public long amount;

    public ShortStockInfo(String company, long amount) {
        this.company = company;
        this.amount = amount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(company, amount);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ShortStockInfo info = (ShortStockInfo) obj;
        return company.equals(info.company) && amount == info.amount;
    }
}
