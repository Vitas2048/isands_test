package ru.isis_test.controller;

import com.google.gson.Gson;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.isis_test.IsisTestApplication;
import ru.isis_test.core.exception.ApplicationException;
import ru.isis_test.config.specification.FarmerSpecificationsBuilder;
import ru.isis_test.model.Farmer;
import ru.isis_test.core.message.FarmerDto;
import ru.isis_test.service.FarmerService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("main")
@SpringBootTest(classes = IsisTestApplication.class)
@AutoConfigureMockMvc
class FarmerControllerTest {

    @MockBean
    private FarmerService farmerService;

    @Autowired
    private MockMvc mock;

    private FarmerDto farmerDto;

    private FarmerDto farmer2Dto;

    private FarmerDto archivedFarmerDto;

    private Gson gson;

    @BeforeEach
    void initDto() {

        farmerDto = new FarmerDto();
        farmerDto.setId(1);
        farmerDto.setInn("1123431231");
        farmerDto.setOrganizationName("wasd");
        farmerDto.setOpf("12354123");
        farmerDto.getFieldDistricts().add("district2");
        farmerDto.setRegDistrict("district2");
        farmerDto.setRegDate(LocalDate.now().toString());
        farmerDto.setKpp("1234");
        farmerDto.setOgrn("222");

        farmer2Dto = new FarmerDto();
        farmer2Dto.setId(2);
        farmer2Dto.setInn("1123431233");
        farmer2Dto.setOrganizationName("wasd1");
        farmer2Dto.setOpf("12354123");
        farmer2Dto.getFieldDistricts().add("district2");
        farmer2Dto.setRegDistrict("district3");
        farmer2Dto.setRegDate(LocalDate.now().toString());
        farmer2Dto.setKpp("1234");
        farmer2Dto.setOgrn("222");

        archivedFarmerDto = new FarmerDto();
        archivedFarmerDto.setId(1);
        archivedFarmerDto.setInn("1123431231");
        archivedFarmerDto.setOrganizationName("wasd");
        archivedFarmerDto.setOpf("12354123");
        archivedFarmerDto.getFieldDistricts().add("district2");
        archivedFarmerDto.setRegDistrict("district2");
        archivedFarmerDto.setRegDate(LocalDate.now().toString());
        archivedFarmerDto.setKpp("1234");
        archivedFarmerDto.setOgrn("222");
        archivedFarmerDto.setArchiveStatus(true);

        gson = new Gson();
    }

    @AfterEach
    void teardown() {
        farmerDto = null;
        farmer2Dto = null;
        archivedFarmerDto = null;
        gson = null;
    }

    @Test
    public void whenAddNewWithoutException() throws ApplicationException, Exception {

        when(farmerService.saveByDto(farmerDto)).thenReturn(farmerDto);

        MvcResult result = mock.perform(post("http://localhost:8080/api/farmers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(farmerDto)))
                        .andExpect(status().isOk()).andReturn();
    }

    @Test
    public void whenAddNotValid() throws Exception {
        FarmerDto farmer1Dto = new FarmerDto();
        MvcResult result = mock.perform(post("http://localhost:8080/api/farmers")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(gson.toJson(farmer1Dto)))
                .andExpect(status().isBadRequest()).andReturn();
    }

    @Test
    public void whenGet() throws Exception {

        List<FarmerDto> list = new ArrayList<>();
        list.add(farmer2Dto);
        list.add(farmerDto);

        FarmerSpecificationsBuilder builder = new FarmerSpecificationsBuilder();
        builder.with("inn", ":", "1123");
        builder.with("organizationName", ":", "wasd");
        Specification<Farmer> spec = builder.build();

        when(farmerService.AllToDto(farmerService.getAll(spec))).thenReturn(list);

        MvcResult result = mock.perform(
                get("http://localhost:8080/api/farmers?search=inn:1123,organizationName:wasd"))
                .andExpect(status().isOk()).andReturn();
        assertEquals(gson.toJson(list), result.getResponse().getContentAsString());
    }

    @Test
    public void whenEdit() throws ApplicationException, Exception {

        when(farmerService.editById(1, farmerDto)).thenReturn(archivedFarmerDto);

        mock.perform(post("http://localhost:8080/api/farmers/1/edit")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(gson.toJson(farmerDto)))
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    public void whenGetById() throws ApplicationException, Exception {

        when(farmerService.toDtoById(1)).thenReturn(farmerDto);

        MvcResult result = mock.perform(
                        get("http://localhost:8080/api/farmers/1"))
                .andExpect(status().isOk()).andReturn();
        assertEquals(gson.toJson(farmerDto), result.getResponse().getContentAsString());
    }

    @Test
    public void whenArchive() throws ApplicationException, Exception {

        when(farmerService.toDto(farmerService.toArchive(1))).thenReturn(archivedFarmerDto);

        MvcResult result = mock.perform(
                        post("http://localhost:8080/api/farmers/1/toArchive"))
                .andExpect(status().isOk()).andReturn();
        assertEquals(gson.toJson(archivedFarmerDto), result.getResponse().getContentAsString());
    }
}