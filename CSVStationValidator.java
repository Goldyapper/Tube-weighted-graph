import java.io.*;
import java.util.*;

public class CSVStationValidator {

    public static void main(String[] args) {
        String stationFile = "tubestations.txt";
        String csvFile = "edges.csv";

        // Load stations into a set for fast lookup
        Set<String> stations = new HashSet<>();
        try (BufferedReader br = new BufferedReader(new FileReader(stationFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                stations.add(line.trim().toLowerCase());
            }
        } catch (IOException e) {
            System.err.println("Error reading station file: " + e.getMessage());
            return;
        }

        // Check each CSV edge
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            br.readLine(); // skip header
            int lineNum = 1;
            boolean hasErrors = false;

            while ((line = br.readLine()) != null) {
                lineNum++;
                String[] parts = line.split(",");
                if (parts.length != 3) continue;

                String src = parts[0].trim().toLowerCase();
                String dest = parts[1].trim().toLowerCase();

                boolean invalid = false;
                if (!stations.contains(src)) {
                    System.out.println("Line " + lineNum + ": Invalid source station -> " + parts[0]);
                    invalid = true;
                }
                if (!stations.contains(dest)) {
                    System.out.println("Line " + lineNum + ": Invalid destination station -> " + parts[1]);
                    invalid = true;
                }
                if (invalid) hasErrors = true;
            }

            if (!hasErrors) {
                System.out.println("All CSV stations match the station list!");
            }

        } catch (IOException e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
        }
    }
}
