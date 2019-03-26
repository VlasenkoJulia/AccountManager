package account_manager.client;

import account_manager.AppContext;
import com.google.gson.Gson;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.stream.Collectors;


@WebServlet("/client")
public class ClientServlet extends HttpServlet {
    private ClientRepository clientRepository = (ClientRepository) AppContext.INSTANCE.getContext().getBean("clientRepository");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Client client;
        try {
            client = clientRepository.getById(Integer.valueOf(request.getParameter("client")));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Passed client_id is not valid");
        }
        try (PrintWriter writer = response.getWriter()) {
            Gson gson = new Gson();
            String json = gson.toJson(client);
            writer.println(json);
        }
    }

    private Client getClientFromRequest(HttpServletRequest request) throws IOException {
        String body = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        Gson gson = new Gson();
        return gson.fromJson(body, Client.class);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Client client = getClientFromRequest(request);
        if (client.getId() != null) {
            throw new IllegalStateException("Can not provide insert operation with passed client");
        }
        Client createdClient = clientRepository.create(client);
        try (PrintWriter writer = response.getWriter()) {
            writer.println("Created client #" + createdClient.getId());
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Client client = getClientFromRequest(request);
        if (client.getId() == null) {
            throw new IllegalStateException("Can not provide update operation with passed client");
        }
        clientRepository.update(client);
        try (PrintWriter writer = response.getWriter()) {
            writer.println("Client updated successfully");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id;
        try {
            id = Integer.parseInt(request.getParameter("clientId"));
            clientRepository.deleteById(id);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Passed client_id is not valid");
        }
        try (PrintWriter writer = response.getWriter()) {
            writer.println("Deleted client #" + id);
        }
    }
}
