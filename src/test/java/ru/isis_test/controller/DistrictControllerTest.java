package ru.isis_test.controller;

import com.google.gson.Gson;
import org.hibernate.query.SemanticException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.isis_test.IsisTestApplication;
import ru.isis_test.core.exception.ApplicationException;;
import ru.isis_test.model.District;
import ru.isis_test.service.DistrictService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("main")
@SpringBootTest(classes = IsisTestApplication.class)
@AutoConfigureMockMvc
class DistrictControllerTest {

    @MockBean
    private DistrictService districtService;

    @Autowired
    private MockMvc mockMvc;

    private District district;

    private District district1;

    private District archivedDistrict;

    private Gson gson;

    @BeforeEach
    void init() {
        district =  new District();
        district.setId(1);
        district.setName("Okt1");
        district.setCode("1123");

        district1 = new District();
        district1.setId(2);
        district1.setName("Okt2");
        district1.setCode("1124");

        archivedDistrict =  new District();
        archivedDistrict.setId(1);
        archivedDistrict.setName("Okt1");
        archivedDistrict.setCode("1123");
        archivedDistrict.setArchiveStatus(true);

        gson = new Gson();
    }

    @AfterEach
    void teardown() {
        archivedDistrict = null;
        district = null;
        district1 = null;
        gson = null;
    }

    @Test
    public void whenGetAll() throws Exception {
        List<District> list = new ArrayList<>();

        list.add(district);
        list.add(district1);

        when(districtService.getByFilter("Okt", "112")).thenReturn(list);

        String requestBody = gson.toJson(list);
        System.out.println(requestBody);
        MvcResult result = mockMvc.perform(get("http://localhost:8080/api/districts")
                .contentType(MediaType.TEXT_PLAIN)
                .content(requestBody))
                .andExpect(status().isOk())
                .andReturn();

    }

    @Test
    public void whenAddNewWithoutName() throws Exception {
        district.setName(null);

        when(districtService.save(district)).
                thenThrow(new SemanticException("district.name Required, but it was null"));

        MvcResult result = mockMvc.perform(post("http://localhost:8080/api/districts")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(gson.toJson(district)))
                        .andExpect(status().isBadRequest())
                        .andReturn();
    }

    @Test
    public void whenAdd() throws Exception {
        when(districtService.save(district)).thenReturn(district);

        MvcResult result = mockMvc.perform(post("http://localhost:8080/api/districts")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(gson.toJson(district)))
                .andExpect(status().isOk())
                .andReturn();
        assertEquals(gson.toJson(district), result.getResponse().getContentAsString());
    }

    @Test
    public void whenEditDistrict() throws Exception, ApplicationException {
        when(districtService.edit(district, 1)).thenReturn(archivedDistrict);
        MvcResult result = mockMvc.perform(post("http://localhost:8080/api/districts/1/edit")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(gson.toJson(district)))
                        .andExpect(status().isOk()).andReturn();
        assertEquals(gson.toJson(archivedDistrict), result.getResponse().getContentAsString());
    }

    @Test
    public void whenEditWrongDistrict() throws Exception {
        district.setName("");
        MvcResult result = mockMvc.perform(post("http://localhost:8080/api/districts/1/edit")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(gson.toJson(district)))
                .andExpect(status().isBadRequest()).andReturn();
    }

    @Test
    public void whenArchiveDistrict() throws Exception, ApplicationException {

        when(districtService.toArchive(district.getId())).thenReturn(archivedDistrict);

        MvcResult result = mockMvc.perform(post("http://localhost:8080/api/districts/1/toArchive")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(gson.toJson(district)))
                .andExpect(status().isOk()).andReturn();
        assertEquals(gson.toJson(archivedDistrict), result.getResponse().getContentAsString());
    }


}