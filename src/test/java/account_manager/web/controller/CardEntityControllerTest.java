package account_manager.web.controller;

import account_manager.security.WebSecurityConfig;
import account_manager.service.CardService;
import account_manager.service.dto.CardDto;
import account_manager.web.controller.CardController;
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

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(controllers = CardController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfig.class)
        })
public class CardEntityControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CardService cardService;

    private final String EXCEPTION_MESSAGE = "Exception message";
    private final String ERROR_DTO_JSON = "{\n"
            + "  \"message\": \"Exception message\",\n"
            + "  \"type\": \"INVALID\"\n"
            + "}";
    private List<Integer> accountIds = List.of(1);
    private CardDto cardWithNotNullId = new CardDto(1, "111", accountIds);
    private final String CARD_WITH_NOT_NULL_ID_JSON = "{\n"
            + "  \"id\": 1,\n"
            + "  \"number\": \"111\",\n"
            + "  \"accountIds\": ["
            + "1"
            + "]\n"
            + "}";

    private CardDto cardWithNullId = new CardDto(null, "111", accountIds);
    private final String CARD_WITH_NULL_ID_JSON = "{\n"
            + "  \"id\": null,\n"
            + "  \"number\": \"111\",\n"
            + "  \"accountIds\": ["
            + "1"
            + "]\n"
            + "}";


    @Test
    @WithMockUser
    public void getCardById_CardNotFound_ShouldReturnErrorDto() throws Exception {
        when(cardService.getById(1)).thenThrow(new InputParameterValidationException(EXCEPTION_MESSAGE));
        mockMvc.perform(get("/card?cardId=1"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(ERROR_DTO_JSON));
    }

    @Test
    @WithMockUser
    public void getCardById_CardFound_ShouldReturnFoundCard() throws Exception {
        when(cardService.getById(1)).thenReturn(cardWithNotNullId);
        mockMvc.perform(get("/card?cardId=1"))
                .andExpect(status().isOk())
                .andExpect(content().json(CARD_WITH_NOT_NULL_ID_JSON));
    }

    @Test
    @WithMockUser
    public void createCard_CardIsNotValid_ShouldReturnErrorDto() throws Exception {
        when(cardService.create(cardWithNotNullId)).thenThrow(new InputParameterValidationException(EXCEPTION_MESSAGE));
        mockMvc.perform(post("/card")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(CARD_WITH_NOT_NULL_ID_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(ERROR_DTO_JSON));
    }

    @Test
    @WithMockUser
    public void createCard_CardIsValid_ShouldReturnSuccessMessage() throws Exception {
        when(cardService.create(cardWithNullId)).thenReturn("Created card #111");
        MvcResult result = mockMvc.perform(post("/card")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(CARD_WITH_NULL_ID_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String body = result.getResponse().getContentAsString();
        Assert.assertEquals("Created card #111", body);
    }

    @Test
    @WithMockUser
    public void deleteCard_CardIsValid_ShouldReturnSuccessMessage() throws Exception {
        when(cardService.deleteById(1)).thenReturn("Deleted card #1");
        MvcResult result = mockMvc.perform(delete("/card?cardId=1")
                .with(csrf()))
                .andExpect(status().isOk())
                .andReturn();
        String body = result.getResponse().getContentAsString();
        Assert.assertEquals("Deleted card #1", body);
    }
}
