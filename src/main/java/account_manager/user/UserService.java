package account_manager.user;

import account_manager.web.exception_handling.InputParameterValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final UserValidator validator;

    @Autowired
    public UserService(UserRepository userRepository, UserValidator validator) {
        this.userRepository = userRepository;
        this.validator = validator;
    }

    public User create(User user) {
        validator.validateCreate(user);
        User byUsername = userRepository.getByUsername(user.getUserName());
        if (byUsername != null) {
            throw new InputParameterValidationException("User with passed user name has already exist");
        }
        return userRepository.create(user);
    }

    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userRepository.getByUsername(s);
        validator.validateGet(user);
        return new UserPrincipal(user);
    }
}
