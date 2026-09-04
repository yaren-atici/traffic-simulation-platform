package com.yaren.trafficsim.repository;

import com.yaren.trafficsim.model.Road;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoadRepository extends JpaRepository<Road, Long> {
}