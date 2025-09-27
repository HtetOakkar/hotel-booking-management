package com.example.hotelbookingmanagement.controller;

import java.util.List;

import javax.validation.Valid;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.hotelbookingmanagement.config.CurrentUser;
import com.example.hotelbookingmanagement.model.dto.BookingDto;
import com.example.hotelbookingmanagement.model.dto.HotelDto;
import com.example.hotelbookingmanagement.model.dto.PaymentTypeDto;
import com.example.hotelbookingmanagement.model.dto.RoomTypeDto;
import com.example.hotelbookingmanagement.model.dto.UserDto;
import com.example.hotelbookingmanagement.model.payload.request.BookingRequest;
import com.example.hotelbookingmanagement.service.BookingService;
import com.example.hotelbookingmanagement.service.HotelService;
import com.example.hotelbookingmanagement.service.PaymentTypeService;
import com.example.hotelbookingmanagement.service.RoomTypeService;
import com.example.hotelbookingmanagement.service.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/bookings")
@RequiredArgsConstructor
@Slf4j
public class BookingController {
    private final RoomTypeService roomTypeService;

    private final UserService userService;

    private final HotelService hotelService;

    private final PaymentTypeService paymentTypeService;

    private final BookingService bookingService;

    @ModelAttribute("hotel")
    public HotelDto hotelDto() {
        return hotelService.findFirstHotel();
    }

    @GetMapping("/{id}")
    public String bookingPage(@PathVariable Long id,
                              @RequestParam(required = false) String checkin,
                              @RequestParam(required = false) String checkout,
                              @CurrentUser UserDetails currentUser,
                              Model model,
                              RedirectAttributes redirectAttributes) {

        HotelDto hotel = hotelService.findFirstHotel();
        RoomTypeDto roomType = roomTypeService.findById(id);

        if (roomType == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "The selected room type could not be found.");
            return "redirect:/rooms";
        }

        List<PaymentTypeDto> paymentTypes = paymentTypeService.findAllActive();
        BookingRequest bookingRequest = new BookingRequest();

        if (currentUser != null) {
            UserDto userDto = userService.findByEmail(currentUser.getUsername());
            bookingRequest.setGuestName(userDto.getFullName());
            bookingRequest.setGuestEmail(userDto.getEmail());
            bookingRequest.setGuestPhone(userDto.getPhoneNumber());
        }

        model.addAttribute("hotel", hotel);
        model.addAttribute("roomType", roomType);
        model.addAttribute("checkinDate", checkin);
        model.addAttribute("checkoutDate", checkout);
        model.addAttribute("paymentTypes", paymentTypes);
        model.addAttribute("bookingRequest", bookingRequest);


        return "booking";
    }

    @PostMapping("/confirm")
    public String confirmBooking(@Valid @ModelAttribute("bookingRequest") BookingRequest bookingRequest,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes,
                                 @CurrentUser UserDetails currentUser,
                                 Model model) {
        if (bindingResult.hasErrors()) {
            // Log the binding errors to understand the issue
            bindingResult.getAllErrors().forEach(error -> log.error(error.toString()));
            log.error("There was an error placing your booking. Binding error");


            // Repopulate the model with necessary data for the booking form
            HotelDto hotel = hotelService.findFirstHotel();
            RoomTypeDto roomType = roomTypeService.findById(bookingRequest.getRoomTypeId());
            List<PaymentTypeDto> paymentTypes = paymentTypeService.findAllActive();

            model.addAttribute("hotel", hotel);
            model.addAttribute("roomType", roomType);
            model.addAttribute("paymentTypes", paymentTypes);
            model.addAttribute("bookingRequest", bookingRequest);

            if (currentUser != null) {
                model.addAttribute("user", userService.findByEmail(currentUser.getUsername()));
            }
            return "booking";
        }

        try {
            Long userId = null;
            if (currentUser != null) {
                UserDto userDto = userService.findByEmail(currentUser.getUsername());
                userId = userDto.getId();
            }
            BookingDto newBooking = bookingService.createBooking(bookingRequest, userId);
            redirectAttributes.addFlashAttribute("successMessage", "Your booking has been successfully placed!");
            redirectAttributes.addFlashAttribute("bookingDetails", newBooking);
            log.info("Booking has been successfully placed!");
            return "redirect:/bookings/confirmation";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "There was an error placing your booking: " + e.getMessage());
            log.error("There was an error placing your booking.");
            return "redirect:/bookings/" + bookingRequest.getRoomTypeId();
        }
    }

    @GetMapping("/confirmation")
    public String bookingConfirmation(Model model) {
        if (!model.containsAttribute("bookingDetails")) {
            return "redirect:/";
        }
        return "booking-confirmation";
    }

    @GetMapping("/details/{bookingId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String viewBookingDetails(@PathVariable Long bookingId, Model model) {
        BookingDto bookingDto = bookingService.findById(bookingId);
        model.addAttribute("booking", bookingDto);
        return "booking-details";
    }

    @PostMapping("/details/cancel/{bookingId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String cancelBookingUser(@PathVariable Long bookingId, RedirectAttributes redirectAttributes) {
        try {
            bookingService.cancelBookingById(bookingId);
            redirectAttributes.addFlashAttribute("successMessage", "Booking has been successfully cancelled.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error cancelling booking: " + e.getMessage());
        }
        return "redirect:/users/profile";
    }
}
