package com.tracking.courier.repository;

import com.tracking.courier.model.Store;
import com.tracking.courier.model.Location;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Long> {
    Store findByLocation(Location location);

    List<Store> findAll();

    @Query("SELECT s FROM Store s WHERE FUNCTION('ST_Distance_Sphere', s.location, :location) < :radius")
    List<Store> findByLocation(@Param("location") Location location, @Param("radius") double radius);

}
