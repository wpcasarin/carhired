package org.ifsul.carhired.api.user.customer.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

@Data
@EqualsAndHashCode(callSuper = true)
public class CustomerDTO extends RepresentationModel<CustomerDTO> {
    private Long id;
    private String email;
    private String name;
    private String cpf;
    private String phone;
    private String address;
}
