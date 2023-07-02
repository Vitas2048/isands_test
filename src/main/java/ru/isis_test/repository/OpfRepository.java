package ru.isis_test.repository;

import org.springframework.data.repository.CrudRepository;
import ru.isis_test.model.OpfForm;

import java.util.Optional;

public interface OpfRepository extends CrudRepository<OpfForm, Integer> {

    Optional<OpfForm> findById(int id);

    Optional<OpfForm> findByName(String name);
}
