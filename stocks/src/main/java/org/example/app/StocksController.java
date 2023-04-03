package org.example.app;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
public class StocksController {
    private static final Stocks stocks = new Stocks();

    @RequestMapping("/add")
    public String add(String company, long amount, double price) {
        return String.valueOf(stocks.add(company, amount, price));
    }

    @RequestMapping("/get")
    public String get(String company) {
        return Objects.toString(stocks.get(company));
    }

    @RequestMapping("/update")
    public String update(String company, double diff) {
        return String.valueOf(stocks.update(company, diff));
    }

    @RequestMapping("/clear")
    public String update() {
        stocks.clear();
        return "Cleared";
    }

    @RequestMapping("/buy")
    public String buy(String company, long amount, double price) {
        return String.valueOf(stocks.buy(company, amount, price));
    }

    @RequestMapping("/sell")
    public String sell(String company, long amount, double price) {
        return String.valueOf(stocks.sell(company, amount, price));
    }
}
