package com.example.hotelbookingmanagement.controller;

import com.example.hotelbookingmanagement.model.dto.*;
import com.example.hotelbookingmanagement.model.enums.AmenityType;
import com.example.hotelbookingmanagement.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class NavbarController {

    private final HotelService hotelService;

    private final RoomTypeService roomTypeService;

    private final RoomService roomService;

    private final AmenityService amenityService;

    private final BookingService bookingService;

    private final MessageService messageService;

    @ModelAttribute("hotel")
    public HotelDto hotelDto() {
        return hotelService.findFirstHotel();
    }

    @GetMapping
    public String getHome(Model model) {
        model.addAttribute("featuredRooms", roomTypeService.findFeaturedRoomTypes());
        return "index";
    }

    @GetMapping("/rooms")
    public String getRooms(Model model) {
        model.addAttribute("allRooms", roomTypeService.findAll());
        model.addAttribute("searchPerformed", false);
        return "rooms";
    }

    @GetMapping("/rooms/search")
    public String searchRooms(@RequestParam("checkin") String checkin,
                              @RequestParam("checkout") String checkout,
                              @RequestParam("adults") Integer adults,
                              @RequestParam("children") Integer children,
                              Model model) {
        List<RoomTypeDto> availableRoomTypes = roomTypeService.searchAvailableRoomTypes(checkin, checkout, adults, children);
        model.addAttribute("allRooms", availableRoomTypes);
        model.addAttribute("searchPerformed", true);
        model.addAttribute("checkin", checkin);
        model.addAttribute("checkout", checkout);
        model.addAttribute("adults", adults);
        model.addAttribute("children", children);
        return "rooms";
    }

    @GetMapping("/rooms/{id}")
    public String getRoomDetails(@PathVariable Long id, Model model) {
        RoomTypeDto roomType = roomTypeService.findById(id);
        List<RoomDto> availableRooms = roomService.findAvailableRoomsByRoomTypeId(id);

        // Group amenities by type
        if (roomType != null && roomType.getAmenities() != null) {
            Map<AmenityType, List<AmenityDto>> groupedAmenities = roomType.getAmenities().stream()
                    .collect(Collectors.groupingBy(AmenityDto::getAmenityType));
            model.addAttribute("groupedAmenities", groupedAmenities);
        }

        model.addAttribute("roomType", roomType);
        model.addAttribute("availableRoomCount", availableRooms.size());
        return "room-details";
    }

    @GetMapping("/amenities")
    public String getAmenities(Model model) {
        model.addAttribute("amenityGroups", amenityService.findAndGroupSelectedAmenities());
        return "amenities";
    }

    @GetMapping("/contacts")
    public String getContact() {
        return "contacts";
    }

    @PostMapping("/contacts/send")
    public String sendMessage(@RequestParam("name") String name,
                              @RequestParam("email") String email,
                              @RequestParam("message") String message,
                              RedirectAttributes redirectAttributes) {
        MessageDto messageDto = new MessageDto();
        messageDto.setName(name);
        messageDto.setEmail(email);
        messageDto.setMessage(message);
        messageService.saveMessage(messageDto);
        redirectAttributes.addFlashAttribute("successMessage", "Your message has been sent successfully!");
        return "redirect:/contacts";
    }

    @GetMapping("/bookings/track")
    public String showTrackBookingPage() {
        return "booking-tracking";
    }

    @PostMapping("/bookings/track")
    public String trackBooking(@RequestParam("trackingId") String trackingId, Model model, RedirectAttributes redirectAttributes) {
        BookingDto booking = bookingService.findByTrackingId(trackingId);
        if (booking != null) {
            model.addAttribute("booking", booking);
        } else {
            model.addAttribute("errorMessage", "No booking found with the tracking ID: " + trackingId);
        }
        return "booking-tracking";
    }

    @PostMapping("/bookings/cancel/{trackingId}")
    public String cancelBooking(@PathVariable String trackingId, RedirectAttributes redirectAttributes) {
        try {
            bookingService.cancelBooking(trackingId);
            redirectAttributes.addFlashAttribute("successMessage", "Your booking has been successfully cancelled.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error cancelling booking: " + e.getMessage());
        }
        return "redirect:/bookings/track?trackingId=" + trackingId;
    }

    @GetMapping("/about-us")
    public String getAboutUs() {
        return "aboutus";
    }
}
