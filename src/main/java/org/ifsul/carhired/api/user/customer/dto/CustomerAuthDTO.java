package org.ifsul.carhired.api.user.customer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerAuthDTO implements Serializable {
    private Long id;
    private String email;
    private String name;
    private String cpf;
    private String phone;
    private String address;
}
