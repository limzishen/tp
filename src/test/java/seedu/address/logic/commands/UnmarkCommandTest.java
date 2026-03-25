package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Attendance;
import seedu.address.model.person.Person;
import seedu.address.model.person.TutorialGroup;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code UnmarkCommand}.
 */
public class UnmarkCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Person personToUnmark = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        int weekToUnmark = 1;
        Person markedPerson = createPersonWithMarkedWeek(personToUnmark, weekToUnmark);
        model.setPerson(personToUnmark, markedPerson);

        UnmarkCommand unmarkCommand = new UnmarkCommand(INDEX_FIRST_PERSON, weekToUnmark);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        Person unmarkedPerson = createPersonWithUnmarkedWeek(markedPerson, weekToUnmark);
        expectedModel.setPerson(markedPerson, unmarkedPerson);

        String expectedMessage = String.format(UnmarkCommand.MESSAGE_UNMARK_PERSON_SUCCESS,
                Messages.format(unmarkedPerson));

        assertCommandSuccess(unmarkCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        UnmarkCommand unmarkCommand = new UnmarkCommand(outOfBoundIndex, 1);

        assertCommandFailure(unmarkCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_alreadyUnmarked_throwsCommandException() {
        UnmarkCommand unmarkCommand = new UnmarkCommand(INDEX_FIRST_PERSON, 2);
        assertCommandFailure(unmarkCommand, model, UnmarkCommand.MESSAGE_ALREADY_UNMARKED);
    }

    @Test
    public void execute_validIndexFilteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person personToUnmark = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        int weekToUnmark = 3;
        Person markedPerson = createPersonWithMarkedWeek(personToUnmark, weekToUnmark);
        model.setPerson(personToUnmark, markedPerson);

        UnmarkCommand unmarkCommand = new UnmarkCommand(INDEX_FIRST_PERSON, weekToUnmark);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        Person unmarkedPerson = createPersonWithUnmarkedWeek(markedPerson, weekToUnmark);
        expectedModel.setPerson(markedPerson, unmarkedPerson);

        String expectedMessage = String.format(UnmarkCommand.MESSAGE_UNMARK_PERSON_SUCCESS,
                Messages.format(unmarkedPerson));

        assertCommandSuccess(unmarkCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        UnmarkCommand unmarkCommand = new UnmarkCommand(outOfBoundIndex, 1);

        assertCommandFailure(unmarkCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_groupUnmark_success() {
        TutorialGroup tutorialGroup = new TutorialGroup("T01");
        int weekToUnmark = 4;

        markGroupInModel(model, tutorialGroup, weekToUnmark);

        UnmarkCommand unmarkCommand = new UnmarkCommand(tutorialGroup, weekToUnmark);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        markGroupInModel(expectedModel, tutorialGroup, weekToUnmark);
        unmarkGroupInModel(expectedModel, tutorialGroup, weekToUnmark);

        String expectedMessage = String.format(UnmarkCommand.MESSAGE_UNMARK_GROUP_SUCCESS, 2, tutorialGroup,
                weekToUnmark);

        assertCommandSuccess(unmarkCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_groupUnmark_noGroup_throwsCommandException() {
        TutorialGroup tutorialGroup = new TutorialGroup("T99");
        UnmarkCommand unmarkCommand = new UnmarkCommand(tutorialGroup, 1);

        assertCommandFailure(unmarkCommand, model,
                String.format(UnmarkCommand.MESSAGE_GROUP_NOT_FOUND, tutorialGroup));
    }

    @Test
    public void execute_groupUnmark_alreadyUnmarked_throwsCommandException() {
        TutorialGroup tutorialGroup = new TutorialGroup("T01");
        UnmarkCommand unmarkCommand = new UnmarkCommand(tutorialGroup, 1);

        assertCommandFailure(unmarkCommand, model,
                String.format(UnmarkCommand.MESSAGE_GROUP_ALREADY_UNMARKED, tutorialGroup, 1));
    }

    @Test
    public void equals() {
        UnmarkCommand unmarkFirstCommand = new UnmarkCommand(INDEX_FIRST_PERSON, 1);
        UnmarkCommand unmarkSecondCommand = new UnmarkCommand(INDEX_SECOND_PERSON, 1);
        UnmarkCommand unmarkFirstCommandWeek2 = new UnmarkCommand(INDEX_FIRST_PERSON, 2);
        UnmarkCommand unmarkGroupCommand = new UnmarkCommand(new TutorialGroup("T01"), 1);

        assertTrue(unmarkFirstCommand.equals(unmarkFirstCommand));

        UnmarkCommand unmarkFirstCommandCopy = new UnmarkCommand(INDEX_FIRST_PERSON, 1);
        assertTrue(unmarkFirstCommand.equals(unmarkFirstCommandCopy));

        assertFalse(unmarkFirstCommand.equals(1));
        assertFalse(unmarkFirstCommand.equals(null));
        assertFalse(unmarkFirstCommand.equals(unmarkSecondCommand));
        assertFalse(unmarkFirstCommand.equals(unmarkFirstCommandWeek2));
        assertFalse(unmarkFirstCommand.equals(unmarkGroupCommand));
    }

    private void markGroupInModel(Model targetModel, TutorialGroup tutorialGroup, int week) {
        List<Person> allPersons = new ArrayList<>(targetModel.getAddressBook().getPersonList());
        for (Person person : allPersons) {
            if (person.getTutorialGroup().equals(tutorialGroup)) {
                Person markedPerson = createPersonWithMarkedWeek(person, week);
                targetModel.setPerson(person, markedPerson);
            }
        }
    }

    private void unmarkGroupInModel(Model targetModel, TutorialGroup tutorialGroup, int week) {
        List<Person> allPersons = new ArrayList<>(targetModel.getAddressBook().getPersonList());
        for (Person person : allPersons) {
            if (person.getTutorialGroup().equals(tutorialGroup)) {
                Person unmarkedPerson = createPersonWithUnmarkedWeek(person, week);
                targetModel.setPerson(person, unmarkedPerson);
            }
        }
    }

    /**
     * Helper method to create a person with a specific week marked.
     */
    private Person createPersonWithMarkedWeek(Person person, int week) {
        Attendance updatedAttendance = person.getAttendance().createCopyWithMarkedWeek(week);
        return new Person(
                person.getName(),
                person.getPhone(),
                person.getEmail(),
                person.getTeleHandle(),
                person.getStudentId(),
                person.getTutorialGroup(),
                updatedAttendance
        );
    }

    /**
     * Helper method to create a person with a specific week unmarked.
     */
    private Person createPersonWithUnmarkedWeek(Person person, int week) {
        Attendance updatedAttendance = person.getAttendance().createCopyWithUnmarkedWeek(week);
        return new Person(
                person.getName(),
                person.getPhone(),
                person.getEmail(),
                person.getTeleHandle(),
                person.getStudentId(),
                person.getTutorialGroup(),
                updatedAttendance
        );
    }
}
