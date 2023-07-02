package ru.isis_test.service;

import com.google.gson.Gson;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.isis_test.IsisTestApplication;
import ru.isis_test.core.exception.ApplicationException;
import ru.isis_test.config.specification.FarmerSpecificationsBuilder;
import ru.isis_test.model.District;
import ru.isis_test.model.Farmer;
import ru.isis_test.core.message.FarmerDto;
import ru.isis_test.model.OpfForm;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest(classes = IsisTestApplication.class)
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")

class FarmerServiceTest {

    @Autowired
    private FarmerService farmerService;

    @Autowired
    private OpfService opfService;

    @Autowired
    private DistrictService districtService;

    private Farmer farmer;

    private Farmer farmer1;

    private District district;

    private OpfForm opfForm;

    private FarmerDto farmerDto;

    @BeforeEach
    public void init() throws ApplicationException {
        district = new District();
        district.setName("adw");

        districtService.save(district);

        opfForm = opfService.getByName("ЮР");

        farmerDto = new FarmerDto();
        farmerDto.setOrganizationName("VV");
        farmerDto.setInn("1234567890");
        farmerDto.setOpf(opfForm.getName());
        farmerDto.setRegDistrict(district.getName());

        farmer = new Farmer();
        farmer.setOrganizationName("VV");
        farmer.setInn("1234567890");
        farmer.setOpf(opfForm);
        farmer.setRegDistrict(district);

        farmer1 = new Farmer();
        farmer1.setOrganizationName("asdwd");
        farmer1.setInn("1234567891");
        farmer1.setRegDistrict(district);
        farmer1.setOpf(opfForm);
    }

    @AfterEach
    public void tearDown() {
        farmerService.deleteAll();
        districtService.deleteAll();

        farmerDto = null;
        farmer = null;
        farmer1 = null;
        district = null;
        opfForm = null;
    }

    @Test
    public void whenGetById() throws ApplicationException {
        int id = farmerService.save(farmer).getId();
        assertEquals(farmer, farmerService.getById(id));
    }

    @Test
    public void whenGetByWrongIdThenThrowException() {
        assertThrows(ApplicationException.class, () -> {
            farmerService.getById(554);
        });
    }

    @Test
    public void whenSaveByDto() throws ApplicationException {

        int id = farmerService.saveByDto(farmerDto).getId();

        farmer.setId(id);

        assertEquals(farmer, farmerService.getById(id));
    }

    @Test
    public void whenSaveWrongByDtoThenThrowException() {
        FarmerDto wrongDto = new FarmerDto();
        assertThrows(ApplicationException.class, () -> {
            farmerService.saveByDto(wrongDto);
        });
    }

    @Test
    public void whenSave() throws ApplicationException {
        int id = farmerService.save(farmer).getId();
        assertEquals(farmer, farmerService.getById(id));
    }

    @Test
    public void whenToDtoById() throws ApplicationException {
        int id = farmerService.save(farmer).getId();
        farmerDto.setId(id);
        Gson gson = new Gson();
        assertEquals(gson.toJson(farmerDto), gson.toJson(farmerService.toDtoById(id)));
    }

    @Test
    public void whenToDtoByWrongIdThenThrowException() {
        assertThrows(ApplicationException.class, () -> {
            farmerService.toDtoById(5);
        });
    }

    @Test
    public void whenToFarmer() throws ApplicationException {
        assertEquals(farmer, farmerService.toFarmer(farmerDto));
    }

    @Test
    public void whenToFarmerWrongThenThrowException() {
        assertThrows(ApplicationException.class, () -> {
            farmerService.toFarmer(new FarmerDto());
        });
    }

    @Test
    public void whenEditById() throws ApplicationException {
        farmerService.save(farmer1);
        farmerService.editById(1, farmerDto);
        farmer.setId(1);
        assertEquals(farmer, farmerService.getById(1));
    }

    @Test
    public void whenWrongEditThenThrowException() {
        farmerService.save(farmer1);

        assertThrows(ApplicationException.class,() -> {
            farmerService.editById(1, new FarmerDto());
        });
    }

    @Test
    public void whenArchive() throws ApplicationException {
        int id = farmerService.save(farmer).getId();
        farmerService.toArchive(id);

        farmer.setArchiveStatus(true);

        assertEquals(farmer, farmerService.getById(id));
    }

    @Test
    public void whenArchiveWrongIdThenThrowException() {
        assertThrows(ApplicationException.class, () -> {
            farmerService.toArchive(4);
        });
    }

    @Test
    public void whenGetAllBySpecification() {
        farmerService.save(farmer1);
        farmerService.save(farmer);

        List<Farmer> farmerList = new ArrayList<>();

        FarmerSpecificationsBuilder builder = new FarmerSpecificationsBuilder();
        builder.with("inn", ":", "12345");
        Specification<Farmer> spec = builder.build();

        farmerList.add(farmer1);
        farmerList.add(farmer);

        assertEquals(farmerList, farmerService.getAll(spec));
    }

    @Test
    public void whenGetAllBySpecification2() {
        farmerService.save(farmer1);
        farmerService.save(farmer);

        List<Farmer> farmerList = new ArrayList<>();

        FarmerSpecificationsBuilder builder = new FarmerSpecificationsBuilder();
        builder.with("inn", ":", "12345");
        builder.with("organizationName", ":", "VV");
        Specification<Farmer> spec = builder.build();

        farmerList.add(farmer);

        assertEquals(farmerList, farmerService.getAll(spec));
    }

    @Test
    public void whenGetAllDto() {
        int id1 = farmerService.save(farmer).getId();
        int id2 = farmerService.save(farmer1).getId();

        FarmerDto farmerDto1 = farmerService.toDto(farmer1);

        List<FarmerDto> dtos = new ArrayList<>();
        farmerDto.setId(id1);
        farmerDto1.setId(id2);
        dtos.add(farmerDto);
        dtos.add(farmerDto1);

        Gson gson = new Gson();
        assertEquals(gson.toJson(dtos), gson.toJson(farmerService.AllToDto(farmerService.getAll())));
    }

}