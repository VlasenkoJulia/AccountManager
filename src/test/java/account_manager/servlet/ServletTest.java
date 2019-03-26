package account_manager.servlet;

import account_manager.AppContext;
import account_manager.account.Account;
import account_manager.account.AccountRepository;
import account_manager.account.AccountType;
import account_manager.card.Card;
import account_manager.card.CardRepository;
import account_manager.client.Client;
import account_manager.client.ClientRepository;
import account_manager.currency_converter.ConversionDto;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Date;


class ServletTest {

    @Test
    public void testServletMethods() {
//        ConversionDto conversionDto = new ConversionDto(10, 5, 1000);
//        Gson gson = new Gson();
//        String json = gson.toJson(conversionDto);
//        System.out.println();
//        sendPost("http://localhost:8080/account-manager-app/converter", json);
        Account account = new Account("263512121212121", "643", AccountType.DEPOSIT, 4);
        Gson gson = new Gson();
//        String s = gson.toJson(account);
        AccountRepository accountRepository = (AccountRepository) AppContext.INSTANCE.getContext().getBean("accountRepository");
        accountRepository.create(account);
//        sendPost("http://localhost:8080/account-manager-app/account",s);
        ClientRepository clientRepository = (ClientRepository) AppContext.INSTANCE.getContext().getBean("clientRepository");
        CardRepository cardRepository = (CardRepository) AppContext.INSTANCE.getContext().getBean("cardRepository");

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
                .uri(URI.create(urlName + "?accountId=" + id))
                .DELETE()
                .build();
        HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        System.out.println(httpResponse.body());
    }


}