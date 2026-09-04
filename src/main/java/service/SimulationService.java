package com.yaren.trafficsim.service;

import com.yaren.trafficsim.model.Intersection;
import com.yaren.trafficsim.model.Road;
import com.yaren.trafficsim.model.Vehicle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class SimulationService {

    private final Map<String, Vehicle> activeVehicles = new ConcurrentHashMap<>();
    private final ExecutorService executor = Executors.newCachedThreadPool();

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private RouteService routeService;

    public Vehicle spawnVehicle(List<Intersection> route) {
        Vehicle vehicle = new Vehicle(route);
        activeVehicles.put(vehicle.getId(), vehicle);

        executor.submit(() -> runVehicle(vehicle));

        return vehicle;
    }

    private void runVehicle(Vehicle vehicle) {
        try {
            occupyCurrentSegment(vehicle);
            broadcastUpdate(vehicle);

            while (!vehicle.isFinished()) {
                Thread.sleep(1500);

                freeCurrentSegment(vehicle);
                vehicle.advance();
                occupyCurrentSegment(vehicle);

                broadcastUpdate(vehicle);
                System.out.println("Vehicle " + vehicle.getId() + " is now at " + vehicle.getCurrentPosition().getName());
            }

            freeCurrentSegment(vehicle);

            System.out.println("Vehicle " + vehicle.getId() + " has FINISHED its route.");
            activeVehicles.remove(vehicle.getId());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void occupyCurrentSegment(Vehicle vehicle) {
        int idx = vehicle.getCurrentIndex();
        if (idx < vehicle.getRoute().size() - 1) {
            Intersection from = vehicle.getRoute().get(idx);
            Intersection to = vehicle.getRoute().get(idx + 1);
            Road road = routeService.findRoadBetween(from, to);
            if (road != null) {
                road.incrementVehicleCount();
            }
        }
    }

    private void freeCurrentSegment(Vehicle vehicle) {
        int idx = vehicle.getCurrentIndex();
        if (idx < vehicle.getRoute().size() - 1) {
            Intersection from = vehicle.getRoute().get(idx);
            Intersection to = vehicle.getRoute().get(idx + 1);
            Road road = routeService.findRoadBetween(from, to);
            if (road != null) {
                road.decrementVehicleCount();
            }
        }
    }

    private void broadcastUpdate(Vehicle vehicle) {
        messagingTemplate.convertAndSend("/topic/vehicles", vehicle);
    }

    public Map<String, Vehicle> getActiveVehicles() {
        return activeVehicles;
    }
}