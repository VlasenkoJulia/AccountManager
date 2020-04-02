package account_manager.web.controllers;


import account_manager.web.WebConfiguration;
import account_manager.repository.entity.Client;
import account_manager.service.ClientService;
import account_manager.web.controller.ClientController;
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

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ClientControllerTest.TestConfiguration.class})
@WebAppConfiguration
public class ClientControllerTest {
    private MockMvc mockMvc;

    @Autowired
    private ClientService clientService;
    @Autowired
    private WebApplicationContext webAppContext;

    @Before
    public void setup() {
        reset(clientService);
        mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
    }

    private final String EXCEPTION_MESSAGE = "Exception message";
    private final String ERROR_DTO_JSON = "{\n"
            + "  \"message\": \"Exception message\",\n"
            + "  \"type\": \"INVALID\"\n"
            + "}";

    private Client clientWithNotNullId = new Client(1, "John", "Doe");
    private final String CLIENT_WITH_NOT_NULL_ID_JSON = "{\n"
            + "  \"id\": 1,\n"
            + "  \"lastName\": \"John\",\n"
            + "  \"firstName\": \"Doe\"\n"
            + "}";

    private Client clientWithNullId = new Client(null, "John", "Doe");
    private final String CLIENT_WITH_NULL_ID_JSON = "{\n"
            + "  \"id\": null,\n"
            + "  \"lastName\": \"John\",\n"
            + "  \"firstName\": \"Doe\"\n"
            + "}";


    @Test
    public void getClientById_ClientNotFound_ShouldReturnErrorDto() throws Exception {
        when(clientService.getById(1)).thenThrow(new InputParameterValidationException(EXCEPTION_MESSAGE));
        mockMvc.perform(get("/client?clientId=1"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(ERROR_DTO_JSON));
    }

    @Test
    public void getClientById_ClientFound_ShouldReturnFoundCard() throws Exception {
        when(clientService.getById(1)).thenReturn(clientWithNotNullId);
        mockMvc.perform(get("/client?clientId=1"))
                .andExpect(status().isOk())
                .andExpect(content().json(CLIENT_WITH_NOT_NULL_ID_JSON));
    }

    @Test
    public void createClient_ClientIsNotValid_ShouldReturnErrorDto() throws Exception {
        when(clientService.create(clientWithNotNullId)).thenThrow(new InputParameterValidationException(EXCEPTION_MESSAGE));
        mockMvc.perform(post("/client")
                .contentType(MediaType.APPLICATION_JSON)
                .content(CLIENT_WITH_NOT_NULL_ID_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(ERROR_DTO_JSON));
    }

    @Test
    public void createClient_ClientIsValid_ShouldReturnSuccessMessage() throws Exception {
        when(clientService.create(clientWithNullId)).thenReturn("Created client #1");
        MvcResult result = mockMvc.perform(post("/client")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(CLIENT_WITH_NULL_ID_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String body = result.getResponse().getContentAsString();
        Assert.assertEquals("Created client #1", body);
    }

    @Test
    public void updateClient_ClientIsValid_ShouldReturnSuccessMessage() throws Exception {
        when(clientService.update(clientWithNotNullId)).thenReturn("Client updated successfully");
        MvcResult result = mockMvc.perform(put("/client")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(CLIENT_WITH_NOT_NULL_ID_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String body = result.getResponse().getContentAsString();
        Assert.assertEquals("Client updated successfully", body);
    }

    @Test
    public void updateClient_ClientIsNotValid_ShouldReturnErrorDto() throws Exception {
        when(clientService.update(clientWithNullId)).thenThrow(new InputParameterValidationException(EXCEPTION_MESSAGE));
        mockMvc.perform(put("/client")
                .contentType(MediaType.APPLICATION_JSON)
                .content(CLIENT_WITH_NULL_ID_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(ERROR_DTO_JSON));
    }

    @Test
    public void deleteCard_CardIsValid_ShouldReturnSuccessMessage() throws Exception {
        when(clientService.deleteById(1)).thenReturn("Deleted client #1");
        MvcResult result = mockMvc.perform(delete("/client?clientId=1"))
                .andExpect(status().isOk())
                .andReturn();
        String body = result.getResponse().getContentAsString();
        Assert.assertEquals("Deleted client #1", body);
    }

    @Configuration
    @Import(WebConfiguration.class)
    public static class TestConfiguration {
        @Bean
        public ClientService clientService() {
            return mock(ClientService.class);
        }

        @Bean
        public ClientController clientController(ClientService clientService) {
            return new ClientController(clientService);
        }

        @Bean
        public CustomExceptionHandler customExceptionHandler() {
            return new CustomExceptionHandler();
        }
    }
}
