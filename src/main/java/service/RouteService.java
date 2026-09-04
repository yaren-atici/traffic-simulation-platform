package com.yaren.trafficsim.service;

import com.yaren.trafficsim.model.Intersection;
import com.yaren.trafficsim.model.Road;
import com.yaren.trafficsim.repository.RoadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RouteService {

    @Autowired
    private RoadRepository roadRepository;

    public List<Intersection> findShortestPath(Intersection start, Intersection end) {
        List<Road> allRoads = roadRepository.findAll();

        Map<Intersection, List<Road>> graph = new HashMap<>();
        for (Road road : allRoads) {
            graph.computeIfAbsent(road.getFrom(), k -> new ArrayList<>()).add(road);
        }

        Map<Long, Double> distances = new HashMap<>();
        Map<Long, Intersection> previous = new HashMap<>();
        PriorityQueue<Intersection> queue = new PriorityQueue<>(
                Comparator.comparingDouble(i -> distances.getOrDefault(i.getId(), Double.MAX_VALUE))
        );

        distances.put(start.getId(), 0.0);
        queue.add(start);

        while (!queue.isEmpty()) {
            Intersection current = queue.poll();

            if (current.getId().equals(end.getId())) {
                break;
            }

            List<Road> neighbors = graph.getOrDefault(current, new ArrayList<>());
            for (Road road : neighbors) {
                Intersection neighbor = road.getTo();
                double weight = calculateWeight(road);
                double newDist = distances.get(current.getId()) + weight;

                if (newDist < distances.getOrDefault(neighbor.getId(), Double.MAX_VALUE)) {
                    distances.put(neighbor.getId(), newDist);
                    previous.put(neighbor.getId(), current);
                    queue.add(neighbor);
                }
            }
        }

        List<Intersection> path = new LinkedList<>();
        Intersection step = end;
        while (step != null) {
            path.add(0, step);
            step = previous.get(step.getId());
        }

        if (path.isEmpty() || !path.get(0).getId().equals(start.getId())) {
            return new ArrayList<>();
        }

        return path;
    }

    /**
     * A road's effective "cost" grows with how many vehicles are currently on it.
     * Each vehicle adds a 15% penalty on top of the base length, simulating congestion.
     */
    private double calculateWeight(Road road) {
        double baseCost = road.getLengthMeters();
        double congestionPenalty = 1.0 + (road.getCurrentVehicleCount() * 0.15);
        return baseCost * congestionPenalty;
    }

    public Road findRoadBetween(Intersection from, Intersection to) {
        return roadRepository.findAll().stream()
                .filter(r -> r.getFrom().getId().equals(from.getId()) && r.getTo().getId().equals(to.getId()))
                .findFirst()
                .orElse(null);
    }
}