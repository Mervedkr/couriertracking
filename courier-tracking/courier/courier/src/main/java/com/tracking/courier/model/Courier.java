package com.tracking.courier.model;

import com.tracking.courier.model.embeddable.Location;
import jdk.vm.ci.meta.Local;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "couriers")
public class Courier {
    @Id
    @Column (name = "id", nullable = false)
    private String id;
    private double totalDistance;
    @Column (name = "name", nullable = false)
    private String name;

    @Embedded
    private Location lastKnownLocation;

    public Courier(String name, Location location) {
        this.name = name;
        this.lastKnownLocation = location;
    }

    public Location getLastKnownLocation() {
        return lastKnownLocation;
    }

    public void setLastKnownLocation(Location location) {
        this.lastKnownLocation = location;
    }

    private Store lastSeenAt;

    public Store getLastSeenAt() {
        return lastSeenAt;
    }

    public void setLastSeenAt(Store lastSeenAt) {
        this.lastSeenAt = lastSeenAt;
    }

    public Courier() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(double totalDistance) {
        this.totalDistance = totalDistance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addDistance(double distance) {
        totalDistance += distance;
    }

    public LocalDateTime getLastEntryTime() {
        if (lastSeenAt != null) {
            return lastSeenAt.getEntryTime();
        } else {
            return null;
        }
    }

    @Override
    public String toString() {
        return "Courier{" +
                "id='" + id + '\'' +
                ", totalDistance=" + totalDistance +
                ", name='" + name + '\'' +
                ", lastKnownLocation=" + lastKnownLocation +
                ", lastSeenAt=" + lastSeenAt +
                '}';
    }
}

