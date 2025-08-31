package model;

import java.util.Objects;

/**
 * Reprezentuje ćwiczenie w aplikacji Fitness Tracker.
 */
public class Exercise {
    private String name;
    private String type;

    // Konstruktor
    public Exercise(String name, String type) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Exercise name cannot be null or empty.");
        }
        if (type == null || type.isEmpty()) {
            throw new IllegalArgumentException("Exercise type cannot be null or empty.");
        }
        this.name = name;
        this.type = type;
    }

    // Gettery i settery
    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Exercise name cannot be null or empty.");
        }
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        if (type == null || type.isEmpty()) {
            throw new IllegalArgumentException("Exercise type cannot be null or empty.");
        }
        this.type = type;
    }

    // Metoda toString
    @Override
    public String toString() {
        return "Exercise{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

    // Metody equals i hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Exercise exercise = (Exercise) o;
        return Objects.equals(name, exercise.name) && Objects.equals(type, exercise.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type);
    }
}
