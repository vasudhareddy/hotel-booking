package com.booking.recruitment.hotel.repository;

import com.booking.recruitment.hotel.model.*;
import org.springframework.data.jpa.repository.*;

import java.util.*;

public interface HotelRepository extends JpaRepository<Hotel, Long> {
    Optional<Hotel> findById(Long id);
}
