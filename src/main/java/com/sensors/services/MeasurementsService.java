package com.sensors.services;

import com.sensors.DTO.MeasurementDTO;
import com.sensors.models.Measurement;
import com.sensors.models.Sensor;
import com.sensors.repositories.MeasurementsRepository;
import com.sensors.utils.MeasurementNotCreatedException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MeasurementsService {
    private final MeasurementsRepository measurementsRepository;
    private final ModelMapper modelMapper;
    private final SensorsService sensorsService;
    public MeasurementsService(MeasurementsRepository measurementsRepository,
                               ModelMapper modelMapper,
                               SensorsService sensorsService) {
        this.measurementsRepository = measurementsRepository;
        this.modelMapper = modelMapper;
        this.sensorsService = sensorsService;
    }
    @Transactional
    public void save(MeasurementDTO measurementDTO) {
        Sensor sensor = sensorsService.findByName(measurementDTO.getSensor().getName())
                .orElseThrow(() -> new MeasurementNotCreatedException("Sensor is not registered"));

        Measurement measurement = modelMapper.map(measurementDTO, Measurement.class);
        measurement.setSensor(sensor);
        measurementsRepository.save(measurement);
    }

    public List<Measurement> getAll() {
        return measurementsRepository.findAll();
    }

    public int countByRainingTrue() {
        return measurementsRepository.countByRainingTrue();
    }
}
