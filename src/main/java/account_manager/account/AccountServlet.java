package account_manager.account;

import com.google.gson.Gson;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.stream.Collectors;


@WebServlet("/account")
public class AccountServlet extends HttpServlet {
    private AccountRepository accountRepository = new AccountRepository();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Account account;
        try {
            account = accountRepository.getById(Integer.valueOf(request.getParameter("accountId")));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Passed account_id is not valid");
        }
        try (PrintWriter writer = response.getWriter()) {
            Gson gson = new Gson();
            String json = gson.toJson(account);
            writer.println(json);
        }
    }

    private Account getAccountFromRequest(HttpServletRequest request) throws IOException {
        String body = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        Gson gson = new Gson();
        return gson.fromJson(body, Account.class);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Account account = getAccountFromRequest(request);
        if (account.getId() != null) {
            throw new IllegalStateException("Can not provide insert operation with passed account");
        }
        Account openAccount = accountRepository.create(account);
        try (PrintWriter writer = response.getWriter()) {
            writer.println("Created account #" + openAccount.getId());
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Account account = getAccountFromRequest(request);
        if (account.getId() == null) {
            throw new IllegalStateException("Can not provide update operation with passed account");
        }
        accountRepository.update(account);
        try (PrintWriter writer = response.getWriter()) {
            writer.println("Account updated successfully");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id;
        try {
            id = Integer.parseInt(request.getParameter("accountId"));
            accountRepository.deleteById(id);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Passed account_id is not valid");
        }
        try (PrintWriter writer = response.getWriter()) {
            writer.println("Deleted account #" + id);
        }
    }
}
