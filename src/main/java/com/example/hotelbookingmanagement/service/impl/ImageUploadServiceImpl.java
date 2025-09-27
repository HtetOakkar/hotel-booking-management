package com.example.hotelbookingmanagement.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.hotelbookingmanagement.service.ImageUploadService;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;

@Service
@Slf4j
@RequiredArgsConstructor
public class ImageUploadServiceImpl implements ImageUploadService {

	private final Storage storage;

	@Value("${spring.cloud.gcp.storage.bucket-name}")
	private String bucketName;

	private String getFileExtension(String fileName) {
		if (fileName == null || fileName.lastIndexOf('.') == -1) {
			return "";
		}
		return fileName.substring(fileName.lastIndexOf('.'));
	}

	@Override
	public String uploadProfileImage(MultipartFile imageFile) throws IOException {

		return uploadImage(imageFile, "profiles");

	}

	@Override
	public void deleteImage(String imageUrl) {

		if (imageUrl == null || imageUrl.isEmpty() || !imageUrl.contains(bucketName)) {
			return;
		}

		try {
			// Extract the object name from the full URL
			String objectName = imageUrl.substring(imageUrl.indexOf(bucketName) + bucketName.length() + 1);
			BlobId blobId = BlobId.of(bucketName, objectName);
			storage.delete(blobId);
		} catch (Exception e) {
			System.err.println("Failed to delete GCS image: " + imageUrl + ". Error: " + e.getMessage());
		}
	}

    @Override
    public String uploadHotelImage(MultipartFile hotelImage) throws IOException {
        return uploadImage(hotelImage, "hotels");
    }

    @Override
    public String uploadRoomTypeImage(MultipartFile imageFile) throws IOException {

        return uploadImage(imageFile, "room-types");
    }

    @Override
    public String uploadPaymentImage(MultipartFile imageFile) throws IOException {

        return uploadImage(imageFile, "payments");
    }

    private String uploadImage(MultipartFile imageFile, String subDir) throws IOException {
		String uniqueFileName = subDir + "/" + UUID.randomUUID().toString().replace("-", "")
				+ getFileExtension(imageFile.getOriginalFilename());

		BlobId blobId = BlobId.of(bucketName, uniqueFileName);
		BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(imageFile.getContentType()).build();

		// Compress the image in memory before uploading
		byte[] compressedImage = imageFile.getBytes();

		// Upload the compressed image bytes to GCS
		storage.create(blobInfo, compressedImage);

		// Return the publicly accessible URL
		return "https://storage.googleapis.com/" + bucketName + "/" + uniqueFileName;
	}


	private byte[] compressImage(MultipartFile imageFile) throws IOException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		Thumbnails.of(imageFile.getInputStream()).size(512, 512).outputQuality(1).toOutputStream(outputStream);
		return outputStream.toByteArray();
	}
}
