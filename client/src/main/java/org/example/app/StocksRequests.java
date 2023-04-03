package org.example.app;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class StocksRequests {
    private static final String BASE_URL = "http://localhost:8080";
    private static final HttpClient client = HttpClient.newHttpClient();

    public static boolean add(String company, long amount, double price) {
        try {
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(new URI(BASE_URL + "/add?company=" + company + "&amount=" + amount + "&price=" + price))
                    .GET()
                    .build();
            HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
            return Boolean.parseBoolean(res.body());
        } catch (Exception e) {
            return false;
        }
    }

    public static String get(String company) {
        try {
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(new URI(BASE_URL + "/get?company=" + company))
                    .GET()
                    .build();
            HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
            return res.body();
        } catch (Exception e) {
            System.err.println(e);
            return "";
        }
    }

    public static boolean update(String company, double diff) {
        try {
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(new URI(BASE_URL + "/update?company=" + company + "&diff=" + diff))
                    .GET()
                    .build();
            HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
            return Boolean.parseBoolean(res.body());
        } catch (Exception e) {
            return false;
        }
    }

    public static void clear() {
        try {
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(new URI(BASE_URL + "/clear"))
                    .GET()
                    .build();
            HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
            res.body();
        } catch (Exception ignore) {}
    }

    public static boolean buy(String company, long amount, double price) {
        try {
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(new URI(BASE_URL + "/buy?company=" + company + "&amount=" + amount + "&price=" + price))
                    .GET()
                    .build();
            HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
            return Boolean.parseBoolean(res.body());
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean sell(String company, long amount, double price) {
        try {
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(new URI(BASE_URL + "/sell?company=" + company + "&amount=" + amount + "&price=" + price))
                    .GET()
                    .build();
            HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
            return Boolean.parseBoolean(res.body());
        } catch (Exception e) {
            return false;
        }
    }
}
