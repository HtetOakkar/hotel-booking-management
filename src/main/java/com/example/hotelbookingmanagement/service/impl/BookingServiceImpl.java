package com.example.hotelbookingmanagement.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.mail.MessagingException;
import javax.validation.Valid;

import com.example.hotelbookingmanagement.model.entity.*;
import com.example.hotelbookingmanagement.repository.*;
import com.example.hotelbookingmanagement.service.EmailService;
import com.example.hotelbookingmanagement.util.EmailTemplateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.hotelbookingmanagement.exception.BadRequestException;
import com.example.hotelbookingmanagement.exception.EntityNotFoundException;
import com.example.hotelbookingmanagement.mapper.BookingMapper;
import com.example.hotelbookingmanagement.model.dto.BookingDto;
import com.example.hotelbookingmanagement.model.enums.BookingStatus;
import com.example.hotelbookingmanagement.model.enums.RoomStatus;
import com.example.hotelbookingmanagement.model.payload.request.BookingRequest;
import com.example.hotelbookingmanagement.service.BookingService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {


    private final BookingRepository bookingRepository;

    private final BookingMapper bookingMapper;
    
    private final GuestRepository guestRepository;
    
    private final PaymentTypeRepository paymentTypeRepository;
    
    private final RoomTypeRepository roomTypeRepository;
    
    private final RoomRepository roomRepository;
    
    private final UserRepository userRepository;

    private final EmailService emailService;

    private final HotelRepository hotelRepository;

    private String getHotelName() {
        Hotel hotel = hotelRepository.findById(1L).get();
        return hotel.getName();
    }

    @Override
    public Page<BookingDto> findAllBookings(Pageable pageable) {
    	return bookingRepository.findAll(pageable).map(bookingMapper::toBookingDto);
    }


	@Override
	public void confirmBooking(Long id) {
		
		Booking booking = bookingRepository.findById(id).get();
		booking.setBookingStatus(BookingStatus.CONFIRMED);
		Booking savedBooking = bookingRepository.save(booking);
        BookingDto bookingDto = bookingMapper.toBookingDto(savedBooking);

        Room bookedRoom = roomRepository.getById(booking.getRoomId());
        bookedRoom.setStatus(RoomStatus.BOOKED);
        roomRepository.save(bookedRoom);
        // Send booking confirmation email
        try {
            String hotelName = getHotelName();
            String emailBody = EmailTemplateUtil.buildBookingConfirmationEmail(booking.getGuest().getName(), bookingDto, hotelName);
            emailService.sendEmail(booking.getGuest().getEmail(), "Your Booking is Confirmed!", emailBody);
        } catch (Exception e) {
            log.error("Failed to send booking confirmation email", e);
        }
	}


	@Override
	public BookingDto createBooking(@Valid BookingRequest bookingRequest, Long userId) {

        // --- Date Parsing and Validation ---
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate checkInDate;
        LocalDate checkOutDate;
        try {
            checkInDate = LocalDate.parse(bookingRequest.getCheckInDate(), formatter);
            checkOutDate = LocalDate.parse(bookingRequest.getCheckOutDate(), formatter);
        } catch (Exception e) {
            throw new BadRequestException("Invalid date format. Please use YYYY-MM-DD.");
        }

        if (checkInDate.isAfter(checkOutDate) || checkInDate.isEqual(checkOutDate)) {
            throw new BadRequestException("Check-out date must be after check-in date.");
        }

        if (checkInDate.isBefore(LocalDate.now())) {
            throw new BadRequestException("Check-in date cannot be in the past.");
        }

		RoomType roomType = roomTypeRepository.findById(bookingRequest.getRoomTypeId())
                .orElseThrow(() -> new EntityNotFoundException("Room type not found"));

        // Validate guest numbers against room capacity
        if (bookingRequest.getAdults() > roomType.getMaxAdults()) {
            throw new BadRequestException("Number of adults exceeds the maximum capacity for this room type.");
        }
        if (bookingRequest.getChildren() > roomType.getMaxChildren()) {
            throw new BadRequestException("Number of children exceeds the maximum capacity for this room type.");
        }

        List<Room> availableRooms = roomRepository.findByRoomTypeIdAndStatus(bookingRequest.getRoomTypeId(), RoomStatus.AVAILABLE);
        if (availableRooms.isEmpty()) {
            throw new BadRequestException("No available rooms for the selected type.");
        }
        // Find or create the guest
        Guest guest = guestRepository.findByEmail(bookingRequest.getGuestEmail())
                .orElseGet(() -> {
                    Guest newGuest = new Guest();
                    newGuest.setName(bookingRequest.getGuestName());
                    newGuest.setEmail(bookingRequest.getGuestEmail());
                    newGuest.setPhoneNumber(bookingRequest.getGuestPhone());
                    if (userId != null) {
                        User user = userRepository.findById(userId).get();
                        newGuest.setUser(user);
                    }

                    return guestRepository.save(newGuest);
                });

        // Get payment type
        PaymentType paymentType = paymentTypeRepository.findById(bookingRequest.getPaymentTypeId())
                .orElseThrow(() -> new EntityNotFoundException("Payment type not found"));

        // Calculate nights
        long nights = ChronoUnit.DAYS.between(checkInDate, checkOutDate);

        // Create the booking
        Booking booking = Booking.builder()
                .roomId(availableRooms.get(0).getId())
                .roomType(roomType)
                .guest(guest)
                .checkInDate(bookingRequest.getCheckInDate())
                .checkOutDate(bookingRequest.getCheckOutDate())
                .adultsNumber(bookingRequest.getAdults())
                .childrenNumber(bookingRequest.getChildren())
                .nights((int) nights)
                .totalAmount(bookingRequest.getTotalAmount().multiply(BigDecimal.valueOf(nights)))
                .paymentType(paymentType)
                .specialRequests(bookingRequest.getSpecialRequests())
                .bookingStatus(BookingStatus.PENDING)
                .trackingId(UUID.randomUUID().toString())
                .build();

        Booking savedBooking = bookingRepository.save(booking);

//        // Update room status
//        availableRoom.setStatus(RoomStatus.BOOKED);
//        roomRepository.save(availableRoom);

        BookingDto bookingDto = bookingMapper.toBookingDto(savedBooking);

        String email = bookingRequest.getGuestEmail();
        String hotelName = getHotelName();
        String body = EmailTemplateUtil.buildBookingSuccessEmail(guest.getName(), bookingDto, hotelName);
        try {
            emailService.sendEmail(email, "Your Booking is Received!", body);
        } catch (MessagingException e) {
            log.error("Failed to send booking email", e);
        }
        bookingDto.setRoomTypeName(roomType.getName());
        return bookingDto;
	}

    @Override
    public BookingDto findByTrackingId(String trackingId) {
        return bookingRepository.findByTrackingId(trackingId)
                .map(bookingMapper::toBookingDto)
                .orElse(null);
    }

    @Override
    public void cancelBooking(String trackingId) {
        Booking booking = bookingRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found with tracking ID: " + trackingId));
        performCancellation(booking);
    }

    @Override
    public void cancelBookingById(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found with ID: " + bookingId));
        performCancellation(booking);
    }

    @Override
    public BookingDto findById(Long bookingId) {
        return bookingRepository.findById(bookingId)
                .map(bookingMapper::toBookingDto)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found with ID: " + bookingId));
    }

    @Override
    public List<BookingDto> findByUserId(Long userId) {
        return bookingRepository.findAllByGuestUser_Id(userId)
                .stream()
                .map(bookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingDto> findByGuestId(Long guestId) {
        return bookingRepository.findAllByGuest_Id(guestId)
                .stream()
                .map(bookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    private void performCancellation(Booking booking) {
        if (booking.getBookingStatus() != BookingStatus.PENDING && booking.getBookingStatus() != BookingStatus.CONFIRMED) {
            throw new BadRequestException("This booking cannot be cancelled as it is already " + booking.getBookingStatus().name().toLowerCase() + ".");
        }

        booking.setBookingStatus(BookingStatus.CANCELLED);

        Room room = roomRepository.findById(booking.getRoomId())
                .orElseThrow(() -> new EntityNotFoundException("Room associated with the booking not found."));
        room.setStatus(RoomStatus.AVAILABLE);
        roomRepository.save(room);

        bookingRepository.save(booking);

        try {
            String emailBody = EmailTemplateUtil.buildBookingCancellationEmail(booking);
            emailService.sendEmail(booking.getGuest().getEmail(), "Your Booking has been Cancelled - " + booking.getTrackingId(), emailBody);
        } catch (MessagingException e) {
            log.error("Failed to send booking cancellation email for tracking ID: {}", booking.getTrackingId(), e);
        }
    }


}
