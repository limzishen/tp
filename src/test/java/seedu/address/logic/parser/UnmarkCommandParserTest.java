package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.getErrorMessageForDuplicatePrefixes;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TUTORIAL_GROUP;
import static seedu.address.logic.parser.CliSyntax.PREFIX_WEEK;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.UnmarkCommand;
import seedu.address.model.person.TutorialGroup;

public class UnmarkCommandParserTest {

    private UnmarkCommandParser parser = new UnmarkCommandParser();

    @Test
    public void parse_validArgs_returnsUnmarkCommand() {
        assertParseSuccess(parser, "1 " + PREFIX_WEEK + "1", new UnmarkCommand(INDEX_FIRST_PERSON, 1));
        assertParseSuccess(parser, "2 " + PREFIX_WEEK + "5", new UnmarkCommand(INDEX_SECOND_PERSON, 5));
    }

    @Test
    public void parse_validGroupArgs_returnsUnmarkCommand() {
        assertParseSuccess(parser, " " + PREFIX_TUTORIAL_GROUP + "T01 " + PREFIX_WEEK + "3",
                new UnmarkCommand(new TutorialGroup("T01"), 3));
    }

    @Test
    public void parse_validTutorialGroupNoLeadingSpace_returnsUnmarkCommand() {
        assertParseSuccess(parser, PREFIX_TUTORIAL_GROUP + "T02 " + PREFIX_WEEK + "1",
                new UnmarkCommand(new TutorialGroup("T02"), 1));
    }

    @Test
    public void parse_duplicateWeek_throwsParseException() {
        assertParseFailure(parser, PREFIX_TUTORIAL_GROUP + "T01 " + PREFIX_WEEK + "1 " + PREFIX_WEEK + "2",
                getErrorMessageForDuplicatePrefixes(PREFIX_WEEK));
    }

    @Test
    public void parse_duplicateTutorialGroup_throwsParseException() {
        assertParseFailure(parser, PREFIX_TUTORIAL_GROUP + "T01 " + PREFIX_TUTORIAL_GROUP + "T02 " + PREFIX_WEEK + "1",
                getErrorMessageForDuplicatePrefixes(PREFIX_TUTORIAL_GROUP));
    }

    @Test
    public void parse_missingWeekPrefix_throwsParseException() {
        assertParseFailure(parser, "1", String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnmarkCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_missingIndexAndTutorialGroup_throwsParseException() {
        assertParseFailure(parser, " " + PREFIX_WEEK + "1",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnmarkCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_bothIndexAndTutorialGroup_throwsParseException() {
        assertParseFailure(parser, "1 " + PREFIX_TUTORIAL_GROUP + "T01 " + PREFIX_WEEK + "1",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnmarkCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidIndex_throwsParseException() {
        assertParseFailure(parser, "-5 " + PREFIX_WEEK + "1",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnmarkCommand.MESSAGE_USAGE));
        assertParseFailure(parser, "0 " + PREFIX_WEEK + "1",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnmarkCommand.MESSAGE_USAGE));
        assertParseFailure(parser, "a " + PREFIX_WEEK + "1",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnmarkCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidWeek_throwsParseException() {
        assertParseFailure(parser, "1 " + PREFIX_WEEK + "0",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnmarkCommand.MESSAGE_USAGE));
        assertParseFailure(parser, "1 " + PREFIX_WEEK + "-1",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnmarkCommand.MESSAGE_USAGE));
        assertParseFailure(parser, "1 " + PREFIX_WEEK + "abc",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnmarkCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidTutorialGroup_throwsParseException() {
        assertParseFailure(parser, " " + PREFIX_TUTORIAL_GROUP + "T1 " + PREFIX_WEEK + "1",
                TutorialGroup.MESSAGE_CONSTRAINTS);
    }
}
