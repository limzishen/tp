package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TUTORIAL_GROUP;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.NameAndTutorialGroupPredicate;
import seedu.address.model.person.TutorialGroup;

/**
 * Parses input arguments and creates a new FindCommand object
 */
public class FindCommandParser implements Parser<FindCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FindCommand
     * and returns a FindCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public FindCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_TUTORIAL_GROUP);

        if (!argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        List<String> nameKeywords = parseNameKeywords(argMultimap.getAllValues(PREFIX_NAME));
        List<TutorialGroup> tutorialGroups = parseTutorialGroups(argMultimap.getAllValues(PREFIX_TUTORIAL_GROUP));

        if (nameKeywords.isEmpty() && tutorialGroups.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        return new FindCommand(new NameAndTutorialGroupPredicate(nameKeywords, tutorialGroups));
    }

    private List<String> parseNameKeywords(List<String> rawNameValues) throws ParseException {
        List<String> keywords = new ArrayList<>();
        for (String rawValue : rawNameValues) {
            String trimmedValue = rawValue.trim();
            if (trimmedValue.isEmpty()) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
            }
            keywords.addAll(Arrays.asList(trimmedValue.split("\\s+")));
        }
        return keywords.stream().filter(value -> !value.isBlank()).collect(Collectors.toList());
    }

    private List<TutorialGroup> parseTutorialGroups(List<String> rawTutorialValues) throws ParseException {
        List<TutorialGroup> tutorialGroups = new ArrayList<>();
        for (String rawValue : rawTutorialValues) {
            String trimmedValue = rawValue.trim();
            if (trimmedValue.isEmpty()) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
            }
            tutorialGroups.add(ParserUtil.parseTutorialGroup(trimmedValue));
        }
        return tutorialGroups;
    }
}
