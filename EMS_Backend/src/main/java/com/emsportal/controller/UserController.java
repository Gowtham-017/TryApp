package com.emsportal.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.emsportal.model.Bookings;
import com.emsportal.model.Events;
import com.emsportal.repository.BookingRepo;
import com.emsportal.repository.EventsRepo;

@CrossOrigin
@RestController
@RequestMapping("/api/user/rights")
public class UserController {

    @Autowired
    private BookingRepo bkr;

    @Autowired
    private EventsRepo eventsrepo;

    @GetMapping(value = "/getallevent", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> getAllEvent() {
        try {
            List<Events> events = eventsrepo.findAll();
            return ResponseEntity.ok(events);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Collections.singletonMap("error", "Failed to fetch events. Exception: " + e.getMessage()));
        }
    }

    @SuppressWarnings("null")
    @PostMapping("/postnewbooking")
    public ResponseEntity<String> saveEvent(@RequestBody Bookings e) {
        try {
            bkr.save(e);
            return ResponseEntity.ok("Booking Successful");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error booking event: " + ex.getMessage());
        }
    }

    @GetMapping("/getallbooking")
    public List<Bookings> getAllEven() {
        return bkr.findAll();
    }

    @DeleteMapping("/deleteobooking/{bookingid}")
	public String deleteDetails(@PathVariable("bookingid") int bookingid) {
		bkr.deleteById(bookingid);
		return "Deleted booking with bookingid:"+bookingid;
	}
}
