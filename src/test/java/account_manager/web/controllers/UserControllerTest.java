package account_manager.web.controllers;

import account_manager.repository.user.User;
import account_manager.security.WebSecurityConfig;
import account_manager.service.UserService;
import account_manager.web.controller.UserController;
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
@WebMvcTest(controllers = UserController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfig.class)
        })
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

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
    @WithMockUser
    public void createUser_InvalidUser_ShouldReturnErrorDto() throws Exception {
        when(userService.create(invalidUser)).thenThrow(new InputParameterValidationException(EXCEPTION_MESSAGE));
        mockMvc.perform(post("/user")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(INVALID_USER_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(ERROR_DTO_JSON));
    }

    @Test
    @WithMockUser
    public void createUser_ValidUser_ShouldReturnSuccessMessage() throws Exception {
        when(userService.create(validUser)).thenReturn("User created successfully! You can return to login page and log in!");
        MvcResult result = mockMvc.perform(post("/user")
                .with(csrf())
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
}
