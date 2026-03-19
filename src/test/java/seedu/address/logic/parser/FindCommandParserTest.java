package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FindCommand;
import seedu.address.model.person.NameAndTutorialGroupPredicate;
import seedu.address.model.person.TutorialGroup;

public class FindCommandParserTest {

    private FindCommandParser parser = new FindCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ", String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_noPrefixes_throwsParseException() {
        assertParseFailure(parser, " Alice Bob ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_emptyPrefixValue_throwsParseException() {
        assertParseFailure(parser, " n/ ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidTutorialGroup_throwsParseException() {
        assertParseFailure(parser, " t/T1 ", TutorialGroup.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_validArgs_returnsFindCommand() {
        // no leading and trailing whitespaces
        FindCommand expectedFindCommand =
                new FindCommand(new NameAndTutorialGroupPredicate(Arrays.asList("Alice", "Bob"), List.of()));
        assertParseSuccess(parser, " n/Alice Bob", expectedFindCommand);

        // multiple whitespaces between keywords
        assertParseSuccess(parser, " \n n/Alice \n \t Bob  \t", expectedFindCommand);

        // tutorial group only
        FindCommand expectedFindCommandByTutorial =
                new FindCommand(new NameAndTutorialGroupPredicate(List.of(), List.of(new TutorialGroup("T01"))));
        assertParseSuccess(parser, " t/T01 ", expectedFindCommandByTutorial);
    }

}
