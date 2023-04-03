package com.tracking.courier.service;

import com.tracking.courier.Exception.CourierNotFoundException;
import com.tracking.courier.Exception.EntityNotFoundException;
import com.tracking.courier.model.Courier;
import com.tracking.courier.model.Location;
import com.tracking.courier.model.Store;
import com.tracking.courier.repository.CourierRepository;
import com.tracking.courier.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class CourierService {

    private final CourierRepository courierRepository;
    private final StoreRepository storeRepository;

    @Autowired
    public CourierService(CourierRepository courierRepository, StoreRepository storeRepository) {
        this.courierRepository = courierRepository;
        this.storeRepository = storeRepository;
    }

    public List<Courier> findCouriersByLastKnownLocationNear(Location location, double radius){
        return courierRepository.findCouriersByLastKnownLocationNear(location, radius);
    }

    public void updateCourierLocation(long courierId, Location location) {
        Courier courier = courierRepository.findById(courierId);
        if (courier == null) {
            throw new EntityNotFoundException("Courier with ID " + courierId + " not found");
        }
        courier.setLastKnownLocation(location);
        courierRepository.save(courier);
    }

    public void checkCourierLocation(double latitude, double longitude) {
        List<Store> stores = storeRepository.findAll();
        for (Store store : stores) {
            double distance = getDistance(latitude, longitude, store.getLat(), store.getLng());
            if (distance < 100) {
                // Check if the courier already entered the store's circumference
                Courier courier = courierRepository.findByLastEntry(store.getId());
                if (courier == null || (System.currentTimeMillis() - courier.getLastSeenAt().getEntryTime().get()) > 60000) {
                    // Record the entry of the courier into the store's circumference
                    courier = new Courier();
                    courier.setId(String.valueOf(UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE));
                    courier.getLastSeenAt().setEntryTime(new LocalDateTime());
                    //courier.setLastEntry(store.getId());
                    courierRepository.save(courier);
                }
            }
        }
    }

    // Method for finding the nearest store to a given location
    private Store findNearestStore(double lat, double lng) {
        List<Store> stores = storeRepository.findAll();
        Store nearestStore = null;
        double minDistance = Double.MAX_VALUE;

        for (Store store : stores) {
            double distance = getDistance(lat, lng, store.getLat(), store.getLng());
            if (distance < minDistance) {
                nearestStore = store;
                minDistance = distance;
            }
        }

        return nearestStore;
    }

    private double getDistance(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371; // Radius of the earth in km
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a =
                Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                        Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                                Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = R * c; // Distance in km
        return d * 1000; // Distance in meters
    }

    public Location getLastKnownLocation(Long courierId) throws CourierNotFoundException {
        Courier courier = courierRepository.findById(courierId)
                .orElseThrow(() -> new CourierNotFoundException(courierId));

        return courier.getLastKnownLocation();
    }

    public Courier saveCourier(Courier courier) {
        return courierRepository.save(courier);
    }

    public List<Courier> findAllCouriers(){
        return courierRepository.findAll();
    }

    public Courier findCourierById(Long id){
        return courierRepository.findById(id);
    }

    public Store getLastSeenAt(String courierId) throws CourierNotFoundException {
        Courier courier = courierRepository.findById(Long.valueOf(courierId))
                .orElseThrow(() -> new CourierNotFoundException(Long.valueOf(courierId)));

        return courier.getLastSeenAt();
    }
}
