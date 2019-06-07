package account_manager.web.controllers;


import account_manager.WebConfiguration;
import account_manager.client.Client;
import account_manager.client.ClientRepository;
import account_manager.web.exception_handling.CustomExceptionHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ClientControllerTest.TestConfiguration.class})
@WebAppConfiguration
public class ClientControllerTest {
    private MockMvc mockMvc;

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private WebApplicationContext webAppContext;

    @Before
    public void setup() {
        reset(clientRepository);
        mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
    }

    @Test
    public void getClientById_ClientNotFound_ShouldReturnErrorDto() throws Exception {
        when(clientRepository.getById(1)).thenReturn(null);
        mockMvc.perform(get("/client?clientId=1"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.message")
                        .value("Client with passed ID do not exist"))
                .andExpect(jsonPath("$.type")
                        .value("INVALID"));
        verify(clientRepository, times(1)).getById(1);
        verifyNoMoreInteractions(clientRepository);
    }

    @Test
    public void getClientById_ClientFound_ShouldReturnFoundCard() throws Exception {
        when(clientRepository.getById(1)).thenReturn(new Client());
        mockMvc.perform(get("/client?clientId=1"))
                .andExpect(status().isOk());
        verify(clientRepository, times(1)).getById(1);
        verifyNoMoreInteractions(clientRepository);
    }

    @Test
    public void createClient_ClientIdIsNotNull_ShouldReturnErrorDto() throws Exception {
        Client client = createClient(1);
        mockMvc.perform(post("/client")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().writeValueAsString(client)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.message")
                        .value("Can not provide insert operation with passed client"))
                .andExpect(jsonPath("$.type")
                        .value("INVALID"));
    }

    @Test
    public void createClient_ClientIsValid_ShouldReturnSuccessMessage() throws Exception {
        Client client = createClient(null);
        when(clientRepository.create(client)).thenReturn(createClient(1));
        MvcResult result = mockMvc.perform(post("/client")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().writeValueAsString(client)))
                .andExpect(status().isOk())
                .andReturn();
        String body = result.getResponse().getContentAsString();
        Assert.assertEquals("Created client #1", body);
        verify(clientRepository, times(1)).create(client);
        verifyNoMoreInteractions(clientRepository);
    }


    @Test
    public void updateClient_ClientIsValid_ShouldReturnSuccessMessage() throws Exception {
        Client client = createClient(1);
        MvcResult result = mockMvc.perform(put("/client")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().writeValueAsString(client)))
                .andExpect(status().isOk())
                .andReturn();
        String body = result.getResponse().getContentAsString();
        Assert.assertEquals("Client updated successfully", body);
        verify(clientRepository, times(1)).update(client);
        verifyNoMoreInteractions(clientRepository);
    }

    @Test
    public void updateClient_ClientIdIsNull_ShouldReturnErrorDto() throws Exception {
        Client client = createClient(null);
        mockMvc.perform(put("/client")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().writeValueAsString(client)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.message")
                        .value("Can not provide update operation with passed client"))
                .andExpect(jsonPath("$.type")
                        .value("INVALID"));
    }

    @Test
    public void deleteCard_CardIsValid_ShouldReturnSuccessMessage() throws Exception {
        doNothing().when(clientRepository).deleteById(1);
        MvcResult result = mockMvc.perform(delete("/client?clientId=1"))
                .andExpect(status().isOk())
                .andReturn();
        String body = result.getResponse().getContentAsString();
        Assert.assertEquals("Deleted client #1", body);
        verify(clientRepository, times(1)).deleteById(1);
        verifyNoMoreInteractions(clientRepository);
    }

    private Client createClient(Integer id) {
        Client client = new Client();
        client.setId(id);
        return client;
    }


    @Configuration
    @Import(WebConfiguration.class)
    public static class TestConfiguration {
        @Bean
        public ClientRepository clientRepository() {
            return mock(ClientRepository.class);
        }

        @Bean
        public ClientController clientController(ClientRepository clientRepository) {
            return new ClientController(clientRepository);
        }

        @Bean
        public CustomExceptionHandler customExceptionHandler() {
            return new CustomExceptionHandler();
        }
    }
}
