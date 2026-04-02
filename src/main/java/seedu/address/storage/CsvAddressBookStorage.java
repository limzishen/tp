package seedu.address.storage;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import seedu.address.model.person.Attendance;
import seedu.address.model.person.Person;

/**
 * Handles writing of address book data to a CSV file.
 */
public class CsvAddressBookStorage {

    private final Path filePath;

    public CsvAddressBookStorage(Path filePath) {
        this.filePath = filePath;
    }

    public Path getFilePath() {
        return filePath;
    }

    /**
     * Writes the given list of persons to a CSV file at the configured path.
     *
     * @param persons the list of persons to export
     * @throws IOException if writing to the file fails
     */
    public void writeCsv(List<Person> persons) throws IOException {
        Files.createDirectories(filePath.getParent() == null ? Paths.get(".") : filePath.getParent());

        try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(filePath))) {
            // Write header
            StringBuilder header = new StringBuilder("Student,StudentID,Email,Tutorial");
            for (int week = 1; week <= Attendance.MAX_WEEKS; week++) {
                header.append(",Week").append(week);
            }
            writer.println(header);

            // Write one row per person
            for (Person person : persons) {
                StringBuilder row = new StringBuilder();
                row.append(escapeCsv(person.getName().fullName)).append(",");
                row.append(escapeCsv(person.getStudentId().value)).append(",");
                row.append(escapeCsv(person.getEmail().value)).append(",");
                row.append(escapeCsv(person.getTutorialGroup().value));

                for (int week = 1; week <= Attendance.MAX_WEEKS; week++) {
                    row.append(",").append(person.getAttendance().isMarked(week) ? "1" : "0");
                }
                writer.println(row);
            }
        }
    }

    /**
     * Wraps a field in double quotes and escapes any internal double quotes.
     */
    private String escapeCsv(String value) {
        if (value == null) {
            return "";
        }
        String escaped = value.replace("\"", "\"\"");
        return "\"" + escaped + "\"";
    }
}
