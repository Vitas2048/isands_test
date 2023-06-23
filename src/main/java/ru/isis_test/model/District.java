package ru.isis_test.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter@Setter
@Entity
@Table(name = "district")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class District {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private String code;

    private boolean archiveStatus;
    @OneToMany(mappedBy = "district", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Farmer> farmers = new ArrayList<>();
}
