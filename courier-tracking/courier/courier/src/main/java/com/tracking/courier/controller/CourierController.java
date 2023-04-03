package com.tracking.courier.controller;

import com.tracking.courier.repository.CourierRepository;
import com.tracking.courier.service.CourierService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/couriers")
public class CourierController {

    private final CourierRepository courierRepository;

    private final CourierService courierService;

    // Constructor injection
    public CourierController(CourierService courierService, CourierRepository courierRepository) {
        this.courierService = courierService;
        this.courierRepository = courierRepository;
    }

    @PostMapping("/location")
    public void updateLocation(@RequestParam double latitude, @RequestParam double longitude) {
        courierService.checkCourierLocation(latitude, longitude);
    }

    @GetMapping("/courier/{id}/distance")
    public double getTotalDistanceTraveled(@PathVariable Long id) {
        return courierRepository.getTotalDistanceTraveled(id);
    }

}

