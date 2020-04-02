package account_manager.web.controllers;

import account_manager.web.WebConfiguration;
import account_manager.security.WebSecurityConfig;
import account_manager.security.AccountManagerUserDetailsService;
import account_manager.web.controller.MainController;
import account_manager.web.exception_handling.CustomExceptionHandler;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.Mockito.mock;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {MainControllerTest.TestConfiguration.class})
@WebAppConfiguration
public class MainControllerTest {
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webAppContext;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webAppContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithMockUser
    public void home_ShouldRenderHomeView() throws Exception {
        mockMvc.perform(get("/").with(user("username")))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(forwardedUrl("/WEB-INF/view/index.jsp"))
                .andExpect(model().attributeExists("username"))
                .andExpect(model().attribute("username", "username"))
        ;
    }


    @Configuration
    @Import({WebConfiguration.class, WebSecurityConfig.class})
    public static class TestConfiguration {
        @Bean
        public MainController mainController() {
            return new MainController();
        }

        @Bean
        public CustomExceptionHandler customExceptionHandler() {
            return new CustomExceptionHandler();
        }

        @Bean
        public AccountManagerUserDetailsService accountManagerUserDetailsService() {
            return mock(AccountManagerUserDetailsService.class);
        }
    }
}
