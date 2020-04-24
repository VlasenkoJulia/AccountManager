package account_manager.web.controllers;


import account_manager.security.WebSecurityConfig;
import account_manager.service.CurrencyConverterService;
import account_manager.service.dto.ConversionDto;
import account_manager.web.controller.CurrencyConverterController;
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

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(controllers = CurrencyConverterController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfig.class)
        })
public class CurrencyConverterControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CurrencyConverterService currencyConverterService;

    @Test
    @WithMockUser
    public void convert_ValidConversionDto_ShouldReturnSuccessMessage() throws Exception {
        ConversionDto validConversionDto = new ConversionDto(1, 2, 1.0);
        String validConversionDtoJson = "{\n"
                + "  \"sourceAccountId\": 1,\n"
                + "  \"targetAccountId\": 2,\n"
                + "  \"amount\": 1.0\n"
                + "}";
        when(currencyConverterService.convert(validConversionDto)).thenReturn("Successful conversion");
        MvcResult result = mockMvc.perform(post("/converter")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(validConversionDtoJson))
                .andExpect(status().isOk())
                .andReturn();
        String body = result.getResponse().getContentAsString();
        Assert.assertEquals("Successful conversion", body);
    }

    @Test
    @WithMockUser
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
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(notValidConversionDtoJson))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(errorDtoJson));
    }
}
