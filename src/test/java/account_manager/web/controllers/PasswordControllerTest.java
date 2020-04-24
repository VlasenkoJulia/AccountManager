package account_manager.web.controllers;

import account_manager.repository.user.User;
import account_manager.security.AccountManagerUserDetailsService;
import account_manager.service.EmailService;
import account_manager.service.UserService;
import account_manager.web.controller.PasswordController;
import account_manager.web.exception_handling.InputParameterValidationException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(controllers = PasswordController.class)
public class PasswordControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;
    @MockBean
    private EmailService emailService;
    @MockBean
    public AccountManagerUserDetailsService accountManagerUserDetailsService;

    private final String EXCEPTION_MESSAGE = "Exception message";
    private final String ERROR_DTO_JSON = "{\n"
            + "  \"message\": \"Exception message\",\n"
            + "  \"type\": \"INVALID\"\n"
            + "}";

    private User userWithoutToken = new User("username", "password", "email@demo.com", null, "password");
    private final String USER_WITHOUT_TOKEN_JSON = "{\n"
            + "  \"userName\" : \"username\",\n"
            + "  \"password\" : \"password\",\n"
            + "  \"email\": \"email@demo.com\",\n"
            + "  \"resetToken\": null,\n"
            + "  \"confirmPassword\": \"password\"\n"
            + "}";

    private User userWithToken = new User("name", "password", "email@demo.com", "token", "password");
    private final String USER_WITH_TOKEN_JSON = "{\n"
            + "  \"userName\" : \"name\",\n"
            + "  \"password\" : \"password\",\n"
            + "  \"email\": \"email@demo.com\",\n"
            + "  \"resetToken\": \"token\",\n"
            + "  \"confirmPassword\": \"password\"\n"
            + "}";

    @Test
    public void forgotPassword_ShouldRenderForgotPasswordForm() throws Exception {
        mockMvc.perform(get("/forgotPassword"))
                .andExpect(status().isOk())
                .andExpect(view().name("forgotPassword"))
                .andExpect(forwardedUrl("/WEB-INF/view/forgotPassword.jsp"))
        ;
    }

    @Test
    public void sendResetMessage_InvalidEmail_ShouldReturnErrorDto() throws Exception {
        when(userService.getByEmail("email")).thenThrow(new InputParameterValidationException(EXCEPTION_MESSAGE));
        mockMvc.perform(post("/forgotPassword?email=email"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(ERROR_DTO_JSON));
    }

    @Test
    public void sendResetMessage_ValidEmail_ShouldReturnSuccessMessage() throws Exception {
        when(userService.getByEmail("email@demo.com")).thenReturn(userWithoutToken);
        MvcResult result = mockMvc.perform(post("/forgotPassword?email=email@demo.com"))
                .andExpect(status().isOk())
                .andReturn();
        String resetToken = userWithoutToken.getResetToken();
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("yuliia.vlasenko90@gmail.com");
        message.setTo("email@demo.com");
        message.setSubject("Password Reset Request");
        message.setText("To reset your password, click the link below:\n" + "http://localhost:8080/reset?token=" + resetToken);
        String body = result.getResponse().getContentAsString();
        assertEquals("A password reset link has been sent to email@demo.com", body);
        verify(emailService).sendEmail(refEq(message));
    }

    @Test
    public void resetPasswordForm_InvalidToken_ShouldReturnErrorDto() throws Exception {
        when(userService.getByResetToken("token")).thenThrow(new InputParameterValidationException(EXCEPTION_MESSAGE));
        mockMvc.perform(get("/reset?token=token"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(ERROR_DTO_JSON));
    }

    @Test
    public void resetPasswordForm_ShouldRenderResetPasswordForm() throws Exception {
        when(userService.getByResetToken("token")).thenReturn(userWithToken);
        mockMvc.perform(get("/reset?token=token"))
                .andExpect(status().isOk())
                .andExpect(view().name("resetPassword"))
                .andExpect(forwardedUrl("/WEB-INF/view/resetPassword.jsp"))
                .andExpect(model().attributeExists("userName"))
                .andExpect(model().attributeExists("email"))
                .andExpect(model().attributeExists("token"))
                .andExpect(model().attribute("userName", userWithToken.getUserName()))
                .andExpect(model().attribute("email", userWithToken.getEmail()))
                .andExpect(model().attribute("token", userWithToken.getResetToken()));
    }

    @Test
    public void resetPassword_InvalidUser_ShouldReturnErrorDto() throws Exception {
        when(userService.resetPassword(userWithoutToken)).thenThrow(new InputParameterValidationException(EXCEPTION_MESSAGE));
        mockMvc.perform(post("/resetPassword")
                .contentType(MediaType.APPLICATION_JSON)
                .content(USER_WITHOUT_TOKEN_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(ERROR_DTO_JSON));
    }

    @Test
    public void resetPassword_ValidUser_ShouldReturnSuccessMessage() throws Exception {
        when(userService.resetPassword(userWithToken)).thenReturn("Password changed successfully");
        MvcResult result = mockMvc.perform(post("/resetPassword")
                .contentType(MediaType.APPLICATION_JSON)
                .content(USER_WITH_TOKEN_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String body = result.getResponse().getContentAsString();
        Assert.assertEquals("Password changed successfully", body);
    }
}
