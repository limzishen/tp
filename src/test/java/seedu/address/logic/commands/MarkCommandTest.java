package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

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
        Person markedPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        String expectedMessage = String.format(MarkCommand.MESSAGE_ALREADY_MARKED,
                markedPerson.getName(), weekToMark);
        MarkCommand secondMarkCommand = new MarkCommand(INDEX_FIRST_PERSON, weekToMark);
        assertCommandFailure(secondMarkCommand, model, expectedMessage);
    }

    @Test
    public void execute_invalidWeek_throwsCommandException() {
        MarkCommand markCommand = new MarkCommand(INDEX_FIRST_PERSON, 0);
        assertCommandFailure(markCommand, model, MarkCommand.MESSAGE_INVALID_WEEK);

        markCommand = new MarkCommand(INDEX_FIRST_PERSON, Attendance.MAX_WEEKS + 1);
        assertCommandFailure(markCommand, model, MarkCommand.MESSAGE_INVALID_WEEK);
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
    public void execute_validMultipleIndices_success() {
        List<Person> initialList = model.getFilteredPersonList();
        Person firstPerson = initialList.get(INDEX_FIRST_PERSON.getZeroBased());
        Person secondPerson = initialList.get(INDEX_SECOND_PERSON.getZeroBased());
        int weekToMark = 6;

        List<Index> indices = List.of(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON);
        MarkCommand markCommand = new MarkCommand(indices, weekToMark);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(firstPerson, createPersonWithMarkedWeek(firstPerson, weekToMark));
        expectedModel.setPerson(secondPerson, createPersonWithMarkedWeek(secondPerson, weekToMark));

        String expectedMessage = String.format(MarkCommand.MESSAGE_MARK_MULTIPLE_SUCCESS,
                weekToMark, 2, 2, 0);

        assertCommandSuccess(markCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_multipleIndices_skipsAlreadyMarked() {
        int weekToMark = 7;
        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        model.setPerson(firstPerson, createPersonWithMarkedWeek(firstPerson, weekToMark));

        List<Index> indices = List.of(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON);
        MarkCommand markCommand = new MarkCommand(indices, weekToMark);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        Person secondPersonExpected = expectedModel.getFilteredPersonList().get(INDEX_SECOND_PERSON.getZeroBased());
        expectedModel.setPerson(secondPersonExpected, createPersonWithMarkedWeek(secondPersonExpected, weekToMark));

        String expectedMessage = String.format(MarkCommand.MESSAGE_MARK_MULTIPLE_SUCCESS,
                weekToMark, 2, 1, 1);

        assertCommandSuccess(markCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidMultipleIndex_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        List<Index> indices = List.of(INDEX_FIRST_PERSON, outOfBoundIndex);
        MarkCommand markCommand = new MarkCommand(indices, 1);

        assertCommandFailure(markCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_markTutorialGroup_success() {
        TutorialGroup t01 = new TutorialGroup("T01");
        int week = 5;
        MarkCommand markCommand = new MarkCommand(t01, week);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        for (Person p : expectedModel.getAddressBook().getPersonList()) {
            if (p.getTutorialGroup().equals(t01)) {
                expectedModel.setPerson(p, createPersonWithMarkedWeek(p, week));
            }
        }

        String expectedMessage = String.format(MarkCommand.MESSAGE_MARK_GROUP_SUCCESS,
                week, t01.value, 2, 0);

        assertCommandSuccess(markCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_markTutorialGroup_skipsAlreadyMarked() {
        TutorialGroup t01 = new TutorialGroup("T01");
        int week = 3;

        Person alice = model.getAddressBook().getPersonList().stream()
                .filter(p -> p.getName().fullName.equals("Alice Pauline"))
                .findFirst()
                .orElseThrow();
        model.setPerson(alice, createPersonWithMarkedWeek(alice, week));

        MarkCommand markCommand = new MarkCommand(t01, week);

        Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Person aliceExpected = expectedModel.getAddressBook().getPersonList().stream()
                .filter(p -> p.getName().fullName.equals("Alice Pauline"))
                .findFirst()
                .orElseThrow();
        Person danielExpected = expectedModel.getAddressBook().getPersonList().stream()
                .filter(p -> p.getName().fullName.equals("Daniel Meier"))
                .findFirst()
                .orElseThrow();
        expectedModel.setPerson(aliceExpected, createPersonWithMarkedWeek(aliceExpected, week));
        expectedModel.setPerson(danielExpected, createPersonWithMarkedWeek(danielExpected, week));

        String expectedMessage = String.format(MarkCommand.MESSAGE_MARK_GROUP_SUCCESS,
                week, t01.value, 1, 1);

        assertCommandSuccess(markCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_markTutorialGroup_allMarked() {
        TutorialGroup t01 = new TutorialGroup("T01");
        int week = 4;
        for (Person p : model.getAddressBook().getPersonList()) {
            if (p.getTutorialGroup().equals(t01)) {
                model.setPerson(p, createPersonWithMarkedWeek(p, week));
            }
        }

        MarkCommand markCommand = new MarkCommand(t01, week);
        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        String expectedMessage = String.format(MarkCommand.MESSAGE_MARK_GROUP_SUCCESS,
                week, t01.value, 0, 2);

        assertCommandSuccess(markCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_markTutorialGroup_emptyGroup() {
        MarkCommand markCommand = new MarkCommand(new TutorialGroup("T09"), 1);
        assertCommandFailure(markCommand, model,
                String.format(MarkCommand.MESSAGE_NO_STUDENTS_IN_GROUP, "T09"));
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

        MarkCommand markGroup = new MarkCommand(new TutorialGroup("T01"), 1);
        MarkCommand markGroupCopy = new MarkCommand(new TutorialGroup("T01"), 1);
        assertTrue(markGroup.equals(markGroupCopy));
        assertFalse(markFirstCommand.equals(markGroup));
        assertFalse(markGroup.equals(new MarkCommand(new TutorialGroup("T02"), 1)));

        // multiple indices
        MarkCommand markMultiple = new MarkCommand(List.of(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON), 1);
        MarkCommand markMultipleCopy = new MarkCommand(List.of(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON), 1);
        assertTrue(markMultiple.equals(markMultipleCopy));
        assertFalse(markFirstCommand.equals(markMultiple));
        assertFalse(markMultiple.equals(new MarkCommand(List.of(INDEX_FIRST_PERSON), 1)));
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
                person.getTeleHandle().orElse(null),
                person.getStudentId(),
                person.getTutorialGroup(),
                updatedAttendance
        );
    }
}
