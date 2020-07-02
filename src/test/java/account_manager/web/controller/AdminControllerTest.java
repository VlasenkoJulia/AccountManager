package account_manager.web.controller;

import account_manager.security.AccountManagerUserDetailsService;
import account_manager.service.ClientService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(controllers = AdminController.class)
public class AdminControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    ClientService clientService;
    @MockBean
    public AccountManagerUserDetailsService accountManagerUserDetailsService;

    @Test
    public void indexing() throws Exception {
        doNothing().when(clientService).index();
        mockMvc.perform(get("/indexing"))
                .andExpect(status().isOk())
        ;
    }
}