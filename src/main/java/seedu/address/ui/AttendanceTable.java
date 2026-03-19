package seedu.address.ui;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import seedu.address.model.person.Attendance;

/**
 * A UI component that displays a student's attendance table with 13 weeks.
 * Each week is represented by a single cell that can be colored grey (default), green (present), or red (absent).
 */
public class AttendanceTable {

    private static final String CELL_STYLE_PREFIX =
            "-fx-alignment: CENTER; -fx-border-color: #cccccc; -fx-border-width: 1; -fx-background-color: ";

    private final GridPane attendanceTable;
    private final Attendance attendance;

    /**
     * Creates an {@code AttendanceTable} with the given GridPane and Attendance.
     * @param attendanceTable The GridPane to use for the attendance table
     * @param attendance The Attendance object containing attendance data for 13 weeks
     */
    public AttendanceTable(GridPane attendanceTable, Attendance attendance) {
        this.attendanceTable = attendanceTable;
        this.attendance = attendance;
        initializeTable();
    }

    /**
     * Creates an {@code AttendanceTable} with the given GridPane.
     * @param attendanceTable The GridPane to use for the attendance table
     */
    public AttendanceTable(GridPane attendanceTable) {
        this(attendanceTable, new Attendance());
    }

    /**
     * Initializes the attendance table with 13 columns for 13 weeks.
     * Row 0: Week headers (W1 - W13)
     * Row 1: Attendance cells (colored based on the Attendance object)
     */
    private void initializeTable() {
        // Add week headers (row 0)
        for (int week = 1; week <= 13; week++) {
            Label weekHeader = new Label("W" + week);
            weekHeader.setStyle("-fx-text-alignment: CENTER; -fx-font-size: 10; -fx-padding: 5;");
            weekHeader.setPrefWidth(35);
            weekHeader.setMinHeight(25);
            attendanceTable.add(weekHeader, week - 1, 0);
        }

        // Add attendance cells (row 1) colored based on attendance status
        for (int week = 1; week <= 13; week++) {
            Label attendanceCell = new Label();
            // Get color based on attendance status
            String color = attendance.isMarked(week) ? "#90EE90" : "#d3d3d3"; // Green if marked, grey otherwise
            attendanceCell.setStyle(CELL_STYLE_PREFIX + color + ";");
            attendanceCell.setPrefWidth(35);
            attendanceCell.setMinHeight(25);
            attendanceCell.setMaxHeight(Double.MAX_VALUE);
            attendanceTable.add(attendanceCell, week - 1, 1);
        }
    }

    /**
     * Sets the attendance status for a specific week.
     * @param week Week number (1-13)
     * @param status "present" (green), "absent" (red), or "default" (grey)
     */
    public void setAttendanceStatus(int week, String status) {
        if (week < 1 || week > 13) {
            return;
        }

        for (Node node : attendanceTable.getChildren()) {
            Integer columnIndex = GridPane.getColumnIndex(node);
            Integer rowIndex = GridPane.getRowIndex(node);

            if (columnIndex != null && rowIndex != null && columnIndex == week - 1 && rowIndex == 1) {
                String backgroundColor;
                switch (status.toLowerCase()) {
                case "present":
                    backgroundColor = "#90EE90"; // Light green
                    break;
                case "absent":
                    backgroundColor = "#FF6B6B"; // Light red
                    break;
                default:
                    backgroundColor = "#d3d3d3"; // Grey
                }
                String style = CELL_STYLE_PREFIX + backgroundColor + ";";
                Label cellLabel = (Label) node;
                cellLabel.setStyle(style);
                break;
            }
        }
    }

    /**
     * Sets the attendance color for a specific week directly using a hex color code.
     * @param week Week number (1-13)
     * @param color Hex color code (e.g., "#90EE90" for green, "#FF6B6B" for red, "#d3d3d3" for grey)
     */
    public void setAttendanceColor(int week, String color) {
        if (week < 1 || week > 13) {
            return;
        }

        for (Node node : attendanceTable.getChildren()) {
            Integer columnIndex = GridPane.getColumnIndex(node);
            Integer rowIndex = GridPane.getRowIndex(node);

            if (columnIndex != null && rowIndex != null && columnIndex == week - 1 && rowIndex == 1) {
                String style = CELL_STYLE_PREFIX + color + ";";
                Label cellLabel = (Label) node;
                cellLabel.setStyle(style);
                break;
            }
        }
    }
}
