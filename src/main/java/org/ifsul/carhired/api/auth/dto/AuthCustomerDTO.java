package org.ifsul.carhired.api.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ifsul.carhired.api.user.customer.dto.CustomerAuthDTO;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthCustomerDTO implements Serializable {
    private CustomerAuthDTO user;
    private String token;
}
