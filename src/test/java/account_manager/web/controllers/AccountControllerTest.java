package account_manager.web.controllers;

import account_manager.security.WebSecurityConfig;
import account_manager.service.AccountService;
import account_manager.service.ClientService;
import account_manager.service.dto.AccountDto;
import account_manager.service.dto.ClientDto;
import account_manager.web.controller.AccountController;
import account_manager.web.exception_handling.InputParameterValidationException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(controllers = AccountController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfig.class)
        })
public class AccountControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @MockBean
    private ClientService clientService;

    private final String EXCEPTION_MESSAGE = "Exception message";
    private final String ERROR_DTO_JSON = "{\n"
            + "  \"message\": \"Exception message\",\n"
            + "  \"type\": \"INVALID\"\n"
            + "}";

    private ClientDto client = new ClientDto(1, "John", "Doe");
    private AccountDto accountDtoWithNotNullId = new AccountDto(1, "111", "840", "CURRENT", 1.0, "01.01.1970", 1);
    private final String ACCOUNT_DTO_WITH_NOT_NULL_ID_JSON = "{\n"
            + "  \"id\": 1,\n"
            + "  \"number\": \"111\",\n"
            + "  \"currencyCode\": \"840\",\n"
            + "  \"type\": \"CURRENT\",\n"
            + "  \"balance\": 1.0,\n"
            + "  \"openDate\": \"01.01.1970\", \n"
            + "  \"ownerId\": 1\n"
            + "}";

    private AccountDto accountDtoWithNullId = new AccountDto(null, "111", "840", "CURRENT", 1.0, "01.01.1970", 1);
    private final String ACCOUNT_DTO_WITH_NULL_ID_JSON = "{\n"
            + "  \"id\": null,\n"
            + "  \"number\": \"111\",\n"
            + "  \"currencyCode\": \"840\",\n"
            + "  \"type\": \"CURRENT\",\n"
            + "  \"balance\": 1.0,\n"
            + "  \"openDate\": \"01.01.1970\", \n"
            + "  \"ownerId\": 1\n"
            + "}";

    @Test
    @WithMockUser
    public void getAccountById_AccountFound_ShouldReturnFoundAccount() throws Exception {
        when(accountService.getById(1)).thenReturn(accountDtoWithNotNullId);
        mockMvc.perform(get("/account?accountId=1"))
                .andExpect(status().isOk())
                .andExpect(content().json(ACCOUNT_DTO_WITH_NOT_NULL_ID_JSON));
    }

    @Test
    @WithMockUser
    public void getAccountById_AccountNotFound_ShouldReturnErrorDto() throws Exception {
        when(accountService.getById(1)).thenThrow(new InputParameterValidationException(EXCEPTION_MESSAGE));
        mockMvc.perform(get("/account?accountId=1"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(ERROR_DTO_JSON));
    }

    @Test
    @WithMockUser
    public void createAccount_InvalidAccount_ShouldReturnErrorDto() throws Exception {
        when(accountService.create(accountDtoWithNotNullId)).thenThrow(new InputParameterValidationException(EXCEPTION_MESSAGE));
        mockMvc.perform(post("/account")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(ACCOUNT_DTO_WITH_NOT_NULL_ID_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(ERROR_DTO_JSON));
    }

    @Test
    @WithMockUser
    public void createAccount_AccountIsValid_ShouldReturnSuccessMessage() throws Exception {
        when(accountService.create(accountDtoWithNullId)).thenReturn("Created account #1");
        MvcResult result = mockMvc.perform(post("/account")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(ACCOUNT_DTO_WITH_NULL_ID_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String body = result.getResponse().getContentAsString();
        Assert.assertEquals("Created account #1", body);
    }

    @Test
    @WithMockUser
    public void updateAccount_AccountIsValid_ShouldReturnSuccessMessage() throws Exception {
        when(accountService.update(accountDtoWithNotNullId)).thenReturn("Account updated successfully");
        MvcResult result = mockMvc.perform(put("/account")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(ACCOUNT_DTO_WITH_NOT_NULL_ID_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String body = result.getResponse().getContentAsString();
        Assert.assertEquals("Account updated successfully", body);
    }

    @Test
    @WithMockUser
    public void updateAccount_AccountIsInvalid_ShouldReturnErrorDto() throws Exception {
        when(accountService.update(accountDtoWithNullId)).thenThrow(new InputParameterValidationException(EXCEPTION_MESSAGE));
        mockMvc.perform(put("/account")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(ACCOUNT_DTO_WITH_NULL_ID_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(ERROR_DTO_JSON));
    }

    @Test
    @WithMockUser
    public void deleteAccount_AccountFound_ShouldReturnSuccessMessage() throws Exception {
        when(accountService.deleteById(1)).thenReturn("Deleted account #1");
        MvcResult result = mockMvc.perform(delete("/account?accountId=1")
                .with(csrf()))
                .andExpect(status().isOk())
                .andReturn();
        String body = result.getResponse().getContentAsString();
        Assert.assertEquals("Deleted account #1", body);
    }

    @Test
    @WithMockUser
    public void getByClientId_AccountsAndClientFound_ShouldRenderAccountsByClientView() throws Exception {
        List<AccountDto> accounts = Collections.singletonList(accountDtoWithNotNullId);
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
    @WithMockUser
    public void getByClientId_ClientNotFound_ShouldReturnErrorDto() throws Exception {
        when(clientService.getById(1)).thenThrow(new InputParameterValidationException(EXCEPTION_MESSAGE));
        mockMvc.perform(get("/account/get-by-client?clientId=1"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(ERROR_DTO_JSON));
    }
}
