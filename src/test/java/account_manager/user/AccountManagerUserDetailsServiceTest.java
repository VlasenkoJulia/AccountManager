package account_manager.user;

import account_manager.web.exception_handling.InputParameterValidationException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class AccountManagerUserDetailsServiceTest {
    @Mock UserRepository userRepository;
    @Mock UserValidator validator;
    @InjectMocks AccountManagerUserDetailsService accountManagerUserDetailsService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = InputParameterValidationException.class)
    public void loadUserByUsername_UserNotFound_ShouldThrowException() {
        when(userRepository.getByUsername("username")).thenReturn(null);
        doThrow(InputParameterValidationException.class).when(validator).validateLoadByUserName(null);
        accountManagerUserDetailsService.loadUserByUsername("username");
    }

    @Test
    public void loadUserByUsername_UserFound_ShouldReturnUserDetails() {
        User user = new User();
        user.setUserName("username");
        user.setPassword("pass");
        user.setEmail("email");
        when(userRepository.getByUsername("username")).thenReturn(user);
        doNothing().when(validator).validateLoadByUserName(user);
        UserDetails userFound = accountManagerUserDetailsService.loadUserByUsername("username");
        assertEquals(new AccountManagerUserDetails(user), userFound);
    }
}