package ru.isis_test.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "farmer")
@NoArgsConstructor
public class Farmer {


    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull(message = "Required")
    @NotEmpty(message = "Required")
    private String organizationName;

    @ManyToOne(fetch = FetchType.LAZY)
    private OpfForm opf;

    @Pattern(regexp = "\\d{10}", message = "only 10 digits")
    @NotNull(message = "Required")
    @NotEmpty(message = "Required")
    private String inn;

    @Pattern(regexp = "[0-9]+", message = "only numbers")
    private String kpp;

    @Pattern(regexp = "[0-9]+", message = "only numbers")
    private String ogrn;

    private LocalDate regDate = LocalDate.now();

    private boolean archiveStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    private District regDistrict;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "district_farmer",
            joinColumns = {@JoinColumn(name = "farmer_id") },
            inverseJoinColumns = { @JoinColumn(name = "district_id")}
    )
    private Set<District> fieldDistricts = new HashSet<>();
}
