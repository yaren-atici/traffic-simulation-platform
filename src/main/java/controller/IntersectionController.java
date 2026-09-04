package com.yaren.trafficsim.controller;

import com.yaren.trafficsim.model.Intersection;
import com.yaren.trafficsim.repository.IntersectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/intersections")
public class IntersectionController {

    @Autowired
    private IntersectionRepository intersectionRepository;

    @GetMapping
    public List<Intersection> getAll() {
        return intersectionRepository.findAll();
    }

    @PostMapping
    public Intersection create(@RequestBody Intersection intersection) {
        return intersectionRepository.save(intersection);
    }
}