	package com.example.hotelbookingmanagement.controller;

import java.io.IOException;
import java.util.List;

import com.example.hotelbookingmanagement.config.CurrentUser;
import com.example.hotelbookingmanagement.exception.EntityNotFoundException;
import com.example.hotelbookingmanagement.model.dto.BookingDto;
import com.example.hotelbookingmanagement.model.dto.HotelDto;
import com.example.hotelbookingmanagement.service.BookingService;
import com.example.hotelbookingmanagement.service.HotelService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.hotelbookingmanagement.model.dto.UserDto;
import com.example.hotelbookingmanagement.service.ImageUploadService;
import com.example.hotelbookingmanagement.service.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final ImageUploadService imageUploadService;

    private final PasswordEncoder passwordEncoder;

    private final HotelService hotelService;

    private final BookingService bookingService;


    @ModelAttribute("hotel")
    public HotelDto hotelDto() {
        return hotelService.findFirstHotel();
    }


    @GetMapping("/profile")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String viewProfile(Model model, @CurrentUser UserDetails currentUser) {
        if (currentUser == null) {
            return "redirect:/users/login";
        }
        UserDto user = userService.findByEmail(currentUser.getUsername());
        model.addAttribute("user", user);

        List<BookingDto> bookings = bookingService.findByUserId(user.getId());
        model.addAttribute("bookings", bookings);

        model.addAttribute("cacheBuster", System.currentTimeMillis());
        return "user-profile";
    }

    @PostMapping("/profile")
    public String updateProfile(
            @RequestParam("fullName") String fullName,
            @RequestParam("phoneNumber") String phoneNumber,
            @RequestParam(value = "profileImage", required = false) MultipartFile profileImage,
            @AuthenticationPrincipal UserDetails currentUser,
            RedirectAttributes redirectAttributes) {

        if (currentUser == null) {
            // Handle case where user is not authenticated
            return "redirect:/users/login";
        }
        UserDto existingUser = userService.findByEmail(currentUser.getUsername());
        String oldProfileUrl = existingUser.getProfileImageUrl();
        String newProfileUrl = oldProfileUrl;
        try {
            if (profileImage != null && !profileImage.isEmpty()) {
                newProfileUrl = imageUploadService.uploadProfileImage(profileImage);
                imageUploadService.deleteImage(oldProfileUrl);
            }
            userService.updateUserProfile(currentUser.getUsername(), fullName, phoneNumber, newProfileUrl);
            redirectAttributes.addFlashAttribute("successMessage", "Profile updated successfully!");
        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating profile. Could not save image.");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "An unexpected error occurred.");
        }

        return "redirect:/users/profile";
    }



    @PostMapping("/profile/change-password")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String changePassword(@RequestParam("currentPassword") String currentPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 @RequestParam("confirmPassword") String confirmPassword,
                                 @CurrentUser UserDetails currentUser,
                                 RedirectAttributes redirectAttributes) {

        UserDto existingUser = userService.findByEmail(currentUser.getUsername());
        String oldPassword = existingUser.getPassword();
        if (!passwordEncoder.matches(currentPassword, oldPassword)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Incorrect current password.");
            return "redirect:/users/profile";
        }
        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("errorMessage", "New passwords do not match.");
            return "redirect:/users/profile";
        }

        try {
            newPassword = passwordEncoder.encode(newPassword);
            userService.changePassword(existingUser.getId(), newPassword);
            redirectAttributes.addFlashAttribute("successMessage", "Your password has been updated successfully.");
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Could not find your user account.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "An unexpected error occurred. Please try again.");
        }
        return "redirect:/users/logout";
    }
}
