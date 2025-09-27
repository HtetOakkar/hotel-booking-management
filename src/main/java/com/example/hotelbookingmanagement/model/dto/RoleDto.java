package com.example.hotelbookingmanagement.model.dto;

import com.example.hotelbookingmanagement.model.enums.RoleName;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RoleDto {
    private Long id;
    private RoleName name;
}
