package org.ifsul.carhired.api.automaker;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.ifsul.carhired.api.base.BaseModelEntity;
import org.ifsul.carhired.api.model.Model;

import java.util.List;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "automakers")
public class Automaker extends BaseModelEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", unique = true, length = 100, nullable = false)
    private String name;

    @Column(name = "country", length = 100)
    private String country;

    @OneToMany(cascade = ALL, orphanRemoval = true, mappedBy = "automaker")
    private List<Model> models;
}
