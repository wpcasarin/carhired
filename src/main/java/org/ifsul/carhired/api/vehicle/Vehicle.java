package org.ifsul.carhired.api.vehicle;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.ifsul.carhired.api.base.BaseModelEntity;
import org.ifsul.carhired.api.model.Model;
import org.ifsul.carhired.api.rental.Rental;

import java.math.BigDecimal;
import java.util.List;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "vehicles")
public class Vehicle extends BaseModelEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "license_plaque", length = 7, nullable = false, unique = true)
    private String licensePlaque;

    @Column(name = "color", length = 50, nullable = false)
    private String color;

    @Column(name = "num_doors", updatable = false, nullable = false)
    private Byte numDoors;

    @Column(name = "mileage", nullable = false)
    private Integer mileage;

    @Column(name = "renavam", length = 11, updatable = false, nullable = false, unique = true)
    private String renavam;

    @Column(name = "chassis", length = 17, updatable = false, nullable = false, unique = true)
    private String chassis;

    @Column(name = "rent_price", precision = 17, scale = 2, nullable = false)
    private BigDecimal rentPrice;

    @Column(name = "available", nullable = false)
    private Boolean available;

    @ManyToOne
    @JoinColumn(name = "model_id", referencedColumnName = "id", nullable = false)
    private Model model;

    @OneToMany(cascade = ALL, orphanRemoval = true, mappedBy = "vehicle")
    private List<Rental> rentals;

}
