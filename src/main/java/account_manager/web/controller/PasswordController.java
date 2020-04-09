package account_manager.web.controller;

import account_manager.service.EmailService;
import account_manager.repository.user.User;
import account_manager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Controller
public class PasswordController {
    private final UserService userService;
    private final EmailService emailService;

    @Autowired
    public PasswordController(UserService userService, EmailService emailService) {
        this.userService = userService;
        this.emailService = emailService;
    }

    @GetMapping("/forgotPassword")
    public String forgotPasswordForm() {
        return "forgotPassword";
    }

    @PostMapping("/forgotPassword")
    @ResponseBody
    public String sendResetMessage(@RequestParam String email) {
        User user = setResetToken(email);
        sendResetEmail(user);
        return "A password reset link has been sent to " + user.getEmail();
    }

    private void sendResetEmail(User user) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("yuliia.vlasenko90@gmail.com");
        message.setTo(user.getEmail());
        message.setSubject("Password Reset Request");
        message.setText("To reset your password, click the link below:\n" + "http://localhost:8080/reset?token=" + user.getResetToken());
        emailService.sendEmail(message);
    }

    private User setResetToken(String email) {
        User user = userService.getByEmail(email);
        user.setResetToken(UUID.randomUUID().toString());
        userService.setResetToken(user);
        return user;
    }

    @GetMapping("/reset")
    public ModelAndView resetPasswordForm(@RequestParam("token") String token) {
        User user = userService.getByResetToken(token);
        ModelAndView modelAndView = new ModelAndView("resetPassword");
        modelAndView.addObject("userName", user.getUserName());
        modelAndView.addObject("email", user.getEmail());
        modelAndView.addObject("token", user.getResetToken());
        return modelAndView;
    }

    @PostMapping("/resetPassword")
    @ResponseBody
    public String resetPassword(@RequestBody User user) {
        return userService.resetPassword(user);
    }

}
