package com.booking.recruitment.hotel.service.impl;

import com.booking.recruitment.hotel.constants.*;
import com.booking.recruitment.hotel.exception.*;
import com.booking.recruitment.hotel.model.*;
import com.booking.recruitment.hotel.repository.*;
import com.booking.recruitment.hotel.service.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

import java.util.*;
import java.util.stream.*;

@Service
class DefaultHotelService implements HotelService {
  private final HotelRepository hotelRepository;
  Havesine havesine;
  CityService cityService;

  @Autowired
  DefaultHotelService(HotelRepository hotelRepository,
                      Havesine havesine,
                      CityService cityService
                      ) {
    this.hotelRepository = hotelRepository;
    this.havesine = havesine;
    this.cityService = cityService;
  }



  @Override
  public List<Hotel> getAllHotels() {
    return hotelRepository.findAll();
  }

  @Override
  public List<Hotel> getHotelsByCity(Long cityId) {
    return hotelRepository.findAll().stream()
        .filter((hotel) -> cityId.equals(hotel.getCity().getId()))
        .collect(Collectors.toList());
  }

  @Override
  public Hotel createNewHotel(Hotel hotel) {
    if (hotel.getId() != null) {
      throw new BadRequestException("The ID must not be provided when creating a new Hotel");
    }

    return hotelRepository.save(hotel);
  }

  @Override
  public Hotel getHotelById(Long id) {
    Optional<Hotel> hotel = hotelRepository.findById(id);
    if (!hotel.isPresent()) {
      throw new ElementNotFoundException("Hotel with this id is not available");
    }
    return hotel.get();
  }

  @Override
  @Transactional
  public String deleteHotelById(Long id) {
    Optional<Hotel> hotel = hotelRepository.findById(id);
    if (!hotel.isPresent()) {
      throw new ElementNotFoundException("Hotel not available for deletion");
    }
    Hotel hotelDetails = hotel.get();
    hotelDetails.setDeleted(true);
    hotelRepository.save(hotelDetails);
    return HotelConstant.HOTEL_DELETED_MESSAGE;
  }

  @Override
  public List<Hotel> getTop3ClosestHotels(Long cityId,String sortBy) {
    City city = cityService.getCityById(cityId);
    List<Hotel> hotels = hotelRepository.findAll();
    Map<Double,Hotel> map = new HashMap<>();
    for(Hotel hotel:hotels) {
      Double distance = havesine.distance(city.getCityCentreLatitude(), city.getCityCentreLongitude(), hotel.getLatitude(), hotel.getLatitude());
      map.put(distance,hotel);
    }
    LinkedHashMap<Double, Hotel> distanceToHotel = map.entrySet().stream().sorted(Map.Entry.comparingByKey()).
            limit(3)
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
            (oldValue, newValue) -> oldValue, LinkedHashMap::new));

    return new ArrayList<>(distanceToHotel.values());
  }


}
