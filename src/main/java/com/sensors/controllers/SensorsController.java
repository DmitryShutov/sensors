package com.sensors.controllers;

import com.sensors.DTO.SensorDTO;
import com.sensors.services.SensorsService;
import com.sensors.utils.MeasurementNotCreatedException;
import com.sensors.utils.ServerErrorResponse;
import com.sensors.utils.SensorNotCreatedException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sensors")
public class SensorsController {

    private final SensorsService sensorsService;

    public SensorsController(SensorsService sensorsService) {
        this.sensorsService = sensorsService;
    }

    @PostMapping("/registration")
    public ResponseEntity<HttpStatus> register(@RequestBody @Valid SensorDTO sensorDTO,
                                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMsg = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError error : fieldErrors) {
                errorMsg.append(error.getField())
                        .append(" - ")
                        .append(error.getDefaultMessage());
            }
            throw new SensorNotCreatedException(errorMsg.toString());

        }
        sensorsService.register(sensorDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler
    private ResponseEntity<ServerErrorResponse> handleException(SensorNotCreatedException e) {
        ServerErrorResponse response = new ServerErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<ServerErrorResponse> handleException(MeasurementNotCreatedException e) {
        ServerErrorResponse response = new ServerErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }


}
