package com.sensors.controllers;

import com.sensors.DTO.MeasurementDTO;
import com.sensors.models.Measurement;
import com.sensors.services.MeasurementsService;
import com.sensors.utils.MeasurementNotCreatedException;
import com.sensors.utils.ServerErrorResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/measurements")
public class MeasurementsController {

    private final MeasurementsService measurementsService;

    public MeasurementsController(MeasurementsService measurementsService) {
        this.measurementsService = measurementsService;
    }

    @GetMapping
    public List<Measurement> getAll() {
        return measurementsService.getAll();
    }

    @GetMapping("/rainyDaysCount")
    public int rainyDaysCount() {
        return measurementsService.countByRainingTrue();
    }

    @PostMapping("/add")
    public ResponseEntity<HttpStatus> add(@RequestBody @Valid MeasurementDTO measurementDTO,
                                          BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder msg = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                msg.append(error.getField())
                        .append(" - ")
                        .append(error.getDefaultMessage());
            }

            throw new MeasurementNotCreatedException(msg.toString());
        }

        measurementsService.save(measurementDTO);
        return ResponseEntity.ok(HttpStatus.OK);

    }

    @ExceptionHandler
    private ResponseEntity<ServerErrorResponse> handleException(MeasurementNotCreatedException e) {
        ServerErrorResponse serverErrorResponse = new ServerErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(serverErrorResponse, HttpStatus.BAD_REQUEST);
    }
}
