package account_manager.web.controllers;


import account_manager.WebConfiguration;
import account_manager.currency_converter.ConversionDto;
import account_manager.currency_converter.ConversionDtoValidator;
import account_manager.currency_converter.CurrencyConverter;
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

import static org.mockito.Matchers.refEq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CurrencyConverterControllerTest.TestConfiguration.class})
@WebAppConfiguration
public class CurrencyConverterControllerTest {
    private MockMvc mockMvc;

    @Autowired
    private CurrencyConverter currencyConverter;
    @Autowired
    private ConversionDtoValidator validator;
    @Autowired
    private WebApplicationContext webAppContext;

    @Before
    public void setup() {
        reset(currencyConverter);
        mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
    }

//    @Test
//    public void convert_AmountIsLessThanZero_ShouldReturnErrorDto() throws Exception {
//        ConversionDto dto = new ConversionDto(1, 2, -1.0);
//        mockMvc.perform(post("/converter")
//                .contentType(MediaType.APPLICATION_JSON_UTF8)
//                .content(new ObjectMapper().writeValueAsString(dto)))
//                .andExpect(status().isInternalServerError())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
//                .andExpect(jsonPath("$.message")
//                        .value("Passed amount less than 0"))
//                .andExpect(jsonPath("$.type")
//                        .value("INVALID"));
//    }
//
//    @Test()
//    public void convert_SourceAccountIdIsLessThanZero_ShouldReturnErrorDto() throws Exception {
//        ConversionDto dto = new ConversionDto(-1, 2, 1.0);
//        mockMvc.perform(post("/converter")
//                .contentType(MediaType.APPLICATION_JSON_UTF8)
//                .content(new ObjectMapper().writeValueAsString(dto)))
//                .andExpect(status().isInternalServerError())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
//                .andExpect(jsonPath("$.message")
//                        .value("Passed source account ID can not be null or less/equal 0"))
//                .andExpect(jsonPath("$.type")
//                        .value("INVALID"));
//    }
//
//    @Test
//    public void convert_SourceAccountIdIsZero_ShouldReturnErrorDto() throws Exception {
//        ConversionDto dto = new ConversionDto(0, 2, 1.0);
//        mockMvc.perform(post("/converter")
//                .contentType(MediaType.APPLICATION_JSON_UTF8)
//                .content(new ObjectMapper().writeValueAsString(dto)))
//                .andExpect(status().isInternalServerError())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
//                .andExpect(jsonPath("$.message")
//                        .value("Passed source account ID can not be null or less/equal 0"))
//                .andExpect(jsonPath("$.type")
//                        .value("INVALID"));
//    }
//
//    @Test
//    public void convert_SourceAccountIdIsNull_ShouldReturnErrorDto() throws Exception {
//        ConversionDto dto = new ConversionDto(null, 2, 1.0);
//        mockMvc.perform(post("/converter")
//                .contentType(MediaType.APPLICATION_JSON_UTF8)
//                .content(new ObjectMapper().writeValueAsString(dto)))
//                .andExpect(status().isInternalServerError())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
//                .andExpect(jsonPath("$.message")
//                        .value("Passed source account ID can not be null or less/equal 0"))
//                .andExpect(jsonPath("$.type")
//                        .value("INVALID"));
//    }
//
//    @Test()
//    public void convert_TargetAccountIdIsLessThanZero_ShouldReturnErrorDto() throws Exception {
//        ConversionDto dto = new ConversionDto(1, -2, 1.0);
//        mockMvc.perform(post("/converter")
//                .contentType(MediaType.APPLICATION_JSON_UTF8)
//                .content(new ObjectMapper().writeValueAsString(dto)))
//                .andExpect(status().isInternalServerError())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
//                .andExpect(jsonPath("$.message")
//                        .value("Passed target account ID can not be null or less/equal 0"))
//                .andExpect(jsonPath("$.type")
//                        .value("INVALID"));
//    }
//
//    @Test
//    public void convert_TargetAccountIdIsZero_ShouldReturnErrorDto() throws Exception {
//        ConversionDto dto = new ConversionDto(1, 0, 1.0);
//        mockMvc.perform(post("/converter")
//                .contentType(MediaType.APPLICATION_JSON_UTF8)
//                .content(new ObjectMapper().writeValueAsString(dto)))
//                .andExpect(status().isInternalServerError())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
//                .andExpect(jsonPath("$.message")
//                        .value("Passed target account ID can not be null or less/equal 0"))
//                .andExpect(jsonPath("$.type")
//                        .value("INVALID"));
//    }
//
//    @Test
//    public void convert_TargetAccountIdIsNull_ShouldReturnErrorDto() throws Exception {
//        ConversionDto dto = new ConversionDto(1, null, 1.0);
//        mockMvc.perform(post("/converter")
//                .contentType(MediaType.APPLICATION_JSON_UTF8)
//                .content(new ObjectMapper().writeValueAsString(dto)))
//                .andExpect(status().isInternalServerError())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
//                .andExpect(jsonPath("$.message")
//                        .value("Passed target account ID can not be null or less/equal 0"))
//                .andExpect(jsonPath("$.type")
//                        .value("INVALID"));
//    }

    @Test
    public void convert_ValidConversionDto_ShouldReturnSuccessMessage() throws Exception {
        ConversionDto dto = new ConversionDto(1, 2, 1.0);
        MvcResult result = mockMvc.perform(post("/converter")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andReturn();
        String body = result.getResponse().getContentAsString();
        Assert.assertEquals("Successful conversion", body);
        verify(currencyConverter, times(1)).convert(refEq(dto));
        verifyNoMoreInteractions(currencyConverter);
        verify(validator).validate(refEq(dto));
        verifyNoMoreInteractions(validator);
    }

    @Configuration
    @Import(WebConfiguration.class)
    public static class TestConfiguration {
        @Bean
        public CurrencyConverter currencyConverter() {
            return mock(CurrencyConverter.class);
        }

        @Bean
        public ConversionDtoValidator conversionDtoValidator() {
            return mock(ConversionDtoValidator.class);
        }

        @Bean
        public CurrencyConverterController currencyConverterController(CurrencyConverter currencyConverter,
                                                                       ConversionDtoValidator conversionDtoValidator) {
            return new CurrencyConverterController(currencyConverter, conversionDtoValidator);
        }

        @Bean
        public CustomExceptionHandler customExceptionHandler() {
            return new CustomExceptionHandler();
        }
    }
}
