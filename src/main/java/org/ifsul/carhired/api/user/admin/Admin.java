package org.ifsul.carhired.api.user.admin;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.ifsul.carhired.api.user.User;

@Getter
@SuperBuilder
@AllArgsConstructor
@Entity
@DiscriminatorValue("A")
public class Admin extends User {
}
