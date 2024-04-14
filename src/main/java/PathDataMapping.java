import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;

public class PathDataMapping {

    public static void main(String[] args) {
        String filePath = "TestData.csv";
        String outputPath = "AlignmentResults.txt";
        processPairs(filePath, outputPath);
    }

    private static void processPairs(String filePath, String outputPath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath));
             PrintWriter writer = new PrintWriter(new FileWriter(outputPath))) {
            String line1, line2;
            while ((line1 = reader.readLine()) != null && (line2 = reader.readLine()) != null) {
                //correction for parsing
                line1 = trimFirstAndLastCharacter(line1);
                line2 = trimFirstAndLastCharacter(line2);
                
                List<String> seq1List = parseLine(line1);
                List<String> seq2List = parseLine(line2);
                String seq1 = buildRoomKeys(seq1List.toArray(new String[0]));
                String seq2 = buildRoomKeys(seq2List.toArray(new String[0]));
                alignSequences(seq1, seq2, writer);
            }
        } catch (IOException e) {
            System.err.println("An error occurred while processing the file: " + e.getMessage());
        }
    }

    //Helper for parsing
    private static String trimFirstAndLastCharacter(String str) {
        if (str != null && str.length() > 1) {
            return str.substring(1, str.length() - 1);
        }
        return str; // Return the original string if it's too short to trim
    }

    private static List<String> parseLine(String csvLine) {
        List<String> fields = new ArrayList<>();
        try (CSVParser parser = CSVParser.parse(csvLine, CSVFormat.DEFAULT)) {
            if (parser.iterator().hasNext()) {
                fields.addAll(parser.iterator().next().toList());
            }
        } catch (IOException e) {
            System.err.println("Failed to parse line: " + e.getMessage());
        }
        return fields;
    }

    private static void alignSequences(String seq1, String seq2, PrintWriter writer) {
        int match = 1;
        int mismatch = -1;
        int gap = 0;

        int[][] matrix = SmithWaterman.smithWaterman(seq1, seq2, match, mismatch, gap);
        AlignmentResult result = AlignmentResult.traceback(matrix, seq1, seq2, match, mismatch, gap);

        writer.println("Seq1: " + seq1);
        writer.println("Seq2: " + seq2);
        writer.println("Optimal Alignment Score: " + result.score);
        writer.println("Aligned Sequence 1 (Recorded): " + result.alignedSeq1);

        writer.println("Aligned Sequence 2 (Verified): " + result.alignedSeq2);

        writer.println("Gap Counts:");
        writer.println("Seq1=   " + result.gapCountSeq1);
        writer.println("Seq2=  " + result.gapCountSeq2);

        double percentageOfMatch = (double) result.matchCount / seq2.length() * 100;
        writer.printf("Percentage of Match: %.2f%%\n", percentageOfMatch);
        writer.println("----------------------------------------");
    }

    public static String buildRoomKeys(String[] roomsToCheck) {
        HashMap<String, String> rooms = new HashMap<>();
        rooms.put("room 1", "1");
        rooms.put("room 2", "2");
        rooms.put("room 3", "3");
        rooms.put("room 4", "4");
        rooms.put("room 5", "5");
        rooms.put("room 6", "6");
        rooms.put("room 7", "7");
        rooms.put("room 8", "8");
        rooms.put("room 9", "9");
        rooms.put("room 10", "0");
        rooms.put("nurse station", "N");
        rooms.put("pharmacy", "P");

        StringBuilder keysPath = new StringBuilder();
        for (String room : roomsToCheck) {
            String trimmedRoom = room.trim().toLowerCase();  // Trim whitespace
            String key = rooms.getOrDefault(trimmedRoom, "?");  // '?' marks unrecognized rooms
            keysPath.append(key);
        }
        System.out.println("Room keys: " + keysPath);  // Debug output
        return keysPath.toString();
    }
}
