package account_manager.currency_converter;

import account_manager.AppContext;
import account_manager.account.Account;
import com.google.gson.Gson;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;


@WebServlet("/converter")
public class CurrencyConverterServlet extends HttpServlet {

    private CurrencyConverter currencyConverter = (CurrencyConverter) AppContext.INSTANCE.getContext().getBean("currencyConverter");

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String body = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        Gson gson = new Gson();
        ConversionDto conversionDto = gson.fromJson(body, ConversionDto.class);
        if (conversionDto.getSourceAccountId() == null || conversionDto.getTargetAccountId() == null) {
            throw new IllegalStateException("Can not provide conversion operation with passed accounts");
        }
        List<Account> accounts = currencyConverter.convert(conversionDto);
        try (PrintWriter writer = response.getWriter()) {
            writer.println(gson.toJson(accounts));
        }
    }
}
