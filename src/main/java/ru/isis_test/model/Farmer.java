package ru.isis_test.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    private String organizationName;

    private OpfForm opfForm;

    private String inn;

    private String kpp;

    private String ogm;

    private LocalDateTime regDate;

    private boolean archiveStatus;

    @ManyToMany
    @JoinTable(
            name = "district_farmer",
            joinColumns = {@JoinColumn(name = "farmer_id") },
            inverseJoinColumns = { @JoinColumn(name = "district_id")}
    )
    private List<District> districts = new ArrayList<>();
}
