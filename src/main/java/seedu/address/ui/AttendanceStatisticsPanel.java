package seedu.address.ui;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import seedu.address.model.person.Attendance;
import seedu.address.model.person.Person;
import seedu.address.model.person.TutorialGroup;

/**
 * Panel for displaying attendance statistics for each tutorial group per week.
 */
public class AttendanceStatisticsPanel extends UiPart<VBox> {
    private static final String FXML = "AttendanceStatisticsPanel.fxml";
    private static final String GRID_STYLE_CLASS = "attendance-statistics-grid";
    private static final String CELL_STYLE_CLASS = "attendance-statistics-cell";
    private static final String HEADER_STYLE_CLASS = "attendance-statistics-header";
    private static final String FIRST_COLUMN_STYLE_CLASS = "attendance-statistics-first-column";
    private static final String RATE_HIGH_CLASS = "attendance-rate-high";
    private static final String RATE_MEDIUM_CLASS = "attendance-rate-medium";
    private static final String RATE_LOW_CLASS = "attendance-rate-low";
    private static final String TUTORIAL_GROUP_HEADER_TEXT = "Tutorial Group";
    private static final String RATE_HEADER_TEXT = "Rate";
    private static final String OVERALL_ROW_TEXT = "Overall";
    private static final String WEEK_HEADER_FORMAT = "W%d";
    private static final String WEEK_RATE_FORMAT = "%.0f%%";
    private static final String OVERALL_RATE_FORMAT = "%.1f%%";
    private static final double FIRST_COLUMN_WIDTH = 100;
    private static final double WEEK_COLUMN_WIDTH = 50;
    private static final double RATE_COLUMN_WIDTH = 60;
    private static final double GRID_GAP = 5;
    private static final double HIGH_ATTENDANCE_THRESHOLD = 80.0;
    private static final double MEDIUM_ATTENDANCE_THRESHOLD = 50.0;
    private static final int HEADER_ROW_INDEX = 0;
    private static final int FIRST_DATA_ROW_INDEX = 1;
    private static final int FIRST_COLUMN_INDEX = 0;
    private static final int OVERALL_RATE_COLUMN_INDEX = Attendance.MAX_WEEKS + 1;

    @FXML
    private ScrollPane scrollPane;

    private final ObservableList<Person> personList;
    private final GridPane statisticsGrid;

    /**
     * Creates an {@code AttendanceStatisticsPanel} with the given person list.
     */
    public AttendanceStatisticsPanel(ObservableList<Person> personList) {
        super(FXML);
        this.personList = personList;
        statisticsGrid = createStatisticsGrid();
        scrollPane.setContent(statisticsGrid);
        updateStatistics();
        personList.addListener((ListChangeListener<Person>) ignored -> updateStatistics());
    }

    /**
     * Updates the attendance statistics displayed in the panel.
     */
    private void updateStatistics() {
        statisticsGrid.getChildren().clear();

        Map<TutorialGroup, List<Person>> studentsByGroup = groupPersonsByTutorialGroup(personList);
        addHeaderRow();
        int nextRowIndex = addTutorialGroupRows(studentsByGroup);
        addStatisticsRow(OVERALL_ROW_TEXT, personList, nextRowIndex);
    }

    private GridPane createStatisticsGrid() {
        GridPane grid = new GridPane();
        grid.getStyleClass().add(GRID_STYLE_CLASS);
        grid.setHgap(GRID_GAP);
        grid.setVgap(GRID_GAP);
        return grid;
    }

    private Map<TutorialGroup, List<Person>> groupPersonsByTutorialGroup(List<Person> persons) {
        Map<TutorialGroup, List<Person>> studentsByGroup =
                new TreeMap<>(Comparator.comparing(tutorialGroup -> tutorialGroup.value));

        for (Person person : persons) {
            studentsByGroup.computeIfAbsent(person.getTutorialGroup(), unused -> new ArrayList<>()).add(person);
        }

        return studentsByGroup;
    }

    private void addHeaderRow() {
        statisticsGrid.add(createFirstColumnHeaderCell(TUTORIAL_GROUP_HEADER_TEXT),
                FIRST_COLUMN_INDEX, HEADER_ROW_INDEX);

        for (int week = 1; week <= Attendance.MAX_WEEKS; week++) {
            statisticsGrid.add(createHeaderCell(String.format(WEEK_HEADER_FORMAT, week), WEEK_COLUMN_WIDTH),
                    week, HEADER_ROW_INDEX);
        }

        statisticsGrid.add(createHeaderCell(RATE_HEADER_TEXT, RATE_COLUMN_WIDTH),
                OVERALL_RATE_COLUMN_INDEX, HEADER_ROW_INDEX);
    }

    private int addTutorialGroupRows(Map<TutorialGroup, List<Person>> studentsByGroup) {
        int rowIndex = FIRST_DATA_ROW_INDEX;
        for (Map.Entry<TutorialGroup, List<Person>> entry : studentsByGroup.entrySet()) {
            addStatisticsRow(entry.getKey().value, entry.getValue(), rowIndex);
            rowIndex++;
        }
        return rowIndex;
    }

    private void addStatisticsRow(String rowLabel, List<Person> students, int rowIndex) {
        statisticsGrid.add(createFirstColumnDataCell(rowLabel), FIRST_COLUMN_INDEX, rowIndex);

        int totalAttendance = addWeeklyAttendanceCells(students, rowIndex);
        addOverallAttendanceCell(students.size(), totalAttendance, rowIndex);
    }

    private int addWeeklyAttendanceCells(List<Person> students, int rowIndex) {
        int totalAttendance = 0;

        for (int week = 1; week <= Attendance.MAX_WEEKS; week++) {
            int attendanceCount = countMarkedStudentsForWeek(students, week);
            totalAttendance += attendanceCount;

            double attendanceRate = calculatePercentage(attendanceCount, students.size());
            statisticsGrid.add(createRateCell(attendanceRate, WEEK_COLUMN_WIDTH, WEEK_RATE_FORMAT), week, rowIndex);
        }

        return totalAttendance;
    }

    private void addOverallAttendanceCell(int studentCount, int totalAttendance, int rowIndex) {
        int totalPossibleAttendance = studentCount * Attendance.MAX_WEEKS;
        double overallRate = calculatePercentage(totalAttendance, totalPossibleAttendance);

        statisticsGrid.add(createRateCell(overallRate, RATE_COLUMN_WIDTH, OVERALL_RATE_FORMAT),
                OVERALL_RATE_COLUMN_INDEX, rowIndex);
    }

    private int countMarkedStudentsForWeek(List<Person> students, int week) {
        int attendanceCount = 0;

        for (Person person : students) {
            if (person.getAttendance().isMarked(week)) {
                attendanceCount++;
            }
        }

        return attendanceCount;
    }

    private double calculatePercentage(int numerator, int denominator) {
        return denominator == 0 ? 0 : (double) numerator / denominator * 100;
    }

    private Label createFirstColumnHeaderCell(String text) {
        return createStyledLabel(text, FIRST_COLUMN_WIDTH, HEADER_STYLE_CLASS, FIRST_COLUMN_STYLE_CLASS);
    }

    private Label createHeaderCell(String text, double width) {
        return createStyledLabel(text, width, HEADER_STYLE_CLASS);
    }

    private Label createFirstColumnDataCell(String text) {
        return createStyledLabel(text, FIRST_COLUMN_WIDTH, CELL_STYLE_CLASS, FIRST_COLUMN_STYLE_CLASS);
    }

    private Label createDataCell(String text, double width) {
        return createStyledLabel(text, width, CELL_STYLE_CLASS);
    }

    private Label createRateCell(double attendanceRate, double width, String format) {
        Label rateCell = createDataCell(String.format(format, attendanceRate), width);
        applyAttendanceRateTierStyle(rateCell, attendanceRate);
        return rateCell;
    }

    private Label createStyledLabel(String text, double width, String... styleClasses) {
        Label label = new Label(text);
        label.getStyleClass().addAll(styleClasses);
        label.setPrefWidth(width);
        return label;
    }

    /**
     * Colors percentage cells by tier: high ({@code >= 80%}), medium ({@code 50%}–{@code 79%}), low ({@code < 50%}).
     */
    private void applyAttendanceRateTierStyle(Label label, double ratePercent) {
        label.getStyleClass().removeAll(RATE_HIGH_CLASS, RATE_MEDIUM_CLASS, RATE_LOW_CLASS);
        if (ratePercent >= HIGH_ATTENDANCE_THRESHOLD) {
            label.getStyleClass().add(RATE_HIGH_CLASS);
        } else if (ratePercent >= MEDIUM_ATTENDANCE_THRESHOLD) {
            label.getStyleClass().add(RATE_MEDIUM_CLASS);
        } else {
            label.getStyleClass().add(RATE_LOW_CLASS);
        }
    }
}
