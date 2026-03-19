package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Attendance;
import seedu.address.model.person.Person;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code MarkCommand}.
 */
public class MarkCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Person personToMark = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        int weekToMark = 1;
        MarkCommand markCommand = new MarkCommand(INDEX_FIRST_PERSON, weekToMark);

        // Create expected model with the marked person
        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        Person markedPerson = createPersonWithMarkedWeek(personToMark, weekToMark);
        expectedModel.setPerson(personToMark, markedPerson);

        String expectedMessage = String.format(MarkCommand.MESSAGE_MARK_PERSON_SUCCESS,
                Messages.format(markedPerson));

        assertCommandSuccess(markCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_validIndexMultipleWeeks_success() {
        Person personToMark = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        int weekToMark = 5;
        MarkCommand markCommand = new MarkCommand(INDEX_FIRST_PERSON, weekToMark);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        Person markedPerson = createPersonWithMarkedWeek(personToMark, weekToMark);
        expectedModel.setPerson(personToMark, markedPerson);

        String expectedMessage = String.format(MarkCommand.MESSAGE_MARK_PERSON_SUCCESS,
                Messages.format(markedPerson));

        assertCommandSuccess(markCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        MarkCommand markCommand = new MarkCommand(outOfBoundIndex, 1);

        assertCommandFailure(markCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_alreadyMarked_throwsCommandException() {
        Person personToMark = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        int weekToMark = 2;

        // First, mark the person for week 2
        MarkCommand firstMarkCommand = new MarkCommand(INDEX_FIRST_PERSON, weekToMark);
        try {
            firstMarkCommand.execute(model);
        } catch (Exception e) {
            throw new AssertionError("Execution of first mark command should not fail.", e);
        }

        // Try to mark the same week again
        MarkCommand secondMarkCommand = new MarkCommand(INDEX_FIRST_PERSON, weekToMark);
        assertCommandFailure(secondMarkCommand, model, MarkCommand.MESSAGE_ALREADY_MARKED);
    }

    @Test
    public void execute_validIndexFilteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person personToMark = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        int weekToMark = 3;
        MarkCommand markCommand = new MarkCommand(INDEX_FIRST_PERSON, weekToMark);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        Person markedPerson = createPersonWithMarkedWeek(personToMark, weekToMark);
        expectedModel.setPerson(personToMark, markedPerson);

        String expectedMessage = String.format(MarkCommand.MESSAGE_MARK_PERSON_SUCCESS,
                Messages.format(markedPerson));

        assertCommandSuccess(markCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        MarkCommand markCommand = new MarkCommand(outOfBoundIndex, 1);

        assertCommandFailure(markCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        MarkCommand markFirstCommand = new MarkCommand(INDEX_FIRST_PERSON, 1);
        MarkCommand markSecondCommand = new MarkCommand(INDEX_SECOND_PERSON, 1);
        MarkCommand markFirstCommandWeek2 = new MarkCommand(INDEX_FIRST_PERSON, 2);

        // same object -> returns true
        assertTrue(markFirstCommand.equals(markFirstCommand));

        // same values -> returns true
        MarkCommand markFirstCommandCopy = new MarkCommand(INDEX_FIRST_PERSON, 1);
        assertTrue(markFirstCommand.equals(markFirstCommandCopy));

        // different types -> returns false
        assertFalse(markFirstCommand.equals(1));

        // null -> returns false
        assertFalse(markFirstCommand.equals(null));

        // different index -> returns false
        assertFalse(markFirstCommand.equals(markSecondCommand));

        // different week -> returns false
        assertFalse(markFirstCommand.equals(markFirstCommandWeek2));
    }

    /**
     * Helper method to create a person with a specific week marked.
     *
     * @param person the original person
     * @param week the week to mark
     * @return a new person with the specified week marked
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
}
