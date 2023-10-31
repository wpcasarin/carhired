package org.ifsul.carhired.api.user.admin.dto;

import lombok.Data;
import org.ifsul.carhired.api.user.Role;

@Data
public class AdminDTO {
    private Long id;
    private String email;
    private Role role;
}
