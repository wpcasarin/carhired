package org.ifsul.carhired.api.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.ifsul.carhired.api.automaker.Automaker;
import org.ifsul.carhired.api.base.BaseModelEntity;
import org.ifsul.carhired.api.vehicle.Vehicle;

import java.time.LocalDate;
import java.util.List;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "models")
public class Model extends BaseModelEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", unique = true, length = 100, nullable = false)
    private String name;

    @JsonProperty("releasedAt")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonSerialize(using = LocalDateSerializer.class)
    @Column(name = "released_at", columnDefinition = "DATE")
    private LocalDate releasedAt;

    @ManyToOne
    @JoinColumn(name = "automaker_id", referencedColumnName = "id", nullable = false)
    private Automaker automaker;

    @OneToMany(cascade = ALL, orphanRemoval = true, mappedBy = "model")
    private List<Vehicle> vehicles;
}
