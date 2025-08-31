package model;

import java.time.LocalDate;

public class TrainingGoal {
    private String exerciseName;
    private String target;
    private LocalDate deadline;
    private boolean isAchieved;

    // Konstruktor
    public TrainingGoal(String exerciseName, String target, LocalDate deadline) {
        if (exerciseName == null || exerciseName.isEmpty()) {
            throw new IllegalArgumentException("Exercise name cannot be null or empty.");
        }
        if (target == null || target.isEmpty()) {
            throw new IllegalArgumentException("Target cannot be null or empty.");
        }
        if (deadline == null || deadline.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Deadline cannot be null or in the past.");
        }

        this.exerciseName = exerciseName;
        this.target = target;
        this.deadline = deadline;
        this.isAchieved = false; // Domyślnie cel nie jest osiągnięty
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

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        if (target == null || target.isEmpty()) {
            throw new IllegalArgumentException("Target cannot be null or empty.");
        }
        this.target = target;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        if (deadline == null || deadline.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Deadline cannot be null or in the past.");
        }
        this.deadline = deadline;
    }

    public boolean isAchieved() {
        return isAchieved;
    }

    // Oznaczenie celu jako osiągnięty
    public void markAsAchieved() {
        this.isAchieved = true;
    }

    // Sprawdzenie, czy cel jest po terminie
    public boolean isOverdue() {
        return !isAchieved && deadline.isBefore(LocalDate.now());
    }

    // Czytelna reprezentacja celu
    @Override
    public String toString() {
        return "TrainingGoal{" +
                "exerciseName='" + exerciseName + '\'' +
                ", target='" + target + '\'' +
                ", deadline=" + deadline +
                ", isAchieved=" + isAchieved +
                '}';
    }
}

