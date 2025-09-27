package com.example.hotelbookingmanagement.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.hotelbookingmanagement.config.CurrentUser;
import com.example.hotelbookingmanagement.exception.EntityNotFoundException;
import com.example.hotelbookingmanagement.model.dto.UserDto;
import com.example.hotelbookingmanagement.service.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/admins/profile")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminProfileController {

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;



    @PostMapping("/change-password")
    public String changePassword(@RequestParam("currentPassword") String currentPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 @RequestParam("confirmPassword") String confirmPassword,
                                 @CurrentUser UserDetails currentUser,
                                 RedirectAttributes redirectAttributes) {

        UserDto existingUser = userService.findByEmail(currentUser.getUsername());
        String oldPassword = existingUser.getPassword();
        if (!passwordEncoder.matches(currentPassword, oldPassword)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Incorrect current password.");
            return "redirect:/admins/profile/change-password";
        }
        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("errorMessage", "New passwords do not match.");
            return "redirect:/admins/profile/change-password";
        }

        try {
            newPassword = passwordEncoder.encode(newPassword);
            userService.changePassword(existingUser.getId(), newPassword);
            redirectAttributes.addFlashAttribute("successMessage", "Your password has been updated successfully. Please sign in again");
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Could not find your user account.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "An unexpected error occurred. Please try again.");
        }
        return "redirect:/admins/login";
    }
}
