package com.arpit.crm_ticketing_api.dto;

import com.arpit.crm_ticketing_api.enums.Department;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AgentRequest {
    @NotBlank(message = "Name must not be blank")
    @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
    private String name;

    @NotBlank(message = "Email must not be blank")
    @Email(message = "Invalid email format")
    @Size(min = 4, max = 150, message = "Email must be between 4 and 150 characters")
    private String email;

    @NotNull(message = "Department is required")
    private Department department;
}
