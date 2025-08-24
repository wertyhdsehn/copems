package com.cop.backend.service;

import com.cop.backend.model.Command;
import com.cop.backend.model.Incident;
import com.cop.backend.model.SpectrumActivity;
import com.cop.backend.model.Unit;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SimulationService {

    private final Map<String, Unit> unitsById = new ConcurrentHashMap<>();
    private final List<SpectrumActivity> spectrumActivities = Collections.synchronizedList(new ArrayList<>());
    private final List<Incident> incidents = Collections.synchronizedList(new ArrayList<>());
    private final List<Command> commands = Collections.synchronizedList(new ArrayList<>());
    private final Random random = new Random();

    public SimulationService() {
        seedData();
    }

    public List<Unit> getUnits() {
        return new ArrayList<>(unitsById.values());
    }

    public List<SpectrumActivity> getSpectrumActivities() {
        return new ArrayList<>(spectrumActivities);
    }

    public List<Incident> getIncidents() {
        return new ArrayList<>(incidents);
    }

    public List<Command> getCommands() {
        return new ArrayList<>(commands);
    }

    public Command sendCommand(String unitId, String content) {
        Command command = new Command();
        command.setId(UUID.randomUUID().toString());
        command.setUnitId(unitId);
        command.setContent(content);
        command.setTimestamp(Instant.now());
        command.setAcknowledged(false);
        commands.add(command);
        return command;
    }

    public Command acknowledgeCommand(String commandId, String response) {
        for (Command command : commands) {
            if (command.getId().equals(commandId)) {
                command.setAcknowledged(true);
                command.setResponse(response);
                return command;
            }
        }
        return null;
    }

    private void seedData() {
        for (int i = 1; i <= 5; i++) {
            Unit unit = new Unit();
            unit.setId("unit-" + i);
            unit.setName("Unit " + i);
            unit.setType(Unit.UnitType.values()[i % Unit.UnitType.values().length]);
            unit.setLatitude(38.9 + random.nextDouble() * 0.2);
            unit.setLongitude(-77.0 + random.nextDouble() * 0.2);
            unit.setLastUpdated(Instant.now());
            unitsById.put(unit.getId(), unit);
        }

        for (SpectrumActivity.Band band : SpectrumActivity.Band.values()) {
            SpectrumActivity activity = new SpectrumActivity();
            activity.setId(UUID.randomUUID().toString());
            activity.setBand(band);
            activity.setIntensity(random.nextDouble());
            activity.setLatitude(38.9 + random.nextDouble() * 0.2);
            activity.setLongitude(-77.0 + random.nextDouble() * 0.2);
            activity.setTimestamp(Instant.now());
            spectrumActivities.add(activity);
        }
    }

    @Scheduled(fixedDelay = 7000)
    public void updateUnitPositions() {
        for (Unit unit : unitsById.values()) {
            unit.setLatitude(unit.getLatitude() + (random.nextDouble() - 0.5) * 0.01);
            unit.setLongitude(unit.getLongitude() + (random.nextDouble() - 0.5) * 0.01);
            unit.setLastUpdated(Instant.now());
        }
    }

    @Scheduled(fixedDelay = 8000)
    public void updateSpectrum() {
        synchronized (spectrumActivities) {
            spectrumActivities.clear();
            for (SpectrumActivity.Band band : SpectrumActivity.Band.values()) {
                SpectrumActivity activity = new SpectrumActivity();
                activity.setId(UUID.randomUUID().toString());
                activity.setBand(band);
                activity.setIntensity(random.nextDouble());
                activity.setLatitude(38.9 + random.nextDouble() * 0.2);
                activity.setLongitude(-77.0 + random.nextDouble() * 0.2);
                activity.setTimestamp(Instant.now());
                spectrumActivities.add(activity);
            }
        }
    }

    @Scheduled(fixedDelay = 30000)
    public void generateRandomIncident() {
        Incident incident = new Incident();
        incident.setId(UUID.randomUUID().toString());
        incident.setType(Incident.Type.values()[random.nextInt(Incident.Type.values().length)]);
        incident.setSeverity(random.nextBoolean() ? "MEDIUM" : (random.nextBoolean() ? "LOW" : "HIGH"));
        incident.setDescription(switch (incident.getType()) {
            case SIGNAL_JAMMING -> "Signal Jamming Detected";
            case UNIDENTIFIED_TRANSMISSION -> "Unidentified Transmission";
            case INTERFERENCE -> "Interference Detected";
        });
        incident.setLatitude(38.9 + random.nextDouble() * 0.2);
        incident.setLongitude(-77.0 + random.nextDouble() * 0.2);
        incident.setTimestamp(Instant.now());
        incidents.add(incident);
    }
}

