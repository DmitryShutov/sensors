package com.sensors.services;

import com.sensors.DTO.SensorDTO;
import com.sensors.models.Sensor;
import com.sensors.repositories.SensorsRepository;
import com.sensors.utils.SensorNotCreatedException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class SensorsService {

    private final SensorsRepository sensorsRepository;
    private final ModelMapper modelMapper;

    public SensorsService(SensorsRepository sensorsRepository, ModelMapper modelMapper) {
        this.sensorsRepository = sensorsRepository;
        this.modelMapper = modelMapper;
    }

    public void register(SensorDTO sensorDTO) {
        findByName(sensorDTO.getName()).ifPresent(sensor -> {
                            throw new SensorNotCreatedException("Sensor " + sensorDTO.getName() +  " already registered");
                        });

        sensorsRepository.save(modelMapper.map(sensorDTO, Sensor.class));
    }

    public Optional<Sensor> findByName(String sensorName) {
        return sensorsRepository.findByName(sensorName);
    }
}
