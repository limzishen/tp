package seedu.address.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.person.Person;

/**
 * An UI component that displays information of a {@code Person}.
 */
public class PersonCard extends UiPart<Region> {

    private static final String FXML = "PersonListCard.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final Person person;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label email;
    @FXML
    private FlowPane tutorialGroup;
    @FXML
    private GridPane attendanceTable;
    private AttendanceTable attendanceTableManager;

    /**
     * Creates a {@code PersonCode} with the given {@code Person} and index to display.
     */
    public PersonCard(Person person, int displayedIndex) {
        super(FXML);
        this.person = person;
        id.setText(displayedIndex + ". ");
        name.setText(person.getName().fullName);
        email.setText(person.getEmail().value);
        person.getTags().forEach(tag -> tutorialGroup.getChildren().add(new Label(tag.tagName)));
        attendanceTableManager = new AttendanceTable(attendanceTable);
    }

    /**
     * Sets the attendance status for a specific week.
     * @param week Week number (1-13)
     * @param status "present" (green), "absent" (red), or "default" (grey)
     */
    public void setAttendanceStatus(int week, String status) {
        attendanceTableManager.setAttendanceStatus(week, status);
    }

    /**
     * Sets the attendance color for a specific week directly.
     * @param week Week number (1-13)
     * @param color Hex color code (e.g., "#90EE90" for green)
     */
    public void setAttendanceColor(int week, String color) {
        attendanceTableManager.setAttendanceColor(week, color);
    }
}
