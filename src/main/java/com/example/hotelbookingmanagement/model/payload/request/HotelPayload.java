package com.example.hotelbookingmanagement.model.payload.request;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HotelPayload {
    private Long id;
    private String name;
    private String address;
    private String phoneNumber;
    private String email;
    private String description;
    private String imageUrl;
    private String location;
}
