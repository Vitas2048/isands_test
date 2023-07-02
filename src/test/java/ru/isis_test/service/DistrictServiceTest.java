package ru.isis_test.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.isis_test.IsisTestApplication;
import ru.isis_test.core.exception.ApplicationException;
import ru.isis_test.model.District;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = IsisTestApplication.class)
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
class DistrictServiceTest {

    @Autowired
    private DistrictService districtService;

    private District district;

    private District district1;

    @BeforeEach
    public void init() {
        district = new District();
        district.setName("daw2");
        district.setCode("1110");

        district1 = new District();
        district1.setName("daw");
        district1.setCode("1111");
    }

    @AfterEach
    public void teardown() {
        district = null;
        district1 = null;
        districtService.deleteAll();
    }

    @Test
    public void whenSave() {
        int id = districtService.save(district1).getId();
        assertEquals(district1, districtService.findById(id).get());
    }


    @Test
    public void whenEdit() throws ApplicationException {
        int id = districtService.save(district).getId();
        District test = districtService.edit(district1, id);
        district1.setId(id);
        assertEquals(district1, test);
    }

    @Test
    public void whenArchive() throws ApplicationException {
        int id = districtService.save(district).getId();
        District test = districtService.toArchive(id);
        district.setArchiveStatus(true);
        assertEquals(district, test);
    }

    @Test
    public void whenFindById() {
        int id = districtService.save(district).getId();
        Optional<District> test = districtService.findById(id);
        assertEquals(Optional.ofNullable(district), test);
    }

    @Test
    public void whenGetByIdWrongThenThrowException() {
        districtService.save(district);
        assertThrows(ApplicationException.class, () -> {
            districtService.getById(22);
        });
    }

    @Test
    public void whenGetAll() {
        List<District> districts = new ArrayList<>();

        districtService.save(district);
        districtService.save(district1);

        districts.add(district);
        districts.add(district1);

        assertEquals(districts, districtService.getAll());
    }

    @Test
    public void whenGetByFilterNameAndCode() {
        districtService.save(district1);
        districtService.save(district);

        List<District> test = districtService.getByFilter("daw", "111");
        List<District> districts = new ArrayList<>();

        districts.add(district1);
        districts.add(district);

        assertEquals(districts, test);
    }

    @Test
    public void whenGetByFilterOnlyName() {
        districtService.save(district1);
        districtService.save(district);

        List<District> test = districtService.getByFilter("daw2", null);
        List<District> districts = new ArrayList<>();

        districts.add(district);

        assertEquals(districts, test);
    }

    @Test
    public void whenGetByFilterOnlyCode() {
        districtService.save(district1);
        districtService.save(district);

        List<District> test = districtService.getByFilter(null, "1111");
        List<District> districts = new ArrayList<>();

        districts.add(district1);

        assertEquals(districts, test);
    }

    @Test
    public void whenGetByFilterByNone() {
        districtService.save(district1);
        districtService.save(district);

        List<District> test = districtService.getByFilter(null, null);
        List<District> districts = new ArrayList<>();

        districts.add(district1);
        districts.add(district);

        assertEquals(districts, test);
    }

    @Test
    public void whenGetByFilterNotExistent() {
        districtService.save(district1);
        districtService.save(district);

        List<District> test = districtService.getByFilter("www", null);
        List<District> districts = new ArrayList<>();

        assertEquals(districts, test);
    }


}