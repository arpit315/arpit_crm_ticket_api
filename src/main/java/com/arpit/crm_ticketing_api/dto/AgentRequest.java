package com.arpit.crm_ticketing_api.dto;

import com.arpit.crm_ticketing_api.enums.Department;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AgentRequest {
    @NotBlank
    private String name;

    @NotBlank
    @Email
    private String email;

    @NotNull
    private Department department;
}
