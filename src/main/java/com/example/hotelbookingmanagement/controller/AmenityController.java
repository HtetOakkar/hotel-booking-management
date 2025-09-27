package com.example.hotelbookingmanagement.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.hotelbookingmanagement.model.dto.AmenityDto;
import com.example.hotelbookingmanagement.model.payload.request.AmenityRequest;
import com.example.hotelbookingmanagement.service.AmenityService;
import com.example.hotelbookingmanagement.service.ImageUploadService;
import com.example.hotelbookingmanagement.service.RoomTypeService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/admins/settings/amenities")
@RequiredArgsConstructor
@Slf4j
public class AmenityController {
    private final AmenityService amenityService;

    private final ImageUploadService imageUploadService;

    private final RoomTypeService roomTypeService;

    @PostMapping("/add")
    public String addAmenity(@ModelAttribute("amenity") AmenityRequest amenityRequest,
                             @RequestParam("imageFile") MultipartFile imageFile,
                             RedirectAttributes redirectAttributes) {
        try {
            String imageUrl = imageUploadService.uploadRoomTypeImage(imageFile);
            amenityRequest.setImageUrl(imageUrl);
            amenityService.saveAmenity(amenityRequest);
            redirectAttributes.addFlashAttribute("successMessage", "Amenity added successfully!");
        } catch (IOException e) {
            log.error("Could not add amenity", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Error uploading image: " + e.getMessage());
        } catch (Exception e) {
            log.error("Could not add amenity", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Error adding amenity: " + e.getMessage());
        }
        return "redirect:/admins/settings/amenities";
    }

    @PostMapping("/update/{id}")
    public String updateAmenity(@PathVariable("id") Long id,
                                @ModelAttribute("amenity") AmenityRequest amenityRequest,
                                @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                                RedirectAttributes redirectAttributes) {
        try {
            AmenityDto existingAmenity = amenityService.getAmenityById(id);
            String imageUrl = existingAmenity.getImageUrl();
            if (imageFile != null && !imageFile.isEmpty()) {

                imageUrl = imageUploadService.uploadRoomTypeImage(imageFile);
                amenityRequest.setImageUrl(imageUrl);
            }

            amenityRequest.setImageUrl(imageUrl);
            amenityService.updateAmenity(id, amenityRequest);
            redirectAttributes.addFlashAttribute("successMessage", "Amenity updated successfully!");
            if (imageFile != null && !imageFile.isEmpty() && !imageUrl.equals(existingAmenity.getImageUrl())) {
                imageUploadService.deleteImage(existingAmenity.getImageUrl());
            }
        } catch (IOException e) {
            log.error("Could not upload amenity image", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Error uploading image: " + e.getMessage());
        } catch (Exception e) {
            log.error("Could not update amenity", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating amenity: " + e.getMessage());
        }
        return "redirect:/admins/settings/amenities";
    }

    @PostMapping("/delete/{id}")
    public String deleteAmenity(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            amenityService.deleteAmenity(id);
            redirectAttributes.addFlashAttribute("successMessage", "Amenity deleted successfully!");
        } catch (Exception e) {
            log.error("Could not delete amenity", e);
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admins/settings/amenities";
    }

    // API endpoint for fetching amenities for a specific room type
    @GetMapping("/by-room-type/{roomTypeId}")
    @ResponseBody
    public ResponseEntity<List<AmenityDto>> getAmenitiesByRoomType(@PathVariable Long roomTypeId) {
        List<AmenityDto> amenities = amenityService.findByRoomTypeId(roomTypeId);
        return ResponseEntity.ok(amenities);
    }
}
