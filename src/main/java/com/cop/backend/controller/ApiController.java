package com.cop.backend.controller;

import com.cop.backend.model.Command;
import com.cop.backend.model.Incident;
import com.cop.backend.model.SpectrumActivity;
import com.cop.backend.model.Unit;
import com.cop.backend.service.SimulationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ApiController {

    private final SimulationService simulationService;

    public ApiController(SimulationService simulationService) {
        this.simulationService = simulationService;
    }

    @GetMapping("/units")
    public List<Unit> getUnits() {
        return simulationService.getUnits();
    }

    @GetMapping("/spectrum")
    public List<SpectrumActivity> getSpectrum() {
        return simulationService.getSpectrumActivities();
    }

    @GetMapping("/incidents")
    public List<Incident> getIncidents() {
        return simulationService.getIncidents();
    }

    @GetMapping("/commands")
    public List<Command> getCommands() {
        return simulationService.getCommands();
    }

    @PostMapping("/commands")
    public Command sendCommand(@RequestBody Map<String, String> payload) {
        return simulationService.sendCommand(payload.get("unitId"), payload.get("content"));
    }

    @PostMapping("/commands/{id}/ack")
    public ResponseEntity<Command> acknowledge(@PathVariable("id") String id, @RequestBody Map<String, String> payload) {
        Command updated = simulationService.acknowledgeCommand(id, payload.getOrDefault("response", "Acknowledged"));
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }
}

