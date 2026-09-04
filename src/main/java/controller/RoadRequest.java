package com.yaren.trafficsim.controller;

public class RoadRequest {
    private Long fromId;
    private Long toId;
    private double lengthMeters;
    private int speedLimitKmh;

    public Long getFromId() {
        return fromId;
    }

    public void setFromId(Long fromId) {
        this.fromId = fromId;
    }

    public Long getToId() {
        return toId;
    }

    public void setToId(Long toId) {
        this.toId = toId;
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
}