package com.yaren.trafficsim.controller;

import com.yaren.trafficsim.model.Intersection;
import com.yaren.trafficsim.model.Road;
import com.yaren.trafficsim.repository.IntersectionRepository;
import com.yaren.trafficsim.repository.RoadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roads")
public class RoadController {

    @Autowired
    private RoadRepository roadRepository;

    @Autowired
    private IntersectionRepository intersectionRepository;

    @GetMapping
    public List<Road> getAll() {
        return roadRepository.findAll();
    }

    @PostMapping
    public Road create(@RequestBody RoadRequest request) {
        Intersection from = intersectionRepository.findById(request.getFromId()).orElseThrow();
        Intersection to = intersectionRepository.findById(request.getToId()).orElseThrow();

        Road road = new Road(from, to, request.getLengthMeters(), request.getSpeedLimitKmh());
        return roadRepository.save(road);
    }
}