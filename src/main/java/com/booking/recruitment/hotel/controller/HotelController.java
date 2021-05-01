package com.booking.recruitment.hotel.controller;

import com.booking.recruitment.hotel.model.Hotel;
import com.booking.recruitment.hotel.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hotel")
public class HotelController {
  private final HotelService hotelService;

  @Autowired
  public HotelController(HotelService hotelService) {
    this.hotelService = hotelService;
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public List<Hotel> getAllHotels() {
    return hotelService.getAllHotels();
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Hotel createHotel(@RequestBody Hotel hotel) {
    return hotelService.createNewHotel(hotel);
  }

  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public Hotel getHotelById(@PathVariable(name = "id") Long id) {
    return hotelService.getHotelById(id);
  }

  @DeleteMapping("/{id}")
  public String deleteHotelById(@PathVariable(name = "id") Long id) {
    return hotelService.deleteHotelById(id);
  }

  @GetMapping("/search/{cityId}")
  @ResponseStatus(HttpStatus.OK)
  public List<Hotel> getTop3ClosestHotels(@PathVariable(name = "cityId") Long cityId,
                                          @RequestParam(value = "sortBy") String sortBy) {
    return hotelService.getTop3ClosestHotels(cityId,sortBy);
  }

}
