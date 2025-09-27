package com.example.hotelbookingmanagement.util.command_line_runner;

import com.example.hotelbookingmanagement.model.entity.Hotel;
import com.example.hotelbookingmanagement.repository.HotelRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Order(2)
@Slf4j
public class InitializeHotelCommmandLineRunner implements CommandLineRunner {

    private final HotelRepository hotelRepository;

    @Override
    public void run(String... args) throws Exception {
        if (hotelRepository.count() == 0) {
            // If no hotel exists, create a default one.
            Hotel defaultHotel = Hotel.builder()
                    .name("Aurora Mandalay")
                    .address("223, 73rd St, Mandalay, Myanmar")
                    .phoneNumber("(09) 1234 5678")
                    .email("contact@auroramandalay.com")
                    .description("Welcome to our hotel! Please update these details in the admin panel.")
                    .imageUrl("https://placehold.co/1200x800/3B82F6/FFFFFF?text=Welcome+to+Serene+Shores")
                    .location("https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d3700.417047227457!2d96.09176857577327!3d21.956951379934853!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x30cb6ddafbc34be9%3A0x61a78d782e6897f8!2sUniversity%20of%20Mandalay!5e0!3m2!1sen!2smm!4v1758305359205!5m2!1sen!2smm")
                    .build();

            hotelRepository.save(defaultHotel);
            log.info("Default hotel created successfully.");
        }
    }
}
