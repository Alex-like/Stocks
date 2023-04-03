package org.example.app;

import org.example.app.details.*;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.stream.DoubleStream;

public class StocksClient {
    private static final String BASE_URL = "http://localhost:8080";
    private final Map<Long, UserInfo> usersInfo = new HashMap<>();
    private final HttpClient client = HttpClient.newHttpClient();
    private final Random rand = new Random();

    public long addUser() {
        long id = rand.nextLong();
        while (usersInfo.containsKey(id))
            id = rand.nextLong();
        usersInfo.put(id, new UserInfo());
        return id;
    }

    public boolean addMoney(long userId, double diff) {
        UserInfo userInfo = usersInfo.get(userId);
        if (userInfo == null)
            return false;
        userInfo.freeMoney += diff;
        return true;
    }

    public double getFreeMoney(long userId) {
        return usersInfo.get(userId).freeMoney;
    }

    public ShortStockInfo getShortStockInfo(long userId, String company) {
        Map<String, Long> stocks = usersInfo.get(userId).stocks;
        if (stocks.containsKey(company))
            return new ShortStockInfo(company, stocks.get(company));
        return null;
    }

    public double getPrice(String company) {
        try {
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(new URI(BASE_URL + "/get?company=" + company))
                    .GET()
                    .build();
            HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
            return new JSONObject(res.body()).getDouble("price");
        } catch (Exception e) {
            return 0;
        }
    }

    private FullStockInfo getFullStockInfo(String company, long amount) {
        return new FullStockInfo(company, amount, getPrice(company));
    }

    private List<FullStockInfo> getAllFullStockInfo(UserInfo userInfo) {
        List<FullStockInfo> res = new ArrayList<>();
        for (Map.Entry<String, Long> entry : userInfo.stocks.entrySet())
            res.add(getFullStockInfo(entry.getKey(), entry.getValue()));
        res.sort(Comparator.comparing(o -> o.company));
        return res;
    }

    public List<FullStockInfo> getAllFullStockInfo(long userId) {
        UserInfo userInfo = usersInfo.get(userId);
        if (userInfo == null) return Collections.emptyList();
        return getAllFullStockInfo(userInfo);
    }

    public double getAllMoney(long userId) {
        UserInfo userInfo = usersInfo.get(userId);
        if (userInfo == null) return 0;
        return userInfo.freeMoney +
                getAllFullStockInfo(userInfo).stream()
                        .flatMapToDouble(u -> DoubleStream.of(u.amount * u.price))
                        .sum();
    }

    public boolean buy(long userId, String company, long amount, double price) {
        UserInfo userInfo = usersInfo.get(userId);
        if (userInfo == null || userInfo.freeMoney < amount * price)
            return false;
        try {
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(new URI(BASE_URL + "/buy?company=" + company + "&amount=" + amount + "&price=" + price))
                    .GET()
                    .build();
            HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
            boolean flag = Boolean.parseBoolean(res.body());
            if (flag) {
                userInfo.stocks.merge(company, amount, Long::sum);
                userInfo.freeMoney -= amount * price;
            }
            return flag;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean sell(long userId, String company, long amount, double price) {
        UserInfo userInfo = usersInfo.get(userId);
        if (userInfo == null)
            return false;
        Long stocksAmount = userInfo.stocks.get(company);
        if (stocksAmount == null || stocksAmount < amount)
            return false;
        try {
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(new URI(BASE_URL + "/sell?company=" + company + "&amount=" + amount + "&price=" + price))
                    .GET()
                    .build();
            HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
            boolean flag = Boolean.parseBoolean(res.body());
            if (flag) {
                userInfo.freeMoney += amount * price;
                Long lastStocksAmount = userInfo.stocks.get(company);
                if (lastStocksAmount == amount)
                    userInfo.stocks.remove(company);
                else
                    userInfo.stocks.put(company, lastStocksAmount - amount);
            }
            return flag;
        } catch (Exception e) {
            return false;
        }
    }
}
