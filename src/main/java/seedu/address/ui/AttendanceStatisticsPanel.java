package seedu.address.ui;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

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
    private static final double FIRST_COLUMN_WIDTH = 100;
    private static final double WEEK_COLUMN_WIDTH = 50;
    private static final double RATE_COLUMN_WIDTH = 60;

    @FXML
    private ScrollPane scrollPane;

    private final GridPane statisticsGrid;

    /**
     * Creates an {@code AttendanceStatisticsPanel} with the given person list.
     */
    public AttendanceStatisticsPanel(ObservableList<Person> personList) {
        super(FXML);
        statisticsGrid = new GridPane();
        statisticsGrid.getStyleClass().add(GRID_STYLE_CLASS);
        statisticsGrid.setHgap(5);
        statisticsGrid.setVgap(5);
        scrollPane.setContent(statisticsGrid);
        updateStatistics(personList);
        personList.addListener((ListChangeListener<Person>) change -> updateStatistics(personList));
    }

    /**
     * Updates the attendance statistics displayed in the panel.
     */
    private void updateStatistics(ObservableList<Person> personList) {
        statisticsGrid.getChildren().clear();

        // Group persons by tutorial group
        Map<TutorialGroup, java.util.List<Person>> groupMap = new HashMap<>();
        for (Person person : personList) {
            groupMap.computeIfAbsent(person.getTutorialGroup(), k -> new java.util.ArrayList<>()).add(person);
        }

        // Sort tutorial groups
        SortedSet<TutorialGroup> sortedGroups = new TreeSet<>((g1, g2) -> g1.value.compareTo(g2.value));
        sortedGroups.addAll(groupMap.keySet());

        int rowIndex = 0;

        // Create header row
        Label headerLabel = new Label("Tutorial Group");
        headerLabel.getStyleClass().addAll(HEADER_STYLE_CLASS, FIRST_COLUMN_STYLE_CLASS);
        headerLabel.setPrefWidth(FIRST_COLUMN_WIDTH);
        statisticsGrid.add(headerLabel, 0, rowIndex);

        for (int week = 1; week <= Attendance.MAX_WEEKS; week++) {
            Label weekLabel = new Label("W" + week);
            weekLabel.getStyleClass().add(HEADER_STYLE_CLASS);
            weekLabel.setPrefWidth(WEEK_COLUMN_WIDTH);
            statisticsGrid.add(weekLabel, week, rowIndex);
        }

        Label rateLabel = new Label("Rate");
        rateLabel.getStyleClass().add(HEADER_STYLE_CLASS);
        rateLabel.setPrefWidth(RATE_COLUMN_WIDTH);
        statisticsGrid.add(rateLabel, Attendance.MAX_WEEKS + 1, rowIndex);

        rowIndex++;

        // Create data rows for each tutorial group
        for (TutorialGroup group : sortedGroups) {
            java.util.List<Person> studentsInGroup = groupMap.get(group);
            Label groupLabel = new Label(group.value);
            groupLabel.getStyleClass().addAll(CELL_STYLE_CLASS, FIRST_COLUMN_STYLE_CLASS);
            groupLabel.setPrefWidth(FIRST_COLUMN_WIDTH);
            statisticsGrid.add(groupLabel, 0, rowIndex);

            int totalAttendance = 0;
            int possibleAttendance = studentsInGroup.size() * Attendance.MAX_WEEKS;

            for (int week = 1; week <= Attendance.MAX_WEEKS; week++) {
                int attendanceCount = 0;
                for (Person person : studentsInGroup) {
                    if (person.getAttendance().isMarked(week)) {
                        attendanceCount++;
                    }
                }
                totalAttendance += attendanceCount;

                double attendanceRate = studentsInGroup.isEmpty()
                        ? 0
                        : (double) attendanceCount / studentsInGroup.size() * 100;
                Label rateCell = new Label(String.format("%.0f%%", attendanceRate));
                rateCell.getStyleClass().add(CELL_STYLE_CLASS);
                applyAttendanceRateTierStyle(rateCell, attendanceRate);
                rateCell.setPrefWidth(WEEK_COLUMN_WIDTH);
                statisticsGrid.add(rateCell, week, rowIndex);
            }

            double overallRate = possibleAttendance == 0 ? 0 : (double) totalAttendance / possibleAttendance * 100;
            Label overallRateLabel = new Label(String.format("%.1f%%", overallRate));
            overallRateLabel.getStyleClass().add(CELL_STYLE_CLASS);
            applyAttendanceRateTierStyle(overallRateLabel, overallRate);
            overallRateLabel.setPrefWidth(RATE_COLUMN_WIDTH);
            statisticsGrid.add(overallRateLabel, Attendance.MAX_WEEKS + 1, rowIndex);

            rowIndex++;
        }

        // Add overall statistics row
        Label totalLabel = new Label("Overall");
        totalLabel.getStyleClass().addAll(CELL_STYLE_CLASS, FIRST_COLUMN_STYLE_CLASS);
        totalLabel.setPrefWidth(FIRST_COLUMN_WIDTH);
        statisticsGrid.add(totalLabel, 0, rowIndex);

        int totalAllAttendance = 0;
        int totalAllPossible = personList.size() * Attendance.MAX_WEEKS;

        for (int week = 1; week <= Attendance.MAX_WEEKS; week++) {
            int attendanceCount = 0;
            for (Person person : personList) {
                if (person.getAttendance().isMarked(week)) {
                    attendanceCount++;
                }
            }
            totalAllAttendance += attendanceCount;

            double attendanceRate = personList.isEmpty() ? 0 : (double) attendanceCount / personList.size() * 100;
            Label rateCell = new Label(String.format("%.0f%%", attendanceRate));
            rateCell.getStyleClass().add(CELL_STYLE_CLASS);
            applyAttendanceRateTierStyle(rateCell, attendanceRate);
            rateCell.setPrefWidth(WEEK_COLUMN_WIDTH);
            statisticsGrid.add(rateCell, week, rowIndex);
        }

        double overallRate = totalAllPossible == 0 ? 0 : (double) totalAllAttendance / totalAllPossible * 100;
        Label overallRateLabel = new Label(String.format("%.1f%%", overallRate));
        overallRateLabel.getStyleClass().add(CELL_STYLE_CLASS);
        applyAttendanceRateTierStyle(overallRateLabel, overallRate);
        overallRateLabel.setPrefWidth(RATE_COLUMN_WIDTH);
        statisticsGrid.add(overallRateLabel, Attendance.MAX_WEEKS + 1, rowIndex);
    }

    /**
     * Colors percentage cells by tier: high ({@code >= 80%}), medium ({@code 50%}–{@code 79%}), low ({@code < 50%}).
     */
    private void applyAttendanceRateTierStyle(Label label, double ratePercent) {
        label.getStyleClass().removeAll(RATE_HIGH_CLASS, RATE_MEDIUM_CLASS, RATE_LOW_CLASS);
        if (ratePercent >= 80.0) {
            label.getStyleClass().add(RATE_HIGH_CLASS);
        } else if (ratePercent >= 50.0) {
            label.getStyleClass().add(RATE_MEDIUM_CLASS);
        } else {
            label.getStyleClass().add(RATE_LOW_CLASS);
        }
    }
}
