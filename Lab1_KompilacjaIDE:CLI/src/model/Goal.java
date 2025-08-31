package model;

import java.time.LocalDate;

public class Goal {
    private String exerciseName;
    private double targetValue;
    private LocalDate deadline;
    private String notes;

    // Konstruktor
    public Goal(String exerciseName, double targetValue, LocalDate deadline, String notes) {
        if (exerciseName == null || exerciseName.isEmpty()) {
            throw new IllegalArgumentException("Exercise name cannot be null or empty.");
        }
        if (targetValue <= 0) {
            throw new IllegalArgumentException("Target value must be greater than 0.");
        }
        if (deadline == null || deadline.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Deadline cannot be in the past.");
        }

        this.exerciseName = exerciseName;
        this.targetValue = targetValue;
        this.deadline = deadline;
        this.notes = notes;
    }

    // Gettery i settery
    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        if (exerciseName == null || exerciseName.isEmpty()) {
            throw new IllegalArgumentException("Exercise name cannot be null or empty.");
        }
        this.exerciseName = exerciseName;
    }

    public double getTargetValue() {
        return targetValue;
    }

    public void setTargetValue(double targetValue) {
        if (targetValue <= 0) {
            throw new IllegalArgumentException("Target value must be greater than 0.");
        }
        this.targetValue = targetValue;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        if (deadline == null || deadline.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Deadline cannot be in the past.");
        }
        this.deadline = deadline;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    // Sprawdzenie, czy deadline minął
    public boolean isDeadlinePassed() {
        return LocalDate.now().isAfter(deadline);
    }

    // Czytelna reprezentacja celu
    @Override
    public String toString() {
        return "Goal{" +
                "exerciseName='" + exerciseName + '\'' +
                ", targetValue=" + targetValue +
                ", deadline=" + deadline +
                ", notes='" + notes + '\'' +
                '}';
    }
}

