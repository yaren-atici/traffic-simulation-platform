package com.yaren.trafficsim.controller;

import com.yaren.trafficsim.model.Intersection;
import com.yaren.trafficsim.repository.IntersectionRepository;
import com.yaren.trafficsim.service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/route")
public class RouteController {

    @Autowired
    private RouteService routeService;

    @Autowired
    private IntersectionRepository intersectionRepository;

    @GetMapping
    public List<Intersection> getRoute(@RequestParam Long fromId, @RequestParam Long toId) {
        Intersection start = intersectionRepository.findById(fromId).orElseThrow();
        Intersection end = intersectionRepository.findById(toId).orElseThrow();
        return routeService.findShortestPath(start, end);
    }
}