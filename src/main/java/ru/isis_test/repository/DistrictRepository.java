package ru.isis_test.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.isis_test.model.District;

import java.util.List;
import java.util.Optional;

public interface DistrictRepository extends CrudRepository<District, Integer> {
    @Query(" SELECT d FROM District d WHERE d.archiveStatus = false")
    List<District> getAll();

    Optional<District> findById(int id);

    @Query("SELECT d FROM District d WHERE d.archiveStatus = false and d.name = ?1")
    Optional<District> findByName(String name);

    @Query("SELECT d FROM District d WHERE d.archiveStatus = false and d.name like %?1%")
    List<District> getByName(String name);

    @Query("SELECT d FROM District d WHERE d.archiveStatus = false and d.name like %?1% and d.code like %?2%")
    List<District> getByNameAndCode(String name, String code);

    @Query("SELECT d FROM District d WHERE d.archiveStatus = false and d.code like %?1%")
    List<District> getByCode(String code);

    District saveAndFlush(District district);
}
