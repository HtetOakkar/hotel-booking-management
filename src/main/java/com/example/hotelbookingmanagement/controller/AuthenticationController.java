package com.example.hotelbookingmanagement.controller;

import com.example.hotelbookingmanagement.model.dto.HotelDto;
import com.example.hotelbookingmanagement.model.payload.request.UserRegistrationRequest;
import com.example.hotelbookingmanagement.service.HotelService;
import com.example.hotelbookingmanagement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class AuthenticationController {

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    private final HotelService hotelService;

    @ModelAttribute("hotel")
    public HotelDto hotelDto() {
        return hotelService.findFirstHotel();
    }

    @GetMapping("/users/login")
    public String userLogin() {
        return "user-login";
    }

    @GetMapping("/admins/login")
    public String adminLogin() {
        return "/admin/admin-login";
    }



    @GetMapping("/users/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserRegistrationRequest());
        return "user-register";
    }

    @PostMapping("/users/register")
    public String registerUser(@Valid @ModelAttribute("user") UserRegistrationRequest userRequest, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "user-register";
        }

        if (userService.existByEmail(userRequest.getEmail())) {
            bindingResult.rejectValue("email", "email.exists", "An account with this email already exists.");
            return "user-register";
        }

        if (userService.existByUsername(userRequest.getUsername())) {
            bindingResult.rejectValue("username", "username.exists", "An account with this username already exists.");
            return "user-register";
        }

        if (userService.existByPhoneNumber(userRequest.getPhoneNumber())) {
            bindingResult.rejectValue("phoneNumber", "phoneNumber.exists", "An account with this phone number already exists.");
            return "user-register";
        }
        userRequest.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        userService.registerNewUser(userRequest);
        return "redirect:/users/login?registered"; // Redirect with a success parameter
    }

}
