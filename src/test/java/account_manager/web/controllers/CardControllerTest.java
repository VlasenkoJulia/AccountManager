package account_manager.web.controllers;

import account_manager.WebConfiguration;
import account_manager.account.Account;
import account_manager.card.Card;
import account_manager.card.CardRepository;
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

import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CardControllerTest.TestConfiguration.class})
@WebAppConfiguration
public class CardControllerTest {
    private MockMvc mockMvc;

    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private WebApplicationContext webAppContext;

    @Before
    public void setup() {
        reset(cardRepository);
        mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
    }

    @Test
    public void getCardById_CardNotFound_ShouldReturnErrorDto() throws Exception {
        when(cardRepository.getById(1)).thenReturn(null);
        mockMvc.perform(get("/card?cardId=1"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.message")
                        .value("Card with passed ID do not exist"))
                .andExpect(jsonPath("$.type")
                        .value("INVALID"));
        verify(cardRepository, times(1)).getById(1);
        verifyNoMoreInteractions(cardRepository);
    }

    @Test
    public void getCardById_CardFound_ShouldReturnFoundCard() throws Exception {
        when(cardRepository.getById(1)).thenReturn(createCard(1));
        mockMvc.perform(get("/card?cardId=1"))
                .andExpect(status().isOk());
        verify(cardRepository, times(1)).getById(1);
        verifyNoMoreInteractions(cardRepository);
    }

    @Test
    public void createCard_CardIdIsNotNull_ShouldReturnErrorDto() throws Exception {
        Card card = createCard(1);
        mockMvc.perform(post("/card")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().writeValueAsString(card)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.message")
                        .value("Can not provide insert operation with passed card"))
                .andExpect(jsonPath("$.type")
                        .value("INVALID"));
    }

    @Test
    public void createCard_AccountsSetIsEmpty_ShouldReturnErrorDto() throws Exception {
        Card card = createCard("111", new HashSet<>(0));
        mockMvc.perform(post("/card")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().writeValueAsString(card)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.message")
                        .value("Card can not be created without reference to the account(s)"))
                .andExpect(jsonPath("$.type")
                        .value("INVALID"));
    }

    @Test
    public void createCard_CardIsValid_ShouldReturnSuccessMessage() throws Exception {
        Set<Account> accounts = new HashSet<>(1);
        accounts.add(new Account());
        Card card = createCard("111", accounts);
        doNothing().when(cardRepository).create(card);
        MvcResult result = mockMvc.perform(post("/card")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().writeValueAsString(card)))
                .andExpect(status().isOk())
                .andReturn();
        String body = result.getResponse().getContentAsString();
        Assert.assertEquals("Created card #111", body);
        verify(cardRepository, times(1)).create(card);
        verifyNoMoreInteractions(cardRepository);
    }

    @Test
    public void deleteCard_CardIsValid_ShouldReturnSuccessMessage() throws Exception {
        doNothing().when(cardRepository).deleteById(1);
        MvcResult result = mockMvc.perform(delete("/card?cardId=1"))
                .andExpect(status().isOk())
                .andReturn();
        String body = result.getResponse().getContentAsString();
        Assert.assertEquals("Deleted card #1", body);
        verify(cardRepository, times(1)).deleteById(1);
        verifyNoMoreInteractions(cardRepository);
    }


    private Card createCard(String number, Set<Account> accounts) {
        Card card = new Card();
        card.setId(null);
        card.setNumber(number);
        card.setAccounts(accounts);
        return card;
    }


    private Card createCard(Integer id) {
        Card card = new Card();
        card.setId(id);
        return card;
    }

    @Configuration
    @Import(WebConfiguration.class)
    public static class TestConfiguration {

        @Bean
        public CardRepository cardRepository() {
            return mock(CardRepository.class);
        }

        @Bean
        public CardController cardController(CardRepository cardRepository) {
            return new CardController(cardRepository);
        }

        @Bean
        public CustomExceptionHandler customExceptionHandler() {
            return new CustomExceptionHandler();
        }
    }
}
