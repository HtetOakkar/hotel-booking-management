package com.example.hotelbookingmanagement.util.command_line_runner;

import com.example.hotelbookingmanagement.model.dto.UserDto;
import com.example.hotelbookingmanagement.model.entity.Role;
import com.example.hotelbookingmanagement.model.enums.RoleName;
import com.example.hotelbookingmanagement.repository.RoleRepository;
import com.example.hotelbookingmanagement.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Order(1)
@Slf4j
public class InitializeAdminCommandLineRunner implements CommandLineRunner {

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    private final RoleRepository roleRepository;


    @Override
    public void run(String... args) throws Exception {

        Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN).orElse(new Role());
        Role userRole = roleRepository.findByName(RoleName.ROLE_USER).orElse(new Role());
        Role staffRole = roleRepository.findByName(RoleName.ROLE_STAFF).orElse(new Role());

        if (adminRole.getName() == null) {
            adminRole.setName(RoleName.ROLE_ADMIN);
            roleRepository.save(adminRole);
            log.info("Admin role created.");
        } else {
            log.info("Admin role already exists.");
        }
        if (userRole.getName() == null) {
            userRole.setName(RoleName.ROLE_USER);
            roleRepository.save(userRole);
            log.info("User role created.");
        } else {
            log.info("User role already exists.");
        }

        if (staffRole.getName() == null) {
            staffRole.setName(RoleName.ROLE_STAFF);
            roleRepository.save(staffRole);
            log.info("Staff role created.");
        } else {
            log.info("Staff role already exists.");
        }

        String ADMIN_PASSWORD = "Admin123";
        String ADMIN_EMAIL = "admin@gmail.com";
        UserDto userDto = UserDto.builder()
                .email(ADMIN_EMAIL)
                .password(passwordEncoder.encode(ADMIN_PASSWORD))
                .fullName("Admin")
                .phoneNumber("0912345678")
                .build();

        if (!userService.existByEmail(ADMIN_EMAIL)) {
            userService.createAdmin(userDto);
            log.info("Admin user created with email: {} and password: {}", ADMIN_EMAIL, ADMIN_PASSWORD);
        } else {
            log.info("Admin user already exits.");
        }
    }
}
