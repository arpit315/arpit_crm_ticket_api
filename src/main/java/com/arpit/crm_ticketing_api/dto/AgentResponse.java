package com.arpit.crm_ticketing_api.dto;

import com.arpit.crm_ticketing_api.enums.Department;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AgentResponse {
    private Long id;
    private String name;
    private String email;
    private Department department;
}
