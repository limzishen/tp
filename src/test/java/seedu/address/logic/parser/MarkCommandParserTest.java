package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_WEEK;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.MarkCommand;

/**
 * As we are only doing white-box testing, our test cases do not cover path variations
 * outside of the MarkCommand code. For example, inputs "1 w/1" and "1 w/1 abc" take the
 * same path through the MarkCommand, and therefore we test only one of them.
 * The path variation for those two cases occur inside the ParserUtil, and
 * therefore should be covered by the ParserUtilTest.
 */
public class MarkCommandParserTest {

    private MarkCommandParser parser = new MarkCommandParser();

    @Test
    public void parse_validArgs_returnsMarkCommand() {
        assertParseSuccess(parser, "1 " + PREFIX_WEEK + "1", new MarkCommand(INDEX_FIRST_PERSON, 1));
        assertParseSuccess(parser, "2 " + PREFIX_WEEK + "5", new MarkCommand(INDEX_SECOND_PERSON, 5));
    }

    @Test
    public void parse_missingWeekPrefix_throwsParseException() {
        assertParseFailure(parser, "1", String.format(MESSAGE_INVALID_COMMAND_FORMAT, MarkCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_missingIndex_throwsParseException() {
        assertParseFailure(parser, " " + PREFIX_WEEK + "1",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, MarkCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidIndex_throwsParseException() {
        // negative index
        assertParseFailure(parser, "-5 " + PREFIX_WEEK + "1",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, MarkCommand.MESSAGE_USAGE));

        // zero index
        assertParseFailure(parser, "0 " + PREFIX_WEEK + "1",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, MarkCommand.MESSAGE_USAGE));

        // non-numeric index
        assertParseFailure(parser, "a " + PREFIX_WEEK + "1",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, MarkCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidWeek_throwsParseException() {
        // zero week
        assertParseFailure(parser, "1 " + PREFIX_WEEK + "0",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, MarkCommand.MESSAGE_USAGE));

        // negative week
        assertParseFailure(parser, "1 " + PREFIX_WEEK + "-1",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, MarkCommand.MESSAGE_USAGE));

        // non-numeric week
        assertParseFailure(parser, "1 " + PREFIX_WEEK + "abc",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, MarkCommand.MESSAGE_USAGE));
    }
}
