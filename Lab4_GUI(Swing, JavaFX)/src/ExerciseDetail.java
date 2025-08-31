package model;

import java.util.Objects;

/**
 * Reprezentuje szczegóły wykonania ćwiczenia w sesji treningowej.
 */
public class ExerciseDetail {
    private int sets;
    private float value;
    private String unit;

    // Konstruktor
    public ExerciseDetail(int sets, float value, String unit) {
        if (sets <= 0) {
            throw new IllegalArgumentException("Sets must be greater than 0.");
        }
        if (value <= 0) {
            throw new IllegalArgumentException("Value must be greater than 0.");
        }
        if (unit == null || unit.isEmpty()) {
            throw new IllegalArgumentException("Unit cannot be null or empty.");
        }
        this.sets = sets;
        this.value = value;
        this.unit = unit;
    }

    // Gettery i settery
    public int getSets() {
        return sets;
    }

    public void setSets(int sets) {
        if (sets <= 0) {
            throw new IllegalArgumentException("Sets must be greater than 0.");
        }
        this.sets = sets;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        if (value <= 0) {
            throw new IllegalArgumentException("Value must be greater than 0.");
        }
        this.value = value;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        if (unit == null || unit.isEmpty()) {
            throw new IllegalArgumentException("Unit cannot be null or empty.");
        }
        this.unit = unit;
    }

    // Metoda toString
    @Override
    public String toString() {
        return "ExerciseDetail{" +
                "sets=" + sets +
                ", value=" + value +
                ", unit='" + unit + '\'' +
                '}';
    }

    // Metody equals i hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExerciseDetail that = (ExerciseDetail) o;
        return sets == that.sets &&
                Float.compare(that.value, value) == 0 &&
                Objects.equals(unit, that.unit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sets, value, unit);
    }
}
