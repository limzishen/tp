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
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_WEEK, PREFIX_TUTORIAL_GROUP);

        Optional<String> weekValue = argMultimap.getValue(PREFIX_WEEK);
        if (weekValue.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnmarkCommand.MESSAGE_USAGE));
        }

        int week;
        try {
            week = Integer.parseInt(weekValue.get().trim());
            if (week <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnmarkCommand.MESSAGE_USAGE), e);
        }

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

        Index index;
        try {
            index = ParserUtil.parseIndex(preamble);
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnmarkCommand.MESSAGE_USAGE), pe);
        }

        return new UnmarkCommand(index, week);
    }
}
