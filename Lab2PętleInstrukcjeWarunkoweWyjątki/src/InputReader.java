import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class InputReader {

    public static List<Participant> readParticipantsFromFile(String fileName) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(fileName));

        return lines.stream()
                .map(InputReader::parseLine)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private static Participant parseLine(String line) {
        String[] parts = line.split("\\t");
        if (parts.length != 3) {
            System.out.println("Invalid line format: " + line);
            return null;
        }

        try {
            int id = Integer.parseInt(parts[0].trim());
            Set<String> attributes = new HashSet<>(Arrays.asList(parts[1].split(",")));
            Set<String> desiredAttributes = new HashSet<>(Arrays.asList(parts[2].split(",")));
            return new Participant(id, attributes, desiredAttributes);
        } catch (NumberFormatException e) {
            System.out.println("Invalid participant ID in line: " + line);
            return null;
        }
    }
}

