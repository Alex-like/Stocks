package org.example.app;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try(Scanner in = new Scanner(System.in)) {
            StocksClient client = new StocksClient();
            while (in.hasNext()) {
                String cmd = in.next();
                switch (cmd) {
                    case "add":
                        System.out.println(StocksRequests.add(in.next(), in.nextLong(), in.nextDouble()));
                        break;
                    case "get":
                        System.out.println(StocksRequests.get(in.next()));
                        break;
                    case "update":
                        System.out.println(StocksRequests.update(in.next(), in.nextDouble()));
                        break;
                    case "clear":
                        StocksRequests.clear();
                        break;
                    case "buy":
                        System.out.println(StocksRequests.buy(in.next(), in.nextLong(), in.nextDouble()));
                        break;
                    case "sell":
                        System.out.println(StocksRequests.sell(in.next(), in.nextLong(), in.nextDouble()));
                        break;
                    case "addUser":
                        System.out.println(client.addUser());
                        break;
                    case "addMoney":
                        System.out.println(client.addMoney(in.nextLong(), in.nextDouble()));
                        break;
                    case "getFreeMoney":
                        System.out.println(client.getFreeMoney(in.nextLong()));
                        break;
                    case "getAllMoney":
                        System.out.println(client.getAllMoney(in.nextLong()));
                        break;
                    case "getStocks":
                        System.out.println(client.getAllFullStockInfo(in.nextLong()));
                        break;
                    case "userBuy":
                        System.out.println(client.buy(in.nextLong(), in.next(), in.nextLong(), in.nextDouble()));
                        break;
                    case "userSell":
                        System.out.println(client.sell(in.nextLong(), in.next(), in.nextLong(), in.nextDouble()));
                        break;
                    default:
                        break;
                }
            }
        } catch (Exception ignore) {}
    }
}
