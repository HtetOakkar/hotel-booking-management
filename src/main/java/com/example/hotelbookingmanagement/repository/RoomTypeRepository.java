package com.example.hotelbookingmanagement.repository;

import com.example.hotelbookingmanagement.model.entity.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomTypeRepository extends JpaRepository<RoomType, Long> {
    @Query("SELECT DISTINCT rt FROM RoomType rt JOIN rt.amenities a WHERE a.id = :amenityId")
    List<RoomType> findDistinctByAmenitiesId(Long amenityId);

    @Query("SELECT rt FROM RoomType rt WHERE rt.isFeatured=true")
    List<RoomType> findAllByIsFeaturedTrue();

    @Query("SELECT DISTINCT rt FROM RoomType rt " +
            "WHERE rt.maxAdults >= :adults AND rt.maxChildren >= :children AND " +
            "(SELECT COUNT(r) FROM Room r WHERE r.roomType = rt) > " +
            "(SELECT COUNT(DISTINCT b.roomId) FROM Booking b JOIN Room r ON b.roomId = r.id WHERE r.roomType = rt AND " +
            "b.bookingStatus != 'CANCELLED' AND (b.checkInDate < :checkout AND b.checkOutDate > :checkin))")
    List<RoomType> findAvailableRoomTypes(String checkin, String checkout, Integer adults, Integer children);
}
