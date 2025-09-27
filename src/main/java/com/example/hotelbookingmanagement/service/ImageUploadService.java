package com.example.hotelbookingmanagement.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageUploadService {

    String uploadProfileImage(MultipartFile imageFile) throws IOException;

    void deleteImage(String imageUrl);

    String uploadHotelImage(MultipartFile hotelImage) throws IOException;

    String uploadRoomTypeImage(MultipartFile imageFile) throws IOException;

    String uploadPaymentImage(MultipartFile imageFile) throws IOException;
}
