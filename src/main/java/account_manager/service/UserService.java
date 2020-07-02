package account_manager.service;

import account_manager.repository.user.UserEntity;
import account_manager.repository.user.UserRepository;
import account_manager.service.validator.UserValidator;
import account_manager.web.exception_handling.InputParameterValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private static Logger log = LoggerFactory.getLogger(UserService.class.getName());

    private final UserRepository userRepository;
    private final UserValidator validator;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository,
                       UserValidator validator,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.validator = validator;
        this.passwordEncoder = passwordEncoder;
    }


    public String create(UserEntity user) {
        validator.validateCreate(user);
        UserEntity byUsername = userRepository.findById(user.getUserName()).orElse(null);
        if (byUsername != null) {
            throw new InputParameterValidationException("User with passed user name has already exist");
        }
        UserEntity byEmail = userRepository.findByEmail(user.getEmail());
        if (byEmail != null) {
            throw new InputParameterValidationException("User with passed email has already exist");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        log.info("Created new user {}", user.getUserName());
        return "User created successfully! You can return to login page and log in!";
    }

    public UserEntity getByEmail(String email) {
        UserEntity byEmail = userRepository.findByEmail(email);
        validator.validateGetByEmail(byEmail);
        return byEmail;
    }

    public UserEntity getByResetToken(String token) {
        UserEntity byResetToken = userRepository.findByResetToken(token);
        validator.validateGetByResetToken(byResetToken);
        return byResetToken;
    }

    public String resetPassword(UserEntity user) {
        validator.validateReset(user);
        getByResetToken(user.getResetToken());
        user.setResetToken(null);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.update(user);
        log.info("User {} reset password", user.getUserName());
        return "Password changed successfully";
    }

    public String setResetToken(UserEntity user) {
        validator.validateSetResetToken(user);
        userRepository.update(user);
        return "Set token to user " + user.getUserName();
    }
}
