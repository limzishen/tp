package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.storage.CsvAddressBookStorage;

/**
 * Exports all student data to a CSV file.
 */
public class ExportCommand extends Command {

    public static final String COMMAND_WORD = "export";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Exports all student data to a CSV file named 'export.csv' in the current directory.\n"
            + "Example: " + COMMAND_WORD;

    public static final String MESSAGE_SUCCESS = "Exported %1$d student(s) to: %2$s";
    public static final String MESSAGE_FAILURE = "Failed to export data: %1$s";

    private static final Path DEFAULT_EXPORT_PATH = Paths.get("export.csv");

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        List<Person> allPersons = model.getAddressBook().getPersonList();

        try {
            new CsvAddressBookStorage(DEFAULT_EXPORT_PATH).writeCsv(allPersons);
        } catch (IOException e) {
            throw new CommandException(String.format(MESSAGE_FAILURE, e.getMessage()));
        }

        return new CommandResult(
                String.format(MESSAGE_SUCCESS, allPersons.size(), DEFAULT_EXPORT_PATH.toAbsolutePath()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this || other instanceof ExportCommand;
    }

    @Override
    public int hashCode() {
        return ExportCommand.class.hashCode();
    }
}
