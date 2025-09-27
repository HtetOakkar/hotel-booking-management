package com.example.hotelbookingmanagement.controller;

import com.example.hotelbookingmanagement.mapper.PaymentTypeMapper;
import com.example.hotelbookingmanagement.model.dto.PaymentTypeDto;
import com.example.hotelbookingmanagement.model.payload.request.PaymentTypeRequest;
import com.example.hotelbookingmanagement.service.ImageUploadService;
import com.example.hotelbookingmanagement.service.PaymentTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@RequestMapping("/admins/settings/payment-methods")
@RequiredArgsConstructor
public class PaymentTypeController {

    private final PaymentTypeService paymentTypeService;

    private final PaymentTypeMapper paymentTypeMapper;

    private final ImageUploadService imageUploadService;

    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String addPaymentType(@ModelAttribute("paymentType") PaymentTypeRequest paymentTypeRequest,
                                 @RequestParam("imageFile") MultipartFile imageFile,
                                 RedirectAttributes redirectAttributes) {
        try {
            if (imageFile != null && !imageFile.isEmpty()) {
                String imageUrl = imageUploadService.uploadPaymentImage(imageFile);
                paymentTypeRequest.setImageUrl(imageUrl);
            } else {
                redirectAttributes.addFlashAttribute("message", "Please select an image file");
            }
            paymentTypeService.savePaymentType(paymentTypeRequest);
            redirectAttributes.addFlashAttribute("successMessage", "Payment method added successfully!");
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error uploading image: " + e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error adding payment method: " + e.getMessage());
        }
        return "redirect:/admins/settings/payment-methods";
    }

    @PostMapping("/update/{id}")
    public String updatePaymentType(@PathVariable("id") Long id,
                                    @ModelAttribute("paymentType") PaymentTypeRequest paymentTypeRequest,
                                    @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                                    RedirectAttributes redirectAttributes) {
        try {
            PaymentTypeDto existingPaymentType = paymentTypeService.findById(id);
            String imageUrl = existingPaymentType.getImageUrl();
            if (imageFile != null && !imageFile.isEmpty()) {
                imageUrl = imageUploadService.uploadPaymentImage(imageFile);

            }
            paymentTypeRequest.setImageUrl(imageUrl);
            paymentTypeService.updatePaymentType(id, paymentTypeRequest);
            redirectAttributes.addFlashAttribute("successMessage", "Payment method updated successfully!");
            if (imageFile != null && !imageFile.isEmpty() && !imageUrl.equals(existingPaymentType.getImageUrl())) {
                imageUploadService.deleteImage(existingPaymentType.getImageUrl());
            }

        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error uploading image: " + e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating payment method: " + e.getMessage());
        }
        return "redirect:/admins/settings/payment-methods";
    }

    @PostMapping("/delete/{id}")
    public String deletePaymentType(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            paymentTypeService.deletePaymentType(id);
            redirectAttributes.addFlashAttribute("successMessage", "Payment method deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admins/settings/payment-methods";
    }

    @PostMapping("/toggle-status/{id}")
    public String togglePaymentTypeStatus(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            paymentTypeService.toggleStatus(id);
            redirectAttributes.addFlashAttribute("successMessage", "Status updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating status: " + e.getMessage());
        }
        return "redirect:/admins/settings/payment-methods";
    }

}
