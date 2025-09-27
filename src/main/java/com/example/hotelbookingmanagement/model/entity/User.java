package com.example.hotelbookingmanagement.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "users", indexes = {
		@Index(name = "idx_email", columnList = "email"),
        @Index(name = "idx_phone_number", columnList = "phone_number")
})
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column(name = "full_name", nullable = false, length = 50)
    private String fullName;

    @Column(nullable = false, unique = true, length = 30)
    private String username;

    @Column(name = "profile_image_url")
    private String profileImageUrl;
	
	@Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "phone_number", nullable = false, unique = true, length = 15)
    private String phoneNumber;
	
	@Column(nullable = false, unique = true)
    private String password;
	
	@CreationTimestamp
	@Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;
	
	@UpdateTimestamp
	@Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean activated;

    @Version
    @Column(updatable = false, nullable = false, columnDefinition = "bigint default 0")
    private Long version;
	
	@ManyToOne
	@JoinColumn(name = "role_id", nullable = false, referencedColumnName = "id")
	private Role role;

    @OneToOne(cascade = CascadeType.DETACH, mappedBy = "user")
    private Guest guest;
}
