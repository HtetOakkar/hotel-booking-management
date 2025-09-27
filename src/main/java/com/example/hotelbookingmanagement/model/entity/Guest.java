package com.example.hotelbookingmanagement.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "guests",
         indexes = {
              @Index(name = "idx_guest_email", columnList = "email"),
              @Index(name = "idx_guest_phone_number", columnList = "phoneNumber")
         }
)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Guest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 100, nullable = false)
    private String name;
    @Column(length = 100, nullable = false, unique = true)
    private String email;
    @Column(length = 15, nullable = false, unique = true)
    private String phoneNumber;
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
    @OneToMany(mappedBy = "guest", fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    private List<Booking> bookings;
}
