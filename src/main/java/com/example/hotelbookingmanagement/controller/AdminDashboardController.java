package com.example.hotelbookingmanagement.controller;

import com.example.hotelbookingmanagement.config.CurrentUser;
import com.example.hotelbookingmanagement.mapper.AmenityMapper;
import com.example.hotelbookingmanagement.mapper.PaymentTypeMapper;
import com.example.hotelbookingmanagement.mapper.RoomMapper;
import com.example.hotelbookingmanagement.mapper.RoomTypeMapper;
import com.example.hotelbookingmanagement.model.dto.*;
import com.example.hotelbookingmanagement.model.enums.AmenityType;
import com.example.hotelbookingmanagement.model.enums.RoomStatus;
import com.example.hotelbookingmanagement.model.payload.request.AmenityRequest;
import com.example.hotelbookingmanagement.model.payload.request.PaymentTypeRequest;
import com.example.hotelbookingmanagement.model.payload.request.RoomRequest;
import com.example.hotelbookingmanagement.model.payload.request.RoomTypeRequest;
import com.example.hotelbookingmanagement.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admins")
@RequiredArgsConstructor
@Slf4j
public class AdminDashboardController {

    private final UserService userService;

    private final HotelService hotelService;

    private final ImageUploadService imageUploadService;

    private final RoomTypeService roomTypeService;

    private final AmenityService amenityService;

    private final AmenityMapper amenityMapper;

    private final RoomService roomService;

    private final RoomMapper roomMapper;

    private final RoomTypeMapper roomTypeMapper;

    private final PaymentTypeMapper paymentTypeMapper;

    private final PaymentTypeService paymentTypeService;

    private final BookingService bookingService;

    private final MessageService messageService;

    private final GuestService guestService;

    private final DashboardService dashboardService;

    @ModelAttribute("hotel")
    public HotelDto addHotelToModel() {
        return hotelService.findFirstHotel();
    }

    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String adminDashBoard(Model model, @CurrentUser UserDetails currentUser) {
        if (currentUser == null) {
            return "redirect:/admins/login";
        }
        addAdminDetailsToModel(model, currentUser);
        model.addAttribute("dashboardData", dashboardService.getDashboardData());
        model.addAttribute("recentMessages", messageService.findRecentMessages());
        return "/admin/admin-dashboard";
    }

    @GetMapping("/bookings")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String manageBookings(Model model,
                                 @RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "8") int size,
                                 @CurrentUser UserDetails currentUser) {
        addAdminDetailsToModel(model, currentUser);
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        Page<BookingDto> bookings = bookingService.findAllBookings(pageable);
        model.addAttribute("bookingsPage", bookings);
        return "/admin/admin-bookings";
    }

    @GetMapping("/bookings/details/{bookingId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String viewBookingDetails(@PathVariable Long bookingId, Model model) {
        BookingDto bookingDto = bookingService.findById(bookingId);
        model.addAttribute("booking", bookingDto);
        return "admin/admin-booking-details";
    }

    @PostMapping("/bookings/confirm/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String confirmBooking(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            bookingService.confirmBooking(id);
            redirectAttributes.addFlashAttribute("successMessage", "Booking confirmed successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error confirming booking: " + e.getMessage());
        }
        return "redirect:/admins/bookings";
    }

    @PostMapping("/bookings/cancel/{bookingId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String cancelBookingAdmin(@PathVariable Long bookingId, RedirectAttributes redirectAttributes) {
        try {
            bookingService.cancelBookingById(bookingId);
            redirectAttributes.addFlashAttribute("successMessage", "Booking has been successfully cancelled.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error cancelling booking: " + e.getMessage());
        }
        return "redirect:/admins/bookings";
    }

    @GetMapping("/guests")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String manageGuests(Model model, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "6") int size) {
        Page<GuestDto> guestsPage = guestService.findAllPaginated(PageRequest.of(page, size, Sort.by("name").ascending()));
        model.addAttribute("guestsPage", guestsPage);
        return "/admin/admin-guests";
    }

    @GetMapping("/guests/details/{guestId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String viewGuestDetails(@PathVariable Long guestId, Model model) {
        GuestDto guest = guestService.findById(guestId);
        List<BookingDto> bookings = bookingService.findByGuestId(guestId);
        model.addAttribute("guest", guest);
        model.addAttribute("bookings", bookings);
        return "admin/admin-guest-details";
    }



    @GetMapping("/settings/hotel")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String showHotelInfoPage(Model model, @CurrentUser UserDetails currentUser) {
        addAdminDetailsToModel(model, currentUser);

        HotelDto hotel = hotelService.findFirstHotel();

        if (hotel != null) {
            model.addAttribute("hotel", hotel);
        } else {
            model.addAttribute("hotel", new HotelDto());
        }

        return "admin/hotel-info";
    }

    @PostMapping("/settings/hotel")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String updateHotelInfo(@ModelAttribute("hotel") HotelDto hotel,
                                  @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                                  RedirectAttributes redirectAttributes) {
        try {
            HotelDto existingHotel = hotelService.findFirstHotel();
            String newHotelImageUrl = existingHotel.getImageUrl();

            if (imageFile != null && !imageFile.isEmpty()) {
                newHotelImageUrl = imageUploadService.uploadHotelImage(imageFile);
            }

            hotelService.updateHotelInfo(hotel, newHotelImageUrl);
            if (imageFile != null && !imageFile.isEmpty() && !newHotelImageUrl.equals(existingHotel.getImageUrl())) {
                imageUploadService.deleteImage(existingHotel.getImageUrl());
            }
            redirectAttributes.addAttribute("successMessage", "Hotel information updated successfully!");

        } catch (IOException e) {
            log.error("Error while uploading image.", e);
            redirectAttributes.addAttribute("errorMessage", "Error uploading hotel image: " + e.getMessage());
        } catch (Exception e) {
            log.error("Error while updating hotel information.", e);
            redirectAttributes.addAttribute("errorMessage", "Error updating hotel information: " + e.getMessage());
        }

        return "redirect:/admins/settings/hotel";
    }

    @GetMapping("/settings/room-types")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String showRoomTypesPage(Model model,
                                    @RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "5") int size, @CurrentUser UserDetails currentUser) {
        addAdminDetailsToModel(model, currentUser);
        // Fetch paginated data from the service
        Page<RoomTypeDto> roomTypesPage = roomTypeService.findAllPaginated(PageRequest.of(page, size));

        model.addAttribute("roomTypesPage", roomTypesPage);
        model.addAttribute("roomType", new RoomTypeDto()); // For the add/edit form
        return "admin/room-types";
    }

    @GetMapping("/settings/amenities")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String showAmenitiesPage(Model model,
                                    @RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "8") int size,
                                    @CurrentUser UserDetails currentUser) {
        addAdminDetailsToModel(model, currentUser);
        Page<AmenityDto> amenityDtosPage = amenityService.findAllPaginated(PageRequest.of(page, size));
        Page<AmenityRequest> amenitiesPage = amenityDtosPage.map(amenityMapper::toRequest);

        model.addAttribute("amenitiesPage", amenitiesPage);
        model.addAttribute("amenityTypes", AmenityType.values());
        model.addAttribute("roomTypes", roomTypeService.findAll());
        model.addAttribute("amenity", new AmenityRequest());
        return "admin/amenities";
    }

    @GetMapping("/settings/rooms")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String showRoomsPage(Model model,
                                @CurrentUser UserDetails currentUser,
                                @RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "8") int size) {
        addAdminDetailsToModel(model, currentUser);
        Page<RoomDto> roomsDtoPage = roomService.findAllPaginated(PageRequest.of(page, size));
        Page<RoomRequest> roomsPage = roomsDtoPage.map(roomMapper::toRoomRequest);
        List<RoomTypeDto> roomTypeDtos = roomTypeService.findAll();
        List<RoomTypeRequest> roomTypes = roomTypeDtos.stream().map(roomTypeMapper::toRoomTypeRequest).collect(Collectors.toList());
        model.addAttribute("roomsPage", roomsPage);
        model.addAttribute("roomTypes", roomTypes);
        model.addAttribute("roomStatuses", RoomStatus.values());
        model.addAttribute("room", new RoomRequest());
        return "/admin/rooms";
    }

    @GetMapping("/settings/payment-methods")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String showPaymentMethodsPage(Model model,
                                         @RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "8") int size,
                                         @CurrentUser UserDetails currentUser) {
        addAdminDetailsToModel(model, currentUser);
        Page<PaymentTypeDto> paymentTypesDtosPage = paymentTypeService.findAllPaginated(PageRequest.of(page, size));
        Page<PaymentTypeRequest> paymentTypesPage = paymentTypesDtosPage.map(paymentTypeMapper::toRequest);
        model.addAttribute("paymentTypesPage", paymentTypesPage);
        model.addAttribute("paymentType", new PaymentTypeRequest());
        return "admin/payment-methods";
    }

    @GetMapping("/profile/change-password")
    public String showChangePasswordPage(@CurrentUser UserDetails currentUser, Model model) {
        addAdminDetailsToModel(model, currentUser);
        return "admin/change-password";
    }
    private void addAdminDetailsToModel(Model model, UserDetails currentUser) {
        UserDto userDto = userService.findByEmail(currentUser.getUsername());
        model.addAttribute("adminFullName", userDto.getFullName());
    }

}
