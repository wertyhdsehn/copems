package com.cop.backend.model;

import java.time.Instant;

public class Incident {
    public enum Type { SIGNAL_JAMMING, UNIDENTIFIED_TRANSMISSION, INTERFERENCE }

    private String id;
    private Type type;
    private String description;
    private String severity; // LOW, MEDIUM, HIGH
    private double latitude;
    private double longitude;
    private Instant timestamp;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public Type getType() { return type; }
    public void setType(Type type) { this.type = type; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getSeverity() { return severity; }
    public void setSeverity(String severity) { this.severity = severity; }
    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }
    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }
    public Instant getTimestamp() { return timestamp; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }
}

