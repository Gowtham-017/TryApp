package com.emsportal.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import com.emsportal.model.Events;

public interface EventsRepo extends JpaRepository<Events,Integer> {

}
