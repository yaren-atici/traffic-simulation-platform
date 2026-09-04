package com.yaren.trafficsim.repository;

import com.yaren.trafficsim.model.Intersection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IntersectionRepository extends JpaRepository<Intersection, Long> {
}