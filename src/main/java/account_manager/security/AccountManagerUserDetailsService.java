package account_manager.security;

import account_manager.repository.entity.User;
import account_manager.repository.UserRepository;
import account_manager.service.validator.UserValidator;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AccountManagerUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    private final UserValidator validator;

    public AccountManagerUserDetailsService(UserRepository userRepository, UserValidator validator) {
        this.userRepository = userRepository;
        this.validator = validator;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.getByUsername(username);
        validator.validateLoadByUserName(user);
        return new AccountManagerUserDetails(user);
    }
}
