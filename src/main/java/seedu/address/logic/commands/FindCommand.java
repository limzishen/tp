package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.person.NameAndTutorialGroupPredicate;

/**
 * Finds and lists all persons in address book whose name contains any of the argument keywords,
 * and/or whose tutorial group, email, or telegram handle matches specified filters.
 * Keyword matching for names is case insensitive.
 */
public class FindCommand extends Command {

    public static final String COMMAND_WORD = "find";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all persons whose names contain any of "
            + "the specified keywords (case-insensitive) and/or match tutorial group, email, or telegram handle, and "
            + "displays them as a list with index numbers.\n"
            + "Parameters: [n/NAME_KEYWORD [MORE_KEYWORDS]...] [t/TUTORIAL_GROUP]... [e/EMAIL]... [th/TELE_HANDLE]...\n"
            + "At least one of n/, t/, e/, or th/ must be present.\n"
            + "Example: " + COMMAND_WORD + " n/alice t/T01 e/alice@u.nus.edu th/@alice";

    private final NameAndTutorialGroupPredicate predicate;

    public FindCommand(NameAndTutorialGroupPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredPersonList(predicate);
        return new CommandResult(
                String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW, model.getFilteredPersonList().size()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof FindCommand)) {
            return false;
        }

        FindCommand otherFindCommand = (FindCommand) other;
        return predicate.equals(otherFindCommand.predicate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("predicate", predicate)
                .toString();
    }
}
