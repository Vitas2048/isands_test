package ru.isis_test.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


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

    @NotNull(message = "Required")
    @NotEmpty(message = "Required")
    private String name;

    private String code;

    private boolean archiveStatus;
}
