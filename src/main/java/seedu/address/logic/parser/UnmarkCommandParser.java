package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TUTORIAL_GROUP;
import static seedu.address.logic.parser.CliSyntax.PREFIX_WEEK;

import java.util.Optional;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.UnmarkCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.TutorialGroup;

/**
 * Parses input arguments and creates a new UnmarkCommand object.
 */
public class UnmarkCommandParser implements Parser<UnmarkCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the UnmarkCommand
     * and returns an UnmarkCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public UnmarkCommand parse(String args) throws ParseException {
        requireNonNull(args);
        String trimmed = args.trim();
        String tokenizeInput = trimmed.startsWith(PREFIX_TUTORIAL_GROUP.getPrefix())
                ? " " + trimmed
                : trimmed;
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(tokenizeInput, PREFIX_WEEK, PREFIX_TUTORIAL_GROUP);
        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_WEEK, PREFIX_TUTORIAL_GROUP);

        Optional<String> weekValue = argMultimap.getValue(PREFIX_WEEK);
        if (weekValue.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnmarkCommand.MESSAGE_USAGE));
        }

        int week = parseWeek(weekValue.get());

        Optional<String> tutorialGroupValue = argMultimap.getValue(PREFIX_TUTORIAL_GROUP);
        String preamble = argMultimap.getPreamble();

        boolean hasIndex = !preamble.trim().isEmpty();
        boolean hasTutorialGroup = tutorialGroupValue.isPresent();

        if (hasIndex && hasTutorialGroup) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnmarkCommand.MESSAGE_USAGE));
        }

        if (hasTutorialGroup) {
            TutorialGroup tutorialGroup = ParserUtil.parseTutorialGroup(tutorialGroupValue.get().trim());
            return new UnmarkCommand(tutorialGroup, week);
        }

        if (!hasIndex) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnmarkCommand.MESSAGE_USAGE));
        }

        try {
            Index index = ParserUtil.parseIndex(preamble);
            return new UnmarkCommand(index, week);
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnmarkCommand.MESSAGE_USAGE), pe);
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
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnmarkCommand.MESSAGE_USAGE), e);
        }
    }
}
