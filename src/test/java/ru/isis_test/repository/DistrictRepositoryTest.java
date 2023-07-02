package ru.isis_test.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.isis_test.IsisTestApplication;
import ru.isis_test.model.District;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest(classes = IsisTestApplication.class)
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
class DistrictRepositoryTest {

    @Autowired
    private DistrictRepository districtRepository;

    private District district;

    private District district1;

    @BeforeEach
    private void init() {
        district =  new District();
        district.setName("Okt1");
        district.setCode("1123");

        district1 = new District();
        district1.setName("Okt2");
        district1.setCode("1124");

        districtRepository.save(district);
        districtRepository.save(district1);
    }

    @AfterEach
    private void teardown() {
        districtRepository.deleteAll();

        district = null;
        district1 = null;
    }

    @Test
    public void whenGetAll() {
        List<District> districts = new ArrayList<>();
        districts.add(district);
        districts.add(district1);

        List<District> districtsTest = districtRepository.getAll();

       assertEquals(districts, districtsTest);
    }

    @Test
    public void whenFindById() {
       assertEquals(districtRepository.findById(district.getId()).get(), district);
    }

    @Test
    public void  whenNotFindByIdThenEmpty() {
        assertEquals(districtRepository.findById(10), Optional.empty());
    }

    @Test
    public void whenFindByName() {
        assertEquals(districtRepository.findByName(district1.getName()).get(), district1);
    }

    @Test
    public void whenGetByNameFew() {
        List<District> districts = new ArrayList<>();
        districts.add(district);
        districts.add(district1);

        assertEquals(districts, districtRepository.getByName("Okt"));
    }

    @Test
    public void whenGetByCode() {
        List<District> districts = new ArrayList<>();
        districts.add(district);

        assertEquals(districts, districtRepository.getByCode(district.getCode()));
    }

    @Test
    public void whenGetByCodAndName() {
        List<District> districts = new ArrayList<>();
        districts.add(district1);

        assertEquals(districts,
                districtRepository.getByNameAndCode(district1.getName(), district1.getCode()));
    }

}