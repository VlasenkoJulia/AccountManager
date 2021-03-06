package account_manager.user;

import account_manager.security.AccountManagerUserDetails;
import account_manager.repository.user.UserEntity;
import account_manager.repository.user.UserRepository;
import account_manager.security.AccountManagerUserDetailsService;
import account_manager.service.validator.UserValidator;
import account_manager.web.exception_handling.InputParameterValidationException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class AccountEntityManagerUserEntityDetailsServiceTest {
    @Mock
    UserRepository userRepository;
    @Mock
    UserValidator validator;
    @InjectMocks
    AccountManagerUserDetailsService accountManagerUserDetailsService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = InputParameterValidationException.class)
    public void loadUserByUsername_UserNotFound_ShouldThrowException() {
        when(userRepository.findById("username")).thenReturn(Optional.empty());
        doThrow(InputParameterValidationException.class).when(validator).validateLoadByUserName(null);
        accountManagerUserDetailsService.loadUserByUsername("username");
    }

    @Test
    public void loadUserByUsername_UserFound_ShouldReturnUserDetails() {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserName("username");
        userEntity.setPassword("pass");
        userEntity.setEmail("email");
        when(userRepository.findById("username")).thenReturn(Optional.of(userEntity));
        doNothing().when(validator).validateLoadByUserName(userEntity);
        UserDetails userFound = accountManagerUserDetailsService.loadUserByUsername("username");
        assertEquals(new AccountManagerUserDetails(userEntity), userFound);
    }
}
