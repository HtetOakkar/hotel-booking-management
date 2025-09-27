package com.example.hotelbookingmanagement.controller;

import com.example.hotelbookingmanagement.mapper.RoomMapper;
import com.example.hotelbookingmanagement.mapper.RoomTypeMapper;
import com.example.hotelbookingmanagement.model.dto.RoomDto;
import com.example.hotelbookingmanagement.model.enums.RoomStatus;
import com.example.hotelbookingmanagement.model.payload.request.RoomRequest;
import com.example.hotelbookingmanagement.service.ImageUploadService;
import com.example.hotelbookingmanagement.service.RoomService;
import com.example.hotelbookingmanagement.service.RoomTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admins/settings/rooms")
@RequiredArgsConstructor
public class RoomController {
    private final RoomService roomService;

    private final RoomTypeService roomTypeService;

    private final ImageUploadService imageUploadService;

    private final RoomMapper roomMapper;

    private final RoomTypeMapper roomTypeMapper;

    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String addRoom(@ModelAttribute("room") RoomRequest roomRequest, RedirectAttributes redirectAttributes) {
        try {
            roomService.saveRoom(roomRequest);
            redirectAttributes.addFlashAttribute("successMessage", "Room added successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error adding room: " + e.getMessage());
        }
        return "redirect:/admins/settings/rooms";
    }

    @PostMapping("/update/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String updateRoom(@PathVariable("id") Long id, @ModelAttribute("room") RoomRequest roomRequest, RedirectAttributes redirectAttributes) {
        try {
            roomService.updateRoom(id, roomRequest);
            redirectAttributes.addFlashAttribute("successMessage", "Room updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating room: " + e.getMessage());
        }
        return "redirect:/admins/settings/rooms";
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String deleteRoom(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            roomService.deleteRoom(id);
            redirectAttributes.addFlashAttribute("successMessage", "Room deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Could not delete room: " + e.getMessage());
        }
        return "redirect:/admins/settings/rooms";
    }

    @PostMapping("/update-status/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF')")
    public String updateRoomStatus(@PathVariable("id") Long id,
                                   @RequestParam("status") RoomStatus status,
                                   RedirectAttributes redirectAttributes) {
        try {
            roomService.updateRoomStatus(id, status);
            redirectAttributes.addFlashAttribute("successMessage", "Room status updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating status: " + e.getMessage());
        }
        return "redirect:/admins/settings/rooms";
    }


}
