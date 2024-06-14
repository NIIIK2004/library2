package com.example.library.controller;

import com.example.library.impl.UserImpl;
import com.example.library.model.User;
import com.example.library.validation.RegistrationValidator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class AuthController {
    private final UserImpl userImpl;
    private final RegistrationValidator registrationValidator;

    @GetMapping("/registration")
    public String regPage(Model model) {
        model.addAttribute("user", new User());
        return "registration";
    }

    @PostMapping("/registration/save")
    public String registration(@ModelAttribute("user") @Valid User user, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        registrationValidator.validate(user, result);

        try {
            if (result.hasErrors()) {
                model.addAttribute("errors", result.getAllErrors());
                model.addAttribute("user", user);
                return "registration";
            }
            userImpl.registerAuthUser(user, redirectAttributes);
            return "redirect:/registration";
        } catch (IllegalArgumentException e) {
            result.rejectValue("username", e.getMessage());
            model.addAttribute("user", user);
            return "registration";
        }
    }

    @GetMapping("/login")
    public String login() {
        return "auth";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        userImpl.logout(request, response);
        return "redirect:/login?logout";
    }

}
