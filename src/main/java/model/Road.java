package com.yaren.trafficsim.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Road {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Intersection from;

    @ManyToOne
    private Intersection to;

    private double lengthMeters;
    private int speedLimitKmh;
    private int currentVehicleCount = 0;

    public Road() {
    }

    public Road(Intersection from, Intersection to, double lengthMeters, int speedLimitKmh) {
        this.from = from;
        this.to = to;
        this.lengthMeters = lengthMeters;
        this.speedLimitKmh = speedLimitKmh;
    }

    public Long getId() {
        return id;
    }

    public Intersection getFrom() {
        return from;
    }

    public void setFrom(Intersection from) {
        this.from = from;
    }

    public Intersection getTo() {
        return to;
    }

    public void setTo(Intersection to) {
        this.to = to;
    }

    public double getLengthMeters() {
        return lengthMeters;
    }

    public void setLengthMeters(double lengthMeters) {
        this.lengthMeters = lengthMeters;
    }

    public int getSpeedLimitKmh() {
        return speedLimitKmh;
    }

    public void setSpeedLimitKmh(int speedLimitKmh) {
        this.speedLimitKmh = speedLimitKmh;
    }

    public int getCurrentVehicleCount() {
        return currentVehicleCount;
    }

    public synchronized void incrementVehicleCount() {
        currentVehicleCount++;
    }

    public synchronized void decrementVehicleCount() {
        if (currentVehicleCount > 0) {
            currentVehicleCount--;
        }
    }
}