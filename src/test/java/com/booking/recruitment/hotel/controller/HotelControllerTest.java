package com.booking.recruitment.hotel.controller;

import com.booking.recruitment.hotel.model.*;
import com.booking.recruitment.hotel.repository.*;
import com.fasterxml.jackson.databind.*;
import org.junit.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.boot.test.context.*;
import org.springframework.http.*;
import org.springframework.test.context.jdbc.*;
import org.springframework.test.context.junit.jupiter.*;
import org.springframework.test.web.servlet.*;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:data.sql")
//@SlowTest
class HotelControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private HotelRepository repository;
    @Autowired
    private CityRepository cityRepository;

    @Test
    @DisplayName("When all hotels are requested then they are all returned")
    void allHotelsRequested() throws Exception {
        mockMvc.perform(get("/hotel")).andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$", hasSize((int) repository.count())));
    }

    @Test
    @DisplayName("When a hotel creation is requested then it is persisted")
    void hotelCreatedCorrectly() throws Exception {
        City city = cityRepository.findById(1L).orElseThrow(() -> new IllegalStateException("Test dataset does not contain a city with ID 1!"));
        Hotel newHotel = Hotel.builder().setName("This is a test hotel").setCity(city).build();
        Long newHotelId = mapper.readValue(mockMvc.perform(post("/hotel").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(newHotel))).andExpect(status().isCreated()).andReturn().getResponse()
                .getContentAsString(), Hotel.class).getId();
        newHotel.setId(newHotelId); // Populate the ID of the hotel after successful creation
        assertThat(repository.findById(newHotelId).orElseThrow(() -> new IllegalStateException("New Hotel has not been saved in the repository")),
                equalTo(newHotel));
    }

    @Test
    @DisplayName("This testcase test to fetch the hotel by id")
    void test_getHotelById() throws Exception {
        MvcResult result = this.mockMvc.perform(get("/hotel/{id}", 1)).andReturn();
        String output = result.getResponse().getContentAsString();
        Hotel hotel = mapper.readValue(output, Hotel.class);
        Assert.assertEquals("Monaghan Hotel", hotel.getName());
        Assert.assertEquals(Double.valueOf("9.2"), hotel.getRating());
    }

    @Test
    @DisplayName("This testcase test to delete the hotel by id")
    void test_deleteHotelById() throws Exception {
        MvcResult result = this.mockMvc.perform(delete("/hotel/{id}", 1L)).andReturn();
        String output = result.getResponse().getContentAsString();
        Assert.assertEquals("Hotel deleted successfully", output);
    }
}
