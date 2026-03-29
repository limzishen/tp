package seedu.address.ui;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import seedu.address.model.person.Attendance;

/**
 * A UI component that displays a student's attendance table with 13 weeks.
 * Each week is represented by a single cell that can be colored for unmarked (muted grey),
 * present (green), or absent (red), using the application dark theme palette.
 */
public class AttendanceTable {

    private static final String STYLE_CLASS_CELL = "attendance-cell";
    private static final String STYLE_CLASS_WEEK_HEADER = "attendance-week-header";
    private static final String STYLE_CLASS_UNMARKED = "attendance-cell-unmarked";
    private static final String STYLE_CLASS_PRESENT = "attendance-cell-present";
    private static final String STYLE_CLASS_ABSENT = "attendance-cell-absent";

    private static final String CELL_INLINE_STYLE_PREFIX =
            "-fx-alignment: CENTER; -fx-border-color: #334155; -fx-border-width: 1; -fx-background-color: ";

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
            weekHeader.getStyleClass().add(STYLE_CLASS_WEEK_HEADER);
            weekHeader.setPrefWidth(35);
            weekHeader.setMinHeight(25);
            attendanceTable.add(weekHeader, week - 1, 0);
        }

        // Add attendance cells (row 1) colored based on attendance status
        for (int week = 1; week <= 13; week++) {
            Label attendanceCell = new Label();
            applyAttendanceStateStyle(attendanceCell, attendance.isMarked(week) ? "present" : "default");
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
                Label cellLabel = (Label) node;
                applyAttendanceStateStyle(cellLabel, status);
                break;
            }
        }
    }

    /**
     * Sets the attendance color for a specific week directly using a hex color code.
     * @param week Week number (1-13)
     * @param color Hex color code (e.g., theme green {@code #22C55E})
     */
    public void setAttendanceColor(int week, String color) {
        if (week < 1 || week > 13) {
            return;
        }

        for (Node node : attendanceTable.getChildren()) {
            Integer columnIndex = GridPane.getColumnIndex(node);
            Integer rowIndex = GridPane.getRowIndex(node);

            if (columnIndex != null && rowIndex != null && columnIndex == week - 1 && rowIndex == 1) {
                Label cellLabel = (Label) node;
                cellLabel.getStyleClass().removeAll(STYLE_CLASS_UNMARKED, STYLE_CLASS_PRESENT, STYLE_CLASS_ABSENT);
                cellLabel.getStyleClass().add(STYLE_CLASS_CELL);
                cellLabel.setStyle(CELL_INLINE_STYLE_PREFIX + color + ";");
                break;
            }
        }
    }

    /**
     * Applies themed style classes or inline color for a cell based on logical status.
     */
    private void applyAttendanceStateStyle(Label cell, String status) {
        cell.setStyle(null);
        cell.getStyleClass().removeAll(STYLE_CLASS_CELL, STYLE_CLASS_UNMARKED, STYLE_CLASS_PRESENT,
                STYLE_CLASS_ABSENT);
        cell.getStyleClass().add(STYLE_CLASS_CELL);

        switch (status.toLowerCase()) {
        case "present":
            cell.getStyleClass().add(STYLE_CLASS_PRESENT);
            break;
        case "absent":
            cell.getStyleClass().add(STYLE_CLASS_ABSENT);
            break;
        default:
            cell.getStyleClass().add(STYLE_CLASS_UNMARKED);
            break;
        }
    }
}
