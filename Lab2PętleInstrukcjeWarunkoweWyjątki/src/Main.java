import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        String inputFilePath;

        if (args.length != 1) {
            System.out.println("Please provide the input file path:");
            Scanner scanner = new Scanner(System.in);
            inputFilePath = scanner.nextLine();
        } else {
            inputFilePath = args[0];
        }

        List<Participant> participants;

        try {
            participants = loadParticipantsFromFile(inputFilePath);
        } catch (IOException e) {
            System.out.println("Error reading input file: " + e.getMessage());
            return;
        }

        GeneticAlgorithm algorithm = new GeneticAlgorithm(participants);
        algorithm.run();
    }

    // Wczytuje uczestników z pliku wejściowego
    private static List<Participant> loadParticipantsFromFile(String filePath) throws IOException {
        List<Participant> participants = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\t");
                if (parts.length != 3) {
                    throw new IOException("Invalid file format. Each line must contain 3 tab-separated fields.");
                }

                int id = Integer.parseInt(parts[0].trim());
                String[] attributes = parts[1].split(",");
                String[] desiredAttributes = parts[2].split(",");

                participants.add(new Participant(id, new HashSet<>(Arrays.asList(attributes)),
                        new HashSet<>(Arrays.asList(desiredAttributes))));
            }
        }
        return participants;
    }
}
