package com.yaren.trafficsim.controller;

import com.yaren.trafficsim.model.Intersection;
import com.yaren.trafficsim.model.Vehicle;
import com.yaren.trafficsim.repository.IntersectionRepository;
import com.yaren.trafficsim.service.RouteService;
import com.yaren.trafficsim.service.SimulationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/simulation")
public class SimulationController {

    @Autowired
    private SimulationService simulationService;

    @Autowired
    private RouteService routeService;

    @Autowired
    private IntersectionRepository intersectionRepository;

    @PostMapping("/spawn")
    public Vehicle spawnVehicle(@RequestParam Long fromId, @RequestParam Long toId) {
        Intersection start = intersectionRepository.findById(fromId).orElseThrow();
        Intersection end = intersectionRepository.findById(toId).orElseThrow();

        List<Intersection> route = routeService.findShortestPath(start, end);
        return simulationService.spawnVehicle(route);
    }

    @GetMapping("/active")
    public Map<String, Vehicle> getActiveVehicles() {
        return simulationService.getActiveVehicles();
    }
}