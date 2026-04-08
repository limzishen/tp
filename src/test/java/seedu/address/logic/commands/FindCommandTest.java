package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_PERSONS_LISTED_OVERVIEW;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;
import static seedu.address.testutil.TypicalPersons.DANIEL;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.NameAndTutorialGroupPredicate;
import seedu.address.model.person.TutorialGroup;

/**
 * Contains integration tests (interaction with the Model) for {@code FindCommand}.
 */
public class FindCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void equals() {
        NameAndTutorialGroupPredicate firstPredicate =
                new NameAndTutorialGroupPredicate(Collections.singletonList("first"), List.of(), List.of(), List.of());
        NameAndTutorialGroupPredicate secondPredicate =
                new NameAndTutorialGroupPredicate(Collections.singletonList("second"), List.of(), List.of(), List.of());

        FindCommand findFirstCommand = new FindCommand(firstPredicate);
        FindCommand findSecondCommand = new FindCommand(secondPredicate);

        // same object -> returns true
        assertTrue(findFirstCommand.equals(findFirstCommand));

        // same values -> returns true
        FindCommand findFirstCommandCopy = new FindCommand(firstPredicate);
        assertTrue(findFirstCommand.equals(findFirstCommandCopy));

        // different types -> returns false
        assertFalse(findFirstCommand.equals(1));

        // null -> returns false
        assertFalse(findFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(findFirstCommand.equals(findSecondCommand));
    }

    @Test
    public void execute_zeroKeywords_noPersonFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 0);
        NameAndTutorialGroupPredicate predicate = new NameAndTutorialGroupPredicate(List.of(), List.of(), List.of(),
                List.of());
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), model.getFilteredPersonList());
    }

    @Test
    public void execute_nameWords_allMatch() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1);
        NameAndTutorialGroupPredicate predicate =
                new NameAndTutorialGroupPredicate(List.of("Daniel"), List.of(), List.of(),
                        List.of());
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.singletonList(DANIEL), model.getFilteredPersonList());
    }

    @Test
    public void execute_tutorialGroupOnly_multiplePersonsFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 2);
        NameAndTutorialGroupPredicate predicate =
                new NameAndTutorialGroupPredicate(List.of(), List.of(new TutorialGroup("T01")), List.of(), List.of());
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(ALICE, DANIEL), model.getFilteredPersonList());
    }

    @Test
    public void execute_nameKeywordsAndTutorialGroup_bothApply() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1);
        NameAndTutorialGroupPredicate predicate = new NameAndTutorialGroupPredicate(
                Arrays.asList("Alice", "Pau"), List.of(new TutorialGroup("T01")), List.of(), List.of());
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.singletonList(ALICE), model.getFilteredPersonList());
    }

    @Test
    public void execute_nameKeywordsAndTutorialGroup_noMatchWhenWrongGroup() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 0);
        NameAndTutorialGroupPredicate predicate = new NameAndTutorialGroupPredicate(
                List.of("Alice"), List.of(new TutorialGroup("T02")), List.of(), List.of());
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), model.getFilteredPersonList());
    }

    @Test
    public void execute_multipleNameKeywords_anyKeywordMatches() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1);
        NameAndTutorialGroupPredicate predicate = new NameAndTutorialGroupPredicate(
                List.of("Daniel", "Ong"), List.of(), List.of(), List.of());
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.singletonList(DANIEL), model.getFilteredPersonList());
    }

    @Test
    public void toStringMethod() {
        NameAndTutorialGroupPredicate predicate =
                new NameAndTutorialGroupPredicate(Arrays.asList("keyword"), List.of(), List.of(), List.of());
        FindCommand findCommand = new FindCommand(predicate);
        String expected = FindCommand.class.getCanonicalName() + "{predicate=" + predicate + "}";
        assertEquals(expected, findCommand.toString());
    }

    @Test
    public void execute_emailOnly_onePersonFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1);
        NameAndTutorialGroupPredicate predicate = new NameAndTutorialGroupPredicate(List.of(), List.of(),
                List.of("alice@u.nus.edu"), List.of());
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.singletonList(ALICE), model.getFilteredPersonList());
    }

    @Test
    public void execute_teleHandleOnly_onePersonFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1);
        NameAndTutorialGroupPredicate predicate = new NameAndTutorialGroupPredicate(List.of(), List.of(), List.of(),
                List.of("@benson_meier"));
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.singletonList(BENSON), model.getFilteredPersonList());
    }

    /**
     * Parses {@code userInput} into name keywords.
     */
    private List<String> prepareNameKeywords(String userInput) {
        return Arrays.asList(userInput.split("\\s+"));
    }

}
