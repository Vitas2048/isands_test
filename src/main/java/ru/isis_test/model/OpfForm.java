package ru.isis_test.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "opf")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class OpfForm {
    @Id
    @EqualsAndHashCode.Include
    private int id;

    private String name;
}
