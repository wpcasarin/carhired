package org.ifsul.carhired.api.user.customer.dto;

import lombok.Data;

@Data
public class CustomerRentalDTO {
    private String email;
    private String name;
    private String cpf;
    private String phone;
    private String address;
}
