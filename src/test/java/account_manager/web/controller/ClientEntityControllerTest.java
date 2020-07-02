package account_manager.web.controller;


import account_manager.security.WebSecurityConfig;
import account_manager.service.ClientService;
import account_manager.service.dto.ClientDto;
import account_manager.web.controller.ClientController;
import account_manager.web.exception_handling.InputParameterValidationException;
import org.elasticsearch.client.RestHighLevelClient;
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

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(controllers = ClientController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfig.class)
        })
public class ClientEntityControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClientService clientService;

    private final String EXCEPTION_MESSAGE = "Exception message";
    private final String ERROR_DTO_JSON = "{\n"
            + "  \"message\": \"Exception message\",\n"
            + "  \"type\": \"INVALID\"\n"
            + "}";

    private final ClientDto clientWithNotNullId = getClientDto(1, "John", "Doe");
    private final String CLIENT_WITH_NOT_NULL_ID_JSON = "{\n"
            + "  \"id\": 1,\n"
            + "  \"lastName\": \"Doe\",\n"
            + "  \"firstName\": \"John\"\n"
            + "}";

    private final ClientDto clientWithNullId = getClientDto(null, "John", "Doe");
    private final String CLIENT_WITH_NULL_ID_JSON = "{\n"
            + "  \"id\": null,\n"
            + "  \"lastName\": \"Doe\",\n"
            + "  \"firstName\": \"John\"\n"
            + "}";

    private final String CLIENT_DTO_ARRAY = "[\n" +
            "  {\n" +
            "    \"id\" : 1,\n" +
            "    \"lastName\": \"Doe\",\n" +
            "    \"firstName\": \"John\"\n" +
            "  }\n" +
            "]";

    @Test
    @WithMockUser
    public void getClientById_ClientNotFound_ShouldReturnErrorDto() throws Exception {
        when(clientService.getById(1)).thenThrow(new InputParameterValidationException(EXCEPTION_MESSAGE));
        mockMvc.perform(get("/client?clientId=1"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(ERROR_DTO_JSON));
    }

    @Test
    @WithMockUser
    public void getClientById_ClientFound_ShouldReturnFoundCard() throws Exception {
        when(clientService.getById(1)).thenReturn(clientWithNotNullId);
        mockMvc.perform(get("/client?clientId=1"))
                .andExpect(status().isOk())
                .andExpect(content().json(CLIENT_WITH_NOT_NULL_ID_JSON));
    }

    @Test
    @WithMockUser
    public void createClient_ClientIsNotValid_ShouldReturnErrorDto() throws Exception {
        when(clientService.create(clientWithNotNullId)).thenThrow(new InputParameterValidationException(EXCEPTION_MESSAGE));
        mockMvc.perform(post("/client")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(CLIENT_WITH_NOT_NULL_ID_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(ERROR_DTO_JSON));
    }

    @Test
    @WithMockUser
    public void createClient_ClientIsValid_ShouldReturnSuccessMessage() throws Exception {
        when(clientService.create(clientWithNullId)).thenReturn("Created client #1");
        MvcResult result = mockMvc.perform(post("/client")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(CLIENT_WITH_NULL_ID_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String body = result.getResponse().getContentAsString();
        Assert.assertEquals("Created client #1", body);
    }

    @Test
    @WithMockUser
    public void updateClient_ClientIsValid_ShouldReturnSuccessMessage() throws Exception {
        when(clientService.update(clientWithNotNullId)).thenReturn("Client updated successfully");
        MvcResult result = mockMvc.perform(put("/client")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(CLIENT_WITH_NOT_NULL_ID_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String body = result.getResponse().getContentAsString();
        Assert.assertEquals("Client updated successfully", body);
    }

    @Test
    @WithMockUser
    public void updateClient_ClientIsNotValid_ShouldReturnErrorDto() throws Exception {
        when(clientService.update(clientWithNullId)).thenThrow(new InputParameterValidationException(EXCEPTION_MESSAGE));
        mockMvc.perform(put("/client")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(CLIENT_WITH_NULL_ID_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(ERROR_DTO_JSON));
    }

    @Test
    @WithMockUser
    public void deleteClient_ClientIsValid_ShouldReturnSuccessMessage() throws Exception {
        when(clientService.deleteById(1)).thenReturn("Deleted client #1");
        MvcResult result = mockMvc.perform(delete("/client?clientId=1")
                .with(csrf()))
                .andExpect(status().isOk())
                .andReturn();
        String body = result.getResponse().getContentAsString();
        Assert.assertEquals("Deleted client #1", body);
    }

    @Test
    @WithMockUser
    public void searchClients_shouldReturnClientDtoList() throws Exception {
        List<ClientDto> clientDtos = List.of(this.clientWithNotNullId);
        when(clientService.search("Doe")).thenReturn(clientDtos);
        mockMvc.perform(get("/client/search?query=Doe"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(CLIENT_DTO_ARRAY));

    }

    private ClientDto getClientDto(Integer id, String firstName, String lastName) {
        ClientDto dto = new ClientDto();
        dto.setId(id);
        dto.setFirstName(firstName);
        dto.setLastName(lastName);
        return dto;
    }
}
