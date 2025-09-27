package com.example.hotelbookingmanagement.model.enums;

public enum AmenityType {
    HOTEL_WIDE("Hotel-Wide"), // For things like the pool, gym, etc.
    IN_ROOM("In-Room"),      // For things like a mini-bar, coffee maker, etc.
    RECREATION("Recreation"), // For things like spa, game room, etc.
    BUSINESS("Business"),    // For things like conference rooms, business center, etc.
    DINING("Dining");        // For things like restaurants, bars, room service, etc


    private final String displayName;

    AmenityType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
