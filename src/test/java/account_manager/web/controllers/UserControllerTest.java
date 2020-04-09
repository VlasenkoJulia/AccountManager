package account_manager.web.controllers;

import account_manager.web.WebConfiguration;
import account_manager.repository.user.User;
import account_manager.service.UserService;
import account_manager.web.controller.UserController;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {UserControllerTest.TestConfiguration.class})
@WebAppConfiguration
public class UserControllerTest {
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private WebApplicationContext webAppContext;

    @Before
    public void setup() {
        reset(userService);
        mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
    }

    private final String EXCEPTION_MESSAGE = "Exception message";
    private final String ERROR_DTO_JSON = "{\n"
            + "  \"message\": \"Exception message\",\n"
            + "  \"type\": \"INVALID\"\n"
            + "}";

    private User validUser = createUser("username", "password", "password", "email@demo.com");
    private final String VALID_USER_JSON = "{\n"
            + "  \"userName\" : \"username\",\n"
            + "  \"password\" : \"password\",\n"
            + "  \"confirmPassword\": \"password\",\n"
            + "  \"email\": \"email@demo.com\"\n"
            + "}";

    private User invalidUser = createUser("username", "password", "password", "email");
    private final String INVALID_USER_JSON = "{\n"
            + "  \"userName\" : \"username\",\n"
            + "  \"password\" : \"password\",\n"
            + "  \"confirmPassword\": \"password\",\n"
            + "  \"email\": \"email\"\n"
            + "}";

    @Test
    public void createUser_InvalidUser_ShouldReturnErrorDto() throws Exception {
        when(userService.create(invalidUser)).thenThrow(new InputParameterValidationException(EXCEPTION_MESSAGE));
        mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(INVALID_USER_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(ERROR_DTO_JSON));
    }

    @Test
    public void createUser_ValidUser_ShouldReturnSuccessMessage() throws Exception {
        when(userService.create(validUser)).thenReturn("User created successfully! You can return to login page and log in!");
        MvcResult result = mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(VALID_USER_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String body = result.getResponse().getContentAsString();
        Assert.assertEquals("User created successfully! You can return to login page and log in!", body);
    }


    private User createUser(String userName, String password, String confirmedPassword, String email) {
        User user = new User();
        user.setUserName(userName);
        user.setPassword(password);
        user.setConfirmPassword(confirmedPassword);
        user.setEmail(email);
        return user;
    }

    @Configuration
    @Import(WebConfiguration.class)
    public static class TestConfiguration {
        @Bean
        public UserService userService() {
            return mock(UserService.class);
        }

        @Bean
        public UserController userController(UserService userService) {
            return new UserController(userService);
        }

        @Bean
        public CustomExceptionHandler customExceptionHandler() {
            return new CustomExceptionHandler();
        }
    }
}
