package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Workout implements Serializable {
    private String name; // Nazwa treningu
    private String location; // Lokalizacja treningu
    private List<TrainingSession> sessions; // Lista sesji powiązanych z treningiem

    public Workout(String name, String location) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty.");
        }
        if (location == null || location.trim().isEmpty()) {
            throw new IllegalArgumentException("Location cannot be empty.");
        }
        this.name = name;
        this.location = location;
        this.sessions = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty.");
        }
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        if (location == null || location.trim().isEmpty()) {
            throw new IllegalArgumentException("Location cannot be empty.");
        }
        this.location = location;
    }

    public List<TrainingSession> getSessions() {
        return sessions;
    }

    public void addSession(TrainingSession session) {
        if (session != null) {
            sessions.add(session);
        }
    }

    public void removeSession(TrainingSession session) {
        sessions.remove(session);
    }

    @Override
    public String toString() {
        return name + " (" + location + ")";
    }
}
