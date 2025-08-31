import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DecisionRepository implements IDecisionRepository {
    private final List<Decision> decisions = new ArrayList<>();
    private final String filePath = "decisions.dat";

    public DecisionRepository() {
        loadDecisions();
    }

    @Override
    public void add(Decision decision) {
        decisions.add(decision);
        saveDecisions();
    }

    @Override
    public List<Decision> getAll() {
        return new ArrayList<>(decisions);
    }

    @Override
    public List<Decision> searchByComponent(String component) {
        return decisions.stream()

                .filter(d -> d.getComponent().equalsIgnoreCase(component))
                .collect(Collectors.toList());
    }

    @Override
    public List<Decision> searchByPerson(String person) {
        return decisions.stream()
                .filter(d -> d.getPerson().equalsIgnoreCase(person))
                .collect(Collectors.toList());
    }

    private void saveDecisions() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(decisions);
        } catch (IOException e) {
            System.out.println("Błąd podczas zapisywania decyzji: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private void loadDecisions() {
        File file = new File(filePath);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
                List<Decision> loadedDecisions = (List<Decision>) ois.readObject();
                decisions.addAll(loadedDecisions);
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Błąd podczas ładowania decyzji: " + e.getMessage());
            }

        }
    }
}