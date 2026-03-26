package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TUTORIAL_GROUP;
import static seedu.address.logic.parser.CliSyntax.PREFIX_WEEK;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.MarkCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.TutorialGroup;

/**
 * Parses input arguments and creates a new MarkCommand object
 */
public class MarkCommandParser implements Parser<MarkCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the MarkCommand
     * and returns a MarkCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public MarkCommand parse(String args) throws ParseException {
        requireNonNull(args);
        String trimmed = args.trim();
        // ArgumentTokenizer only treats a prefix as valid if it is preceded by whitespace; ensure
        // group form "t/T01 w/2" is recognized when arguments are trimmed after the command word.
        String tokenizeInput = trimmed.startsWith(PREFIX_TUTORIAL_GROUP.getPrefix())
                ? " " + trimmed
                : trimmed;

        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(tokenizeInput, PREFIX_WEEK, PREFIX_TUTORIAL_GROUP);
        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_WEEK, PREFIX_TUTORIAL_GROUP);

        if (!argMultimap.getValue(PREFIX_WEEK).isPresent()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, MarkCommand.MESSAGE_USAGE));
        }

        int week = parseWeek(argMultimap.getValue(PREFIX_WEEK).get());

        boolean hasGroup = argMultimap.getValue(PREFIX_TUTORIAL_GROUP).isPresent();
        String preamble = argMultimap.getPreamble().trim();

        if (hasGroup) {
            if (!preamble.isEmpty()) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, MarkCommand.MESSAGE_USAGE));
            }
            TutorialGroup group = ParserUtil.parseTutorialGroup(argMultimap.getValue(PREFIX_TUTORIAL_GROUP).get());
            return new MarkCommand(group, week);
        }

        if (preamble.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, MarkCommand.MESSAGE_USAGE));
        }

        try {
            Index index = ParserUtil.parseIndex(preamble);
            return new MarkCommand(index, week);
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, MarkCommand.MESSAGE_USAGE), pe);
        }
    }

    private static int parseWeek(String weekStr) throws ParseException {
        try {
            int week = Integer.parseInt(weekStr.trim());
            if (week <= 0) {
                throw new NumberFormatException();
            }
            return week;
        } catch (NumberFormatException e) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, MarkCommand.MESSAGE_USAGE), e);
        }
    }
}
