package account_manager.web.controllers;

import account_manager.web.WebConfiguration;
import account_manager.repository.entity.Account;
import account_manager.service.AccountService;
import account_manager.repository.enums.AccountType;
import account_manager.repository.entity.Client;
import account_manager.service.ClientService;
import account_manager.repository.entity.Currency;
import account_manager.web.controller.AccountController;
import account_manager.web.exception_handling.CustomExceptionHandler;
import account_manager.web.exception_handling.InputParameterValidationException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AccountControllerTest.TestConfiguration.class})
@WebAppConfiguration
public class AccountControllerTest {
    private MockMvc mockMvc;

    @Autowired
    private AccountService accountService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private WebApplicationContext webAppContext;

    @Before
    public void setup() {
        reset(accountService);
        reset(clientService);
        mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
    }

    private final String EXCEPTION_MESSAGE = "Exception message";
    private final String ERROR_DTO_JSON = "{\n"
            + "  \"message\": \"Exception message\",\n"
            + "  \"type\": \"INVALID\"\n"
            + "}";

    private Client client = new Client(1, "John", "Doe");
    private Currency currency = new Currency("840", 1.0, "US Dollar", "USD");
    private Account accountWithNotNullId = new Account(1, "111", currency, AccountType.CURRENT, 1.0, new Date(1L), client, Collections.emptySet());
    private final String ACCOUNT_WITH_NOT_NULL_ID_JSON = "{\n"
            + "  \"id\": 1,\n"
            + "  \"number\": \"111\",\n"
            + "  \"currency\": {\n"
            + "    \"code\": \"840\",\n"
            + "    \"rate\": 1.0,\n"
            + "    \"name\": \"US Dollar\",\n"
            + "    \"iso\": \"USD\"\n"
            + "  },\n"
            + "  \"type\": \"CURRENT\",\n"
            + "  \"balance\": 1.0,\n"
            + "  \"openDate\": \"01.01.1970\",\n"
            + "  \"client\": {\n"
            + "    \"id\": 1,\n"
            + "    \"lastName\": \"John\",\n"
            + "    \"firstName\": \"Doe\"\n"
            + "  },\n"
            + "  \"cards\": []\n"
            + "}";

    private Account accountWithNullId = new Account(null, "111", currency, AccountType.CURRENT, 1.0, new Date(1L), client, Collections.emptySet());
    private final String ACCOUNT_WITH_NULL_ID_JSON = "{\n"
            + "  \"id\": null,\n"
            + "  \"number\": \"111\",\n"
            + "  \"currency\": {\n"
            + "    \"code\": \"980\",\n"
            + "    \"rate\": 1.0,\n"
            + "    \"name\": \"US Dollar\",\n"
            + "    \"iso\": \"USD\"\n"
            + "  },\n"
            + "  \"type\": \"CURRENT\",\n"
            + "  \"balance\": 1.0,\n"
            + "  \"openDate\": \"01.01.1970\",\n"
            + "  \"client\": {\n"
            + "    \"id\": 1,\n"
            + "    \"lastName\": \"John\",\n"
            + "    \"firstName\": \"Doe\"\n"
            + "  },\n"
            + "  \"cards\": []\n"
            + "}";

    @Test
    public void getAccountById_AccountFound_ShouldReturnFoundAccount() throws Exception {
        when(accountService.getById(1)).thenReturn(accountWithNotNullId);
        mockMvc.perform(get("/account?accountId=1"))
                .andExpect(status().isOk())
                .andExpect(content().json(ACCOUNT_WITH_NOT_NULL_ID_JSON));
    }

    @Test
    public void getAccountById_AccountNotFound_ShouldReturnErrorDto() throws Exception {
        when(accountService.getById(1)).thenThrow(new InputParameterValidationException(EXCEPTION_MESSAGE));
        mockMvc.perform(get("/account?accountId=1"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(ERROR_DTO_JSON));
    }

    @Test
    public void createAccount_InvalidAccount_ShouldReturnErrorDto() throws Exception {
        when(accountService.create(accountWithNotNullId)).thenThrow(new InputParameterValidationException(EXCEPTION_MESSAGE));
        mockMvc.perform(post("/account")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(ACCOUNT_WITH_NOT_NULL_ID_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(ERROR_DTO_JSON));
    }

    @Test
    public void createAccount_AccountIsValid_ShouldReturnSuccessMessage() throws Exception {
        when(accountService.create(accountWithNullId)).thenReturn("Created account #1");
        MvcResult result = mockMvc.perform(post("/account")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(ACCOUNT_WITH_NULL_ID_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String body = result.getResponse().getContentAsString();
        Assert.assertEquals("Created account #1", body);
    }

    @Test
    public void updateAccount_AccountIsValid_ShouldReturnSuccessMessage() throws Exception {
        when(accountService.update(accountWithNotNullId)).thenReturn("Account updated successfully");
        MvcResult result = mockMvc.perform(put("/account")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(ACCOUNT_WITH_NOT_NULL_ID_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String body = result.getResponse().getContentAsString();
        Assert.assertEquals("Account updated successfully", body);
    }

    @Test
    public void updateAccount_AccountIsInvalid_ShouldReturnErrorDto() throws Exception {
        when(accountService.update(accountWithNullId)).thenThrow(new InputParameterValidationException(EXCEPTION_MESSAGE));
        mockMvc.perform(put("/account")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ACCOUNT_WITH_NULL_ID_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(ERROR_DTO_JSON));
    }

    @Test
    public void deleteAccount_AccountFound_ShouldReturnSuccessMessage() throws Exception {
        when(accountService.deleteById(1)).thenReturn("Deleted account #1");
        MvcResult result = mockMvc.perform(delete("/account?accountId=1"))
                .andExpect(status().isOk())
                .andReturn();
        String body = result.getResponse().getContentAsString();
        Assert.assertEquals("Deleted account #1", body);
    }

    @Test
    public void getByClientId_AccountsAndClientFound_ShouldRenderAccountsByClientView() throws Exception {
        List<Account> accounts = Collections.singletonList(accountWithNotNullId);
        when(clientService.getById(1)).thenReturn(client);
        when(accountService.getByClientId(1)).thenReturn(accounts);
        mockMvc.perform(get("/account/get-by-client?clientId=1"))
                .andExpect(status().isOk())
                .andExpect(view().name("accountsByClient"))
                .andExpect(forwardedUrl("/WEB-INF/view/accountsByClient.jsp"))
                .andExpect(model().attributeExists("client"))
                .andExpect(model().attributeExists("accounts"))
                .andExpect(model().attribute("accounts", accounts))
                .andExpect(model().attribute("client", client));
    }

    @Test
    public void getByClientId_ClientNotFound_ShouldReturnErrorDto() throws Exception {
        when(clientService.getById(1)).thenThrow(new InputParameterValidationException(EXCEPTION_MESSAGE));
        mockMvc.perform(get("/account/get-by-client?clientId=1"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(ERROR_DTO_JSON));
    }

    @Configuration
    @Import(WebConfiguration.class)
    public static class TestConfiguration {
        @Bean
        public AccountService accountService() {
            return mock(AccountService.class);
        }

        @Bean
        public ClientService clientService() {
            return mock(ClientService.class);
        }

        @Bean
        public AccountController accountController(ClientService clientService, AccountService accountService) {
            return new AccountController(accountService, clientService);
        }

        @Bean
        public CustomExceptionHandler customExceptionHandler() {
            return new CustomExceptionHandler();
        }
    }
}
