package account_manager.servlet;

import account_manager.account.Account;
import account_manager.account.AccountType;
import account_manager.card.Card;
import account_manager.client.Client;
import account_manager.currency_converter.ConversionDto;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Date;
import java.util.HashSet;

class ServletTest {

    @Test

    public void testServletMethods() throws IOException, InterruptedException {
//        ConversionDto conversionDto = new ConversionDto(11, 15, 250000);
//        Gson gson = new Gson();
//        String json = gson.toJson(conversionDto);
//        sendPost("http://localhost:8080/account-manager-app/converter", json);
        HashSet<Integer> set = new HashSet<>();
        set.add(5);
        set.add(7);
        set.add(13);
        Card card = new Card("1598-5874-3654-1249", set);
        Gson gson = new Gson();
        String s = gson.toJson(card);
        sendPost("http://localhost:8080/account-manager-app/card", s);
    }

    public static void sendGet(String urlName, String id) throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(urlName + "?accountId=" + id))
                .GET()
                .build();
        HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        System.out.println(httpResponse.body());
    }

    public static void sendPost(String urlName, String message) throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(urlName))
                .POST(HttpRequest.BodyPublishers.ofString(message))
                .build();
        HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        System.out.println(httpResponse.body());
    }

    public static void sendPut(String urlName, String message) throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(urlName))
                .PUT(HttpRequest.BodyPublishers.ofString(message))
                .build();
        HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        System.out.println(httpResponse.body());
    }

    public static void sendDelete(String urlName, String id) throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(urlName + "?clientId=" + id))
                .DELETE()
                .build();
        HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        System.out.println(httpResponse.body());
    }


}