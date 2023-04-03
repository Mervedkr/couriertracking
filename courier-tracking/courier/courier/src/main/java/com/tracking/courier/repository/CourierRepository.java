package com.tracking.courier.repository;

import com.tracking.courier.model.Courier;
import com.tracking.courier.model.Location;
import org.springframework.stereotype.Repository;

import java.awt.*;
import java.util.Date;
import java.util.List;

@Repository
public interface CourierRepository extends JpaRepository<Courier, Long> {
    @Query("SELECT SUM(c.distance) FROM Courier c WHERE c.id = :id")
    double getTotalDistanceTraveled(@Param("id") Long id);

    @Query("SELECT c FROM Courier c WHERE c.lastEntry")
    Courier findByLastEntry(@Param("storeId") Long storeId);

    Courier save(Courier courier);

    void saveAll(List<Courier> couriers);

    List<Courier> findAll();

    Courier findById(long id);

    @Query("SELECT c FROM Courier c WHERE FUNCTION('ST_Distance_Sphere', c.lastKnownLocation, :location) < :radius")
    List<Courier> findCouriersByLastKnownLocationNear(@Param("location") Location location, @Param("radius") double radius);

}

