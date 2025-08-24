package com.cop.backend.model;

import java.time.Instant;

public class Command {
    private String id;
    private String unitId;
    private String content;
    private Instant timestamp;
    private boolean acknowledged;
    private String response;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getUnitId() { return unitId; }
    public void setUnitId(String unitId) { this.unitId = unitId; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public Instant getTimestamp() { return timestamp; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }
    public boolean isAcknowledged() { return acknowledged; }
    public void setAcknowledged(boolean acknowledged) { this.acknowledged = acknowledged; }
    public String getResponse() { return response; }
    public void setResponse(String response) { this.response = response; }
}

