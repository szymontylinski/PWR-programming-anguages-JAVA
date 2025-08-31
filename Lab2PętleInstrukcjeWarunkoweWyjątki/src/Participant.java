import java.util.Set;

public class Participant {
    private final int id;
    private final Set<String> attributes;
    private final Set<String> desiredAttributes;

    public Participant(int id, Set<String> attributes, Set<String> desiredAttributes) {
        this.id = id;
        this.attributes = attributes;
        this.desiredAttributes = desiredAttributes;
    }

    public int getId() {
        return id;
    }

    public Set<String> getAttributes() {
        return attributes;
    }

    public Set<String> getDesiredAttributes() {
        return desiredAttributes;
    }

    // Funkcja licząca dopasowanie
    public int matchScore(Participant other) {
        if (this.id == other.getId()) {
            return 0;  // Nie sugerujemy samego siebie
        }
        Set<String> otherAttributes = other.getAttributes();
        return (int) desiredAttributes.stream()
                .filter(otherAttributes::contains)
                .count();
    }

    @Override
    public String toString() {
        return "Participant{" +
                "id=" + id +
                ", attributes=" + attributes +
                ", desiredAttributes=" + desiredAttributes +
                '}';
    }
}
