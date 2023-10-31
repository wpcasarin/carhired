package org.ifsul.carhired.api.user.customer;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.ifsul.carhired.api.user.User;


@Getter
@SuperBuilder
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@DiscriminatorValue("C")
public class Customer extends User {
    @Column(name = "name", length = 100, nullable = false)
    private String name;
    @Column(name = "cpf", length = 11, unique = true, nullable = false)
    private String cpf;
    @Column(name = "phone", length = 20)
    private String phone;
    @Column(name = "address")
    private String address;
}
