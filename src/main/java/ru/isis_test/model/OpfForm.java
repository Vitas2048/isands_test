package ru.isis_test.model;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "opf")
@Getter@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class OpfForm {
    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
}
