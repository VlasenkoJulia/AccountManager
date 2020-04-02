package account_manager.web.controllers;


import account_manager.web.WebConfiguration;
import account_manager.service.dto.ConversionDto;
import account_manager.service.CurrencyConverterService;
import account_manager.web.controller.CurrencyConverterController;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CurrencyConverterControllerTest.TestConfiguration.class})
@WebAppConfiguration
public class CurrencyConverterControllerTest {
    private MockMvc mockMvc;
    @Autowired
    private CurrencyConverterService currencyConverterService;
    @Autowired
    private WebApplicationContext webAppContext;

    @Before
    public void setup() {
        reset(currencyConverterService);
        mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
    }

    @Test
    public void convert_ValidConversionDto_ShouldReturnSuccessMessage() throws Exception {
        ConversionDto validConversionDto = new ConversionDto(1, 2, 1.0);
        String validConversionDtoJson = "{\n"
                + "  \"sourceAccountId\": 1,\n"
                + "  \"targetAccountId\": 2,\n"
                + "  \"amount\": 1.0\n"
                + "}";
        when(currencyConverterService.convert(validConversionDto)).thenReturn("Successful conversion");
        MvcResult result = mockMvc.perform(post("/converter")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(validConversionDtoJson))
                .andExpect(status().isOk())
                .andReturn();
        String body = result.getResponse().getContentAsString();
        Assert.assertEquals("Successful conversion", body);
    }

    @Test
    public void convert_NotValidConversionDto_ShouldReturnErrorDto() throws Exception {
        ConversionDto notValidConversionDto = new ConversionDto(0, 2, 1.0);
        String errorDtoJson = "{\n"
                + "  \"message\": \"Exception message\",\n"
                + "  \"type\": \"INVALID\"\n"
                + "}";
        String notValidConversionDtoJson = "{\n"
                + "  \"sourceAccountId\": 0,\n"
                + "  \"targetAccountId\": 2,\n"
                + "  \"amount\": 1.0\n"
                + "}";
        when(currencyConverterService.convert(notValidConversionDto)).thenThrow(new InputParameterValidationException("Exception message"));
        mockMvc.perform(post("/converter")
                .contentType(MediaType.APPLICATION_JSON)
                .content(notValidConversionDtoJson))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(errorDtoJson));
    }

    @Configuration
    @Import(WebConfiguration.class)
    public static class TestConfiguration {
        @Bean
        public CurrencyConverterService currencyConverterService() {
            return mock(CurrencyConverterService.class);
        }

        @Bean
        public CurrencyConverterController currencyConverterController(CurrencyConverterService currencyConverterService) {
            return new CurrencyConverterController(currencyConverterService);
        }

        @Bean
        public CustomExceptionHandler customExceptionHandler() {
            return new CustomExceptionHandler();
        }
    }
}
