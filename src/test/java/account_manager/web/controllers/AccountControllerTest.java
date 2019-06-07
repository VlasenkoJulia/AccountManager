package account_manager.web.controllers;

import account_manager.WebConfiguration;
import account_manager.account.Account;
import account_manager.account.AccountRepository;
import account_manager.account.AccountType;
import account_manager.client.Client;
import account_manager.client.ClientRepository;
import account_manager.currency_converter.Currency;
import account_manager.web.exception_handling.CustomExceptionHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;
import java.util.Arrays;
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
    private AccountRepository accountRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private WebApplicationContext webAppContext;

    @Before
    public void setup() {
        reset(clientRepository);
        reset(accountRepository);
        mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
    }

    @Test
    public void checkIfAccountControllerExistsInWac() {
        ServletContext servletContext = webAppContext.getServletContext();
        Assert.assertNotNull(servletContext);
        Assert.assertTrue(servletContext instanceof MockServletContext);
        Assert.assertNotNull(webAppContext.getBean("accountController"));
    }

    @Test
    public void getAccountById_AccountFound_ShouldReturnFoundAccount() throws Exception {
        when(accountRepository.getById(1)).thenReturn(new Account());
        mockMvc.perform(get("/account?accountId=1"))
                .andExpect(status().isOk());
        verify(accountRepository, times(1)).getById(1);
        verifyNoMoreInteractions(accountRepository);
    }

    @Test
    public void getAccountById_AccountNotFound_ShouldReturnErrorDto() throws Exception {
        when(accountRepository.getById(1)).thenReturn(null);
        mockMvc.perform(get("/account?accountId=1"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.message")
                        .value("Account with passed ID do not exist"))
                .andExpect(jsonPath("$.type")
                        .value("INVALID"));
        verify(accountRepository, times(1)).getById(1);
        verifyNoMoreInteractions(accountRepository);
    }

    @Test
    public void createAccount_AccountIdIsNotNull_ShouldReturnErrorDto() throws Exception {
        Account account = createAccount(1);
        mockMvc.perform(post("/account")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().writeValueAsString(account)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.message")
                        .value("Can not provide insert operation with passed account"))
                .andExpect(jsonPath("$.type")
                        .value("INVALID"));
    }

    @Test
    public void createAccount_AccountCurrencyIsNull_ShouldReturnErrorDto() throws Exception {
        Account account = new Account();
        mockMvc.perform(post("/account")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().writeValueAsString(account)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.message").value("Missed account currency"))
                .andExpect(jsonPath("$.type").value("INVALID"));
    }

    @Test
    public void createAccount_AccountCurrencyCodeIsEmptyString_ShouldReturnErrorDto() throws Exception {
        Account account = createAccount(null, AccountType.CURRENT, "");
        mockMvc.perform(post("/account")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().writeValueAsString(account)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.message").value("Missed account currency"))
                .andExpect(jsonPath("$.type").value("INVALID"));
    }

    @Test
    public void createAccount_AccountTypeIsNull_ShouldReturnErrorDto() throws Exception {
        Account account = createAccount(null, null, "980");
        mockMvc.perform(post("/account")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().writeValueAsString(account)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.message").value("Missed account type"))
                .andExpect(jsonPath("$.type").value("INVALID"));
    }

    @Test
    public void createAccount_AccountIsValid_ShouldReturnSuccessMessage() throws Exception {
        Account account = createAccount(null, AccountType.DEPOSIT, "980");
        when(accountRepository.create(account)).thenReturn(createAccount(1));
        MvcResult result = mockMvc.perform(post("/account")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().writeValueAsString(account)))
                .andExpect(status().isOk())
                .andReturn();
        String body = result.getResponse().getContentAsString();
        Assert.assertEquals("Created account #1", body);
        verify(accountRepository, times(1)).create(account);
        verifyNoMoreInteractions(accountRepository);
    }

    @Test
    public void updateAccount_AccountIsValid_ShouldReturnSuccessMessage() throws Exception {
        Account account = createAccount(1);
        MvcResult result = mockMvc.perform(put("/account")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().writeValueAsString(account)))
                .andExpect(status().isOk())
                .andReturn();
        String body = result.getResponse().getContentAsString();
        Assert.assertEquals("Account updated successfully", body);
        verify(accountRepository, times(1)).update(account);
        verifyNoMoreInteractions(accountRepository);
    }

    @Test
    public void updateAccount_AccountIdIsNull_ShouldReturnErrorDto() throws Exception {
        Account account = createAccount(null);
        mockMvc.perform(put("/account")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().writeValueAsString(account)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.message")
                        .value("Can not provide update operation with passed account"))
                .andExpect(jsonPath("$.type")
                        .value("INVALID"));
    }

    @Test
    public void deleteAccount_AccountFound_ShouldReturnSuccessMessage() throws Exception {
        doNothing().when(accountRepository).deleteById(1);
        MvcResult result = mockMvc.perform(delete("/account?accountId=1"))
                .andExpect(status().isOk())
                .andReturn();
        String body = result.getResponse().getContentAsString();
        Assert.assertEquals("Deleted account #1", body);
        verify(accountRepository, times(1)).deleteById(1);
        verifyNoMoreInteractions(accountRepository);
    }

    @Test
    public void getByClientId_AccountsAndClientFound_ShouldRenderAccountsByClientView() throws Exception {
        List<Account> accounts = Arrays.asList(Mockito.mock(Account.class), Mockito.mock(Account.class));
        Client client = Mockito.mock(Client.class);
        when(clientRepository.getById(1)).thenReturn(client);
        when(accountRepository.getByClientId(1)).thenReturn(accounts);
        mockMvc.perform(get("/account/get-by-client?clientId=1"))
                .andExpect(status().isOk())
                .andExpect(view().name("accountsByClient"))
                .andExpect(forwardedUrl("/WEB-INF/view/accountsByClient.jsp"))
                .andExpect(model().attributeExists("client"))
                .andExpect(model().attributeExists("accounts"))
                .andExpect(model().attribute("accounts", accounts))
                .andExpect(model().attribute("client", client));
        verify(accountRepository, times(1)).getByClientId(1);
        verify(clientRepository, times(1)).getById(1);
        verifyNoMoreInteractions(accountRepository);
        verifyNoMoreInteractions(clientRepository);
    }

    @Test
    public void getByClientId_ClientIsNull_ShouldReturnErrorDto() throws Exception {
        when(clientRepository.getById(1)).thenReturn(null);
        mockMvc.perform(get("/account/get-by-client?clientId=1"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.message")
                        .value("Client with passed ID do not exist"))
                .andExpect(jsonPath("$.type")
                        .value("INVALID"));
        verify(clientRepository, times(1)).getById(1);
        verifyNoMoreInteractions(clientRepository);
    }

    private Account createAccount(Integer id, AccountType type, String currencyCode) {
        Account account = new Account();
        account.setId(id);
        account.setType(type);
        account.setCurrency(createCurrency(currencyCode));
        return account;
    }

    private Account createAccount(Integer id) {
        Account account = new Account();
        account.setId(id);
        return account;
    }

    private Currency createCurrency(String code) {
        Currency currency = new Currency();
        currency.setCode(code);
        return currency;
    }

    @Configuration
    @Import(WebConfiguration.class)
    public static class TestConfiguration {
        @Bean
        public AccountRepository accountRepository() {
            return mock(AccountRepository.class);
        }

        @Bean
        public ClientRepository clientRepository() {
            return mock(ClientRepository.class);
        }

        @Bean
        public AccountController accountController(AccountRepository accountRepository, ClientRepository clientRepository) {
            return new AccountController(accountRepository, clientRepository);
        }

        @Bean
        public CustomExceptionHandler customExceptionHandler() {
            return new CustomExceptionHandler();
        }
    }
}