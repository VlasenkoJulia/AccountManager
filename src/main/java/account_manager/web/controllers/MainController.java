package account_manager.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

@Controller
public class MainController {
    @GetMapping("/")
    public ModelAndView home(Principal principal) {
        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("username", principal.getName());
        return modelAndView;
    }
}
