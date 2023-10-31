package org.ifsul.carhired.api.rental;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.ifsul.carhired.api.base.BaseModelEntity;
import org.ifsul.carhired.api.user.customer.Customer;
import org.ifsul.carhired.api.vehicle.Vehicle;

import java.time.LocalDate;

import static jakarta.persistence.CascadeType.REFRESH;
import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "rentals")
public class Rental extends BaseModelEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "due_date", columnDefinition = "DATE", nullable = false)
    private LocalDate dueDate;

    @Column(name = "return_date", columnDefinition = "DATE")
    private LocalDate returnedAt;

    @ManyToOne(cascade = REFRESH)
    @JoinColumn(name = "vehicle_id", referencedColumnName = "id", nullable = false)
    private Vehicle vehicle;

    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id", nullable = false)
    private Customer customer;

}
