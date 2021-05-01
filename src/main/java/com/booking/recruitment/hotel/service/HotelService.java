package com.booking.recruitment.hotel.service;

import com.booking.recruitment.hotel.model.*;

import java.util.*;

public interface HotelService {
    List<Hotel> getAllHotels();

    List<Hotel> getHotelsByCity(Long cityId);

    Hotel createNewHotel(Hotel hotel);

    Hotel getHotelById(Long id);

    String deleteHotelById(Long id);

    List<Hotel> getTop3ClosestHotels(Long cityId,String sortBy);
}
