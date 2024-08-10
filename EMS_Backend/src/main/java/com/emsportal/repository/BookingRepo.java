package com.emsportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.emsportal.model.Bookings;


public interface BookingRepo extends JpaRepository<Bookings,Integer> {

}
