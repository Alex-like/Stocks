package org.example.app;

import org.example.app.model.Stock;

import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

public class Stocks {
    private final HashMap<String, Stock> stocks = new HashMap<>();
    private final ReentrantLock lock = new ReentrantLock();

    public boolean add(String company, long amount, double price) {
        lock.lock();
        try {
            Stock newStock = stocks.putIfAbsent(company, new Stock(amount, price));
            return newStock == null;
        } finally {
            lock.unlock();
        }
    }

    public boolean add(String company, double price) {
        return add(company, 0, price);
    }

    public Stock get(String company) {
        lock.lock();
        try {
            return stocks.get(company);
        } finally {
            lock.unlock();
        }
    }

    public void clear() {
        lock.lock();
        try {
            stocks.clear();
        } finally {
            lock.unlock();
        }
    }

    public boolean update(String company, double diff) {
        lock.lock();
        try {
            Stock curStock = stocks.get(company);
            if (curStock == null)
                return false;
            stocks.put(company, new Stock(curStock.amount, curStock.price + diff));
            return true;
        } finally {
            lock.unlock();
        }
    }

    public boolean buy(String company, long amount, double price) {
        lock.lock();
        try {
            Stock curStock = stocks.get(company);
            if (curStock == null || curStock.amount < amount || curStock.price != price)
                return false;
            stocks.put(company, new Stock(curStock.amount - amount, price));
            return true;
        } finally {
            lock.unlock();
        }
    }

    public boolean sell(String company, long amount, double price) {
        lock.lock();
        try {
            Stock curStock = stocks.get(company);
            if (curStock == null || curStock.price != price)
                return false;
            stocks.put(company, new Stock(curStock.amount + amount, price));
            return true;
        } finally {
            lock.unlock();
        }
    }
}
