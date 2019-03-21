package account_manager.card;


import com.google.gson.Gson;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.stream.Collectors;

@WebServlet("/card")
public class CardServlet extends HttpServlet {
   private CardRepository cardRepository = new CardRepository();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Card card;
        try {
            card = cardRepository.getById(Integer.valueOf(request.getParameter("cardId")));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Passed card_id is not valid");
        }
        try (PrintWriter writer = response.getWriter()) {
            Gson gson = new Gson();
            String json = gson.toJson(card);
            writer.println(json);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String body = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        Gson gson = new Gson();
        Card card = gson.fromJson(body, Card.class);
        if (card.getAccountsId().isEmpty()) {
            throw new IllegalArgumentException("Card can not be created without reference to the account(s)");
        }
        cardRepository.create(card);
        try (PrintWriter writer = response.getWriter()) {
            writer.println("Created card #" + card.getNumber());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id;
        try {
            id = Integer.parseInt(request.getParameter("CardId"));
            cardRepository.deleteById(id);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Passed card_id is not valid");
        }
        try (PrintWriter writer = response.getWriter()) {
            writer.println("Deleted card #" + id);
        }
    }
}
