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
import ru.isis_test.model.Farmer;
import ru.isis_test.model.OpfForm;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest(classes = IsisTestApplication.class)
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
class FarmerRepositoryTest {
    @Autowired
    private FarmerRepository farmerRepository;

    @Autowired
    private OpfRepository opfRepository;

    @Autowired
    private DistrictRepository districtRepository;

    private Farmer farmer;

    private Farmer farmer1;


    @BeforeEach
    public void init() {
        District district = new District();
        district.setName("adw");

        districtRepository.save(district);

        OpfForm form = opfRepository.findById(1).get();

        farmer = new Farmer();
        farmer.setOrganizationName("VV");
        farmer.setInn("1234567890");
        farmer.setOpf(form);
        farmer.setRegDistrict(district);

        farmer1 = new Farmer();
        farmer1.setOrganizationName("asdwd");
        farmer1.setInn("1234567891");
        farmer1.setRegDistrict(district);
        farmer1.setOpf(form);

        farmerRepository.save(farmer);
        farmerRepository.save(farmer1);
    }

    @AfterEach
    public void teardown() {
        farmer = null;
        farmer1 = null;

        farmerRepository.deleteAll();
        districtRepository.deleteAll();
    }
    @Test
    public void whenGetAll() {
        List<Farmer> farmers = new ArrayList<>();
        farmers.add(farmer);
        farmers.add(farmer1);

       assertEquals(farmers, farmerRepository.findAll());
    }

    @Test
    public void whenFindById() {
        assertEquals(farmer, farmerRepository.findById(farmer.getId()).get());
    }

    @Test void  whenNotFoundReturnEmpty() {
        assertEquals(Optional.empty(), farmerRepository.findById(23));
    }

}