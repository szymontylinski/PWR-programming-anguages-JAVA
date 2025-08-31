import java.util.*;

public class GeneticAlgorithm {

    private final List<Participant> participants;
    private final Random random = new Random();
    private static final int POPULATION_SIZE = 50;
    private static final int GENERATIONS = 100;
    private static final int NO_IMPROVEMENT_LIMIT = 10;
    private static final int SUGGESTION_COUNT = 5; // Liczba sugerowanych osób

    public GeneticAlgorithm(List<Participant> participants) {
        this.participants = participants;
    }

    public void run() {
        List<List<List<Integer>>> population = initializePopulation();
        List<List<Integer>> bestSolution = null;
        int bestScore = Integer.MIN_VALUE;
        int noImprovementCounter = 0;

        for (int generation = 0; generation < GENERATIONS; generation++) {
            List<List<Integer>> currentBestSolution = findBestSolution(population);
            int currentBestScore = evaluateSolution(currentBestSolution);

            if (currentBestScore > bestScore) {
                bestScore = currentBestScore;
                bestSolution = currentBestSolution;
                noImprovementCounter = 0;
            } else {
                noImprovementCounter++;
            }

            if (noImprovementCounter >= NO_IMPROVEMENT_LIMIT) {
                System.out.println("No improvement in the last " + NO_IMPROVEMENT_LIMIT + " generations. Terminating.");
                break;
            }

            System.out.println("Generation " + generation + " Best Score: " + bestScore +
                    " Average Fitness: " + averageFitness(population));

            population = createNewGeneration(population);
        }

        if (bestSolution != null) {
            displaySolution(bestSolution);
        }
    }

    // Tworzenie początkowej populacji
    private List<List<List<Integer>>> initializePopulation() {
        List<List<List<Integer>>> population = new ArrayList<>();
        for (int i = 0; i < POPULATION_SIZE; i++) {
            List<List<Integer>> individual = new ArrayList<>();
            for (Participant participant : participants) {
                Set<Integer> uniqueSuggestions = new HashSet<>();
                List<Integer> suggestions = new ArrayList<>();
                while (suggestions.size() < SUGGESTION_COUNT) {
                    int suggestion = random.nextInt(participants.size());
                    if (suggestion != participant.getId() - 1 && uniqueSuggestions.add(suggestion)) {
                        suggestions.add(suggestion);
                    }
                }
                individual.add(suggestions);
            }
            population.add(individual);
        }
        return population;
    }

    // Funkcja fitness oceniająca dopasowanie dla 5 osób
    private int evaluateSolution(List<List<Integer>> solution) {
        int totalScore = 0;
        for (int i = 0; i < participants.size(); i++) {
            Participant participant = participants.get(i);
            List<Integer> suggestions = solution.get(i);
            for (int suggestion : suggestions) {
                Participant suggestedParticipant = participants.get(suggestion);
                totalScore += participant.matchScore(suggestedParticipant);
            }
        }
        return totalScore;
    }

    // Znalezienie najlepszego rozwiązania w populacji
    private List<List<Integer>> findBestSolution(List<List<List<Integer>>> population) {
        return population.stream()
                .max(Comparator.comparingInt(this::evaluateSolution))
                .orElseThrow();
    }

    // Tworzenie nowego pokolenia poprzez krzyżowanie i mutację
    private List<List<List<Integer>>> createNewGeneration(List<List<List<Integer>>> population) {
        List<List<List<Integer>>> newGeneration = new ArrayList<>();
        int tournamentSize = 5; // Rozmiar turnieju

        for (int i = 0; i < POPULATION_SIZE; i++) {
            int parent1Index = tournamentSelection(population, tournamentSize);
            int parent2Index = tournamentSelection(population, tournamentSize);

            List<List<Integer>> parent1 = population.get(parent1Index);
            List<List<Integer>> parent2 = population.get(parent2Index);

            List<List<Integer>> child = crossover(parent1, parent2);
            mutate(child);

            newGeneration.add(child);
        }

        return newGeneration;
    }

    // Krzyżowanie dwóch osobników
    private List<List<Integer>> crossover(List<List<Integer>> parent1, List<List<Integer>> parent2) {
        List<List<Integer>> child = new ArrayList<>();
        for (int i = 0; i < parent1.size(); i++) {
            Set<Integer> uniqueSuggestions = new HashSet<>();
            List<Integer> suggestions = new ArrayList<>();

            for (int j = 0; j < SUGGESTION_COUNT; j++) {
                int suggestion;
                if (random.nextBoolean()) {
                    suggestion = parent1.get(i).get(j);
                } else {
                    suggestion = parent2.get(i).get(j);
                }

                // Unikalność propozycji i sprawdzenie, czy nie jest to sam uczestnik
                if (suggestion != i && uniqueSuggestions.add(suggestion)) {
                    suggestions.add(suggestion);
                }
            }

            // Uzupełnienie brakujących unikalnych propozycji
            while (suggestions.size() < SUGGESTION_COUNT) {
                int newSuggestion;
                do {
                    newSuggestion = random.nextInt(participants.size());
                } while (newSuggestion == i || uniqueSuggestions.contains(newSuggestion));

                suggestions.add(newSuggestion);
                uniqueSuggestions.add(newSuggestion);
            }

            child.add(suggestions);
        }
        return child;
    }

    // Mutacja osobnika
    private void mutate(List<List<Integer>> individual) {
        double mutationRate = 0.3; // Prawdopodobieństwo mutacji każdej osoby w propozycji

        for (int i = 0; i < individual.size(); i++) {
            List<Integer> suggestions = individual.get(i);
            for (int j = 0; j < SUGGESTION_COUNT; j++) {
                if (random.nextDouble() < mutationRate) {
                    int newSuggestion;
                    do {
                        newSuggestion = random.nextInt(participants.size());
                    } while (newSuggestion == i || suggestions.contains(newSuggestion)); // Unikaj powtórek i samego uczestnika

                    suggestions.set(j, newSuggestion);
                }
            }
        }
    }

    // Wyświetlenie najlepszego rozwiązania
    private void displaySolution(List<List<Integer>> solution) {
        for (int i = 0; i < solution.size(); i++) {
            System.out.print("Participant " + participants.get(i).getId() + " is matched with: ");
            List<Integer> suggestions = solution.get(i);
            suggestions.forEach(s -> System.out.print(participants.get(s).getId() + " "));
            System.out.println();
        }
    }

    // Selekcja turniejowa
    private int tournamentSelection(List<List<List<Integer>>> population, int tournamentSize) {
        List<Integer> tournamentParticipants = new ArrayList<>();
        for (int i = 0; i < tournamentSize; i++) {
            int randomIndex = random.nextInt(population.size());
            tournamentParticipants.add(randomIndex);
        }

        return tournamentParticipants.stream()
                .max(Comparator.comparingInt(index -> evaluateSolution(population.get(index))))
                .orElseThrow();
    }

    // Obliczanie średniej fitness dla całej populacji
    private double averageFitness(List<List<List<Integer>>> population) {
        return population.stream()
                .mapToInt(this::evaluateSolution)
                .average()
                .orElse(0);
    }
}
