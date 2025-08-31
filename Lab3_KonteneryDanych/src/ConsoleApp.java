import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class ConsoleApp {
    private final IDecisionService decisionService;
    private final Scanner scanner;

    public ConsoleApp(IDecisionService decisionService) {
        this.decisionService = decisionService;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        while (true) {
            System.out.println("1. Dodaj nową decyzję");
            System.out.println("2. Przeglądaj podjęte decyzje");
            System.out.println("3. Wyszukaj decyzje po komponencie");
            System.out.println("4. Wyszukaj decyzje po osobie");
            System.out.println("5. Wyjdź");
            System.out.print("Wybierz opcję: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Oczyszczenie bufora

            switch (choice) {
                case 1:
                    addDecision();
                    break;
                case 2:
                    viewDecisions();
                    break;
                case 3:
                    searchByComponent();
                    break;
                case 4:
                    searchByPerson();
                    break;
                case 5:
                    System.out.println("Zamykam program...");
                    return;
                default:
                    System.out.println("Nieprawidłowy wybór. Spróbuj ponownie.");
            }
        }
    }

    private void addDecision() {
        try {
            System.out.print("Podaj datę (YYYY-MM-DD): ");
            LocalDate date = LocalDate.parse(scanner.nextLine());

            System.out.print("Podaj komponent: ");
            String component = scanner.nextLine();
            validateComponent(component);

            System.out.print("Podaj osobę: ");
            String person = scanner.nextLine();
            validatePerson(person);

            System.out.print("Podaj ważność (1-5): ");
            int importance = scanner.nextInt();
            validateImportance(importance);
            scanner.nextLine();

            System.out.print("Podaj opis: ");
            String description = scanner.nextLine();
            validateDescription(description);

            Decision decision = new Decision(date, component, person, importance, description);
            decisionService.addDecision(decision);
            System.out.println("Decyzja została dodana.");
        } catch (IllegalArgumentException e) {
            System.err.println("Błąd walidacji: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Wystąpił błąd: " + e.getMessage());
        }
    }

    private void validateComponent(String component) {
        if (component == null || component.isEmpty()) {
            throw new IllegalArgumentException("Komponent nie może być pusty.");
        }
    }

    private void validatePerson(String person) {
        if (person == null || person.isEmpty()) {
            throw new IllegalArgumentException("Osoba nie może być pusta.");
        }
    }

    private void validateImportance(int importance) {
        if (importance < 1 || importance > 5) {
            throw new IllegalArgumentException("Ważność musi być w zakresie od 1 do 5.");
        }
    }

    private void validateDescription(String description) {
        if (description == null || description.isEmpty()) {
            throw new IllegalArgumentException("Opis nie może być pusty.");
        }
    }

    private void viewDecisions() {
        List<Decision> decisions = decisionService.getAllDecisions();
        if (decisions.isEmpty()) {
            System.out.println("Brak podjętych decyzji.");
        } else {
            for (Decision decision : decisions) {
                System.out.println(decision);
            }
        }
    }

    private void searchByComponent() {
        System.out.print("Podaj komponent do wyszukiwania: ");
        String component = scanner.nextLine();
        List<Decision> results = decisionService.searchByComponent(component);
        if (results.isEmpty()) {
            System.out.println("Brak decyzji dla komponentu: " + component);
        } else {
            for (Decision decision : results) {
                System.out.println(decision);
            }
        }
    }

    private void searchByPerson() {
        System.out.print("Podaj osobę do wyszukiwania: ");
        String person = scanner.nextLine();
        List<Decision> results = decisionService.searchByPerson(person);
        if (results.isEmpty()) {
            System.out.println("Brak decyzji dla osoby: " + person);
        } else {
            for (Decision decision : results) {
                System.out.println(decision);
            }
        }
    }

    public static void main(String[] args) {
        IDecisionRepository decisionRepository = new DecisionRepository();
        IDecisionService decisionService = new DecisionService(decisionRepository);
        ConsoleApp app = new ConsoleApp(decisionService);
        app.start();
    }
}