package ru.isis_test.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.isis_test.model.Farmer;

import java.util.List;
import java.util.Optional;

public interface FarmerRepository extends CrudRepository<Farmer, Integer>, JpaSpecificationExecutor<Farmer> {
    @Query("SELECT f FROM Farmer f WHERE f.archiveStatus = false")
    @EntityGraph(attributePaths = {"regDistrict", "fieldDistricts", "opf"})
    List<Farmer> findAll();

    @EntityGraph(attributePaths = {"regDistrict", "fieldDistricts", "opf"})
    Optional<Farmer> findById(int id);

}
