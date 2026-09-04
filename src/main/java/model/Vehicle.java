package com.yaren.trafficsim.model;

import java.util.List;
import java.util.UUID;

public class Vehicle {

    private final String id;
    private List<Intersection> route;
    private int currentIndex;
    private boolean finished;

    public Vehicle(List<Intersection> route) {
        this.id = UUID.randomUUID().toString().substring(0, 8);
        this.route = route;
        this.currentIndex = 0;
        this.finished = false;
    }

    public String getId() {
        return id;
    }

    public List<Intersection> getRoute() {
        return route;
    }

    public Intersection getCurrentPosition() {
        return route.get(currentIndex);
    }

    public boolean isFinished() {
        return finished;
    }

    public void advance() {
        if (currentIndex < route.size() - 1) {
            currentIndex++;
        }
        if (currentIndex == route.size() - 1) {
            finished = true;
        }
    }

    public int getCurrentIndex() {
        return currentIndex;
    }
}