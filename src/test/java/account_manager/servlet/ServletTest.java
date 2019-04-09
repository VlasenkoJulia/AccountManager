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
import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;

class ServletTest {

    @Test

    public void testServletMethods() throws IOException, InterruptedException {
     sendGet("http://localhost:8080/account-manager-app/account/get-by-client?clientId=4", "5");
    }

    public static void sendGet(String urlName, String id) throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(urlName))
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