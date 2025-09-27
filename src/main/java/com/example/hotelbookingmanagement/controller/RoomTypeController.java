package com.example.hotelbookingmanagement.controller;

import com.example.hotelbookingmanagement.model.dto.RoomTypeDto;
import com.example.hotelbookingmanagement.model.payload.request.RoomTypeRequest;
import com.example.hotelbookingmanagement.service.ImageUploadService;
import com.example.hotelbookingmanagement.service.RoomTypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@RequestMapping("/admins/settings/room-types")
@RequiredArgsConstructor
@Slf4j
public class RoomTypeController {

    private final RoomTypeService roomTypeService;

    private final ImageUploadService imageUploadService;


    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String addRoomType(@ModelAttribute("roomType") RoomTypeRequest roomTypeRequest,
                              @RequestParam("imageFile") MultipartFile imageFile,
                              RedirectAttributes redirectAttributes) {
        try {
            String imageUrl = imageUploadService.uploadRoomTypeImage(imageFile);
            roomTypeRequest.setImageUrl(imageUrl);
            roomTypeService.createRoomType(roomTypeRequest);
            redirectAttributes.addFlashAttribute("successMessage", "Room type added successfully!");
        } catch (IOException e) {
            log.error("Failed to upload room type image", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Error uploading image: " + e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error adding room type: " + e.getMessage());
        }
        return "redirect:/admins/settings/room-types";
    }


    @PostMapping("/update/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String updateRoomType(@PathVariable("id") Long id,
                                 @ModelAttribute("roomType") RoomTypeRequest roomTypeRequest,
                                 @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                                 RedirectAttributes redirectAttributes) {
        try {
            RoomTypeDto existingRoomType = roomTypeService.findById(id);
            String imageUrl = existingRoomType.getImageUrl();
            if (imageFile != null && !imageFile.isEmpty()) {
                imageUrl = imageUploadService.uploadRoomTypeImage(imageFile);
            }
            roomTypeRequest.setImageUrl(imageUrl);
            roomTypeService.updateRoomType(id, roomTypeRequest);
            redirectAttributes.addFlashAttribute("successMessage", "Room type updated successfully!");
            if (imageFile != null && !imageFile.isEmpty() && !imageUrl.equals(existingRoomType.getImageUrl())) {
                imageUploadService.deleteImage(existingRoomType.getImageUrl());
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating room type: " + e.getMessage());
        }
        return "redirect:/admins/settings/room-types";
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String deleteRoomType(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            // CRITICAL: Check for existing bookings before deleting.
            if (roomTypeService.hasAssociatedBookings(id)) {
                redirectAttributes.addFlashAttribute("errorMessage", "Cannot delete room type: It has active bookings or check-ins.");
                return "redirect:/admins/settings/room-types";
            }
            roomTypeService.deleteRoomType(id);
            redirectAttributes.addFlashAttribute("successMessage", "Room type deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting room type: " + e.getMessage());
        }
        return "redirect:/admins/settings/room-types";
    }

    @PostMapping("/toggle-featured/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String toggleFeatured(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            roomTypeService.toggleFeatured(id);
            redirectAttributes.addFlashAttribute("successMessage", "Room type feature status updated.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating room type: " + e.getMessage());
        }
        return "redirect:/admins/settings/room-types";
    }
}
