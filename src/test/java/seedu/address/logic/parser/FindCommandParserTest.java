package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

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
        FindCommand expectedFindCommand =
                new FindCommand(new NameAndTutorialGroupPredicate(List.of("Alice", "Bob"),
                        List.of(), List.of(), List.of()));
        assertParseSuccess(parser, " n/Alice Bob", expectedFindCommand);

        FindCommand expectedFindCommandByTutorial =
                new FindCommand(new NameAndTutorialGroupPredicate(List.of(),
                        List.of(new TutorialGroup("T01")), List.of(), List.of()));
        assertParseSuccess(parser, " t/T01 ", expectedFindCommandByTutorial);

        FindCommand expectedFindCommandByEmail =
                new FindCommand(new NameAndTutorialGroupPredicate(List.of(), List.of(),
                        List.of("alice@u.nus.edu"), List.of()));
        assertParseSuccess(parser, " e/alice@u.nus.edu ", expectedFindCommandByEmail);

        FindCommand expectedFindCommandByTele =
                new FindCommand(new NameAndTutorialGroupPredicate(List.of(), List.of(),
                        List.of(), List.of("@alice")));
        assertParseSuccess(parser, " th/@alice ", expectedFindCommandByTele);
    }

    @Test
    public void parse_nameAndTutorialGroup_combined() {
        FindCommand expected = new FindCommand(new NameAndTutorialGroupPredicate(
                List.of("Alice"), List.of(new TutorialGroup("T01")), List.of(), List.of()));
        assertParseSuccess(parser, " n/Alice t/T01", expected);
    }

    @Test
    public void parse_multipleNamePrefixes_mergedAsKeywords() {
        FindCommand expected = new FindCommand(new NameAndTutorialGroupPredicate(
                List.of("alice", "bob"), List.of(), List.of(), List.of()));
        assertParseSuccess(parser, " n/alice n/bob", expected);
    }

    @Test
    public void parse_multiWordNameAndTutorial_combined() {
        FindCommand expected = new FindCommand(new NameAndTutorialGroupPredicate(
                List.of("John", "Do"), List.of(new TutorialGroup("T01")), List.of(), List.of()));
        assertParseSuccess(parser, " n/John Do t/T01", expected);
    }

}
