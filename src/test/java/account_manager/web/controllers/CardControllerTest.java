package account_manager.web.controllers;

import account_manager.web.WebConfiguration;
import account_manager.repository.account.Account;
import account_manager.repository.card.Card;
import account_manager.service.CardService;
import account_manager.web.controller.CardController;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CardControllerTest.TestConfiguration.class})
@WebAppConfiguration
public class CardControllerTest {
    private MockMvc mockMvc;

    @Autowired
    private CardService cardService;
    @Autowired
    private WebApplicationContext webAppContext;

    @Before
    public void setup() {
        reset(cardService);
        mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
    }

    private final String EXCEPTION_MESSAGE = "Exception message";
    private final String ERROR_DTO_JSON = "{\n"
            + "  \"message\": \"Exception message\",\n"
            + "  \"type\": \"INVALID\"\n"
            + "}";
    private List<Account> accounts = new ArrayList<>(Collections.singletonList(new Account()));
    private Card cardWithNotNullId = new Card(1, "111", accounts);
    private final String CARD_WITH_NOT_NULL_ID_JSON = "{\n"
            + "  \"id\": 1,\n"
            + "  \"number\": \"111\",\n"
            + "  \"accounts\": [\n"
            + "    {}\n"
            + "  ]\n"
            + "}";

    private Card cardWithNullId = new Card(null, "111", accounts);
    private final String CARD_WITH_NULL_ID_JSON = "{\n"
            + "  \"id\": null,\n"
            + "  \"number\": \"111\",\n"
            + "  \"accounts\": [\n"
            + "    {}\n"
            + "  ]\n"
            + "}";


    @Test
    public void getCardById_CardNotFound_ShouldReturnErrorDto() throws Exception {
        when(cardService.getById(1)).thenThrow(new InputParameterValidationException(EXCEPTION_MESSAGE));
        mockMvc.perform(get("/card?cardId=1"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(ERROR_DTO_JSON));
    }

    @Test
    public void getCardById_CardFound_ShouldReturnFoundCard() throws Exception {
        when(cardService.getById(1)).thenReturn(cardWithNotNullId);
        mockMvc.perform(get("/card?cardId=1"))
                .andExpect(status().isOk())
                .andExpect(content().json(CARD_WITH_NOT_NULL_ID_JSON));
    }

    @Test
    public void createCard_CardIsNotValid_ShouldReturnErrorDto() throws Exception {
        when(cardService.create(cardWithNotNullId)).thenThrow(new InputParameterValidationException(EXCEPTION_MESSAGE));
        mockMvc.perform(post("/card")
                .contentType(MediaType.APPLICATION_JSON)
                .content(CARD_WITH_NOT_NULL_ID_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(ERROR_DTO_JSON));
    }

    @Test
    public void createCard_CardIsValid_ShouldReturnSuccessMessage() throws Exception {
        when(cardService.create(cardWithNullId)).thenReturn("Created card #111");
        MvcResult result = mockMvc.perform(post("/card")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(CARD_WITH_NULL_ID_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String body = result.getResponse().getContentAsString();
        Assert.assertEquals("Created card #111", body);
    }

    @Test
    public void deleteCard_CardIsValid_ShouldReturnSuccessMessage() throws Exception {
        when(cardService.deleteById(1)).thenReturn("Deleted card #1");
        MvcResult result = mockMvc.perform(delete("/card?cardId=1"))
                .andExpect(status().isOk())
                .andReturn();
        String body = result.getResponse().getContentAsString();
        Assert.assertEquals("Deleted card #1", body);
    }

    @Configuration
    @Import(WebConfiguration.class)
    public static class TestConfiguration {
        @Bean
        public CardService cardService() {
            return mock(CardService.class);
        }

        @Bean
        public CardController cardController(CardService cardService) {
            return new CardController(cardService);
        }

        @Bean
        public CustomExceptionHandler customExceptionHandler() {
            return new CustomExceptionHandler();
        }
    }
}
