package com.sensors.repositories;

import com.sensors.models.Measurement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MeasurementsRepository extends JpaRepository<Measurement, Integer> {
    int countByRainingTrue();
}
