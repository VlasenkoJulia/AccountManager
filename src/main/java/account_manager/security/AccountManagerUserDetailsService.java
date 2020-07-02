package account_manager.security;

import account_manager.repository.user.UserEntity;
import account_manager.repository.user.UserRepository;
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
        UserEntity userEntity = userRepository.findById(username).orElse(null);
        validator.validateLoadByUserName(userEntity);
        return new AccountManagerUserDetails(userEntity);
    }
}
