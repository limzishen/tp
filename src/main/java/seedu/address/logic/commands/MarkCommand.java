package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TUTORIAL_GROUP;
import static seedu.address.logic.parser.CliSyntax.PREFIX_WEEK;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Attendance;
import seedu.address.model.person.Person;
import seedu.address.model.person.TutorialGroup;

/**
 * Marks the attendance of an existing person in the address book, or marks all students
 * in a tutorial group for a given week.
 */
public class MarkCommand extends Command {

    public static final String COMMAND_WORD = "mark";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Marks attendance for one or more persons by list index, or for everyone in a tutorial group.\n"
            + "Parameters (single): INDEX (positive integer) "
            + PREFIX_WEEK + "WEEK (positive integer)\n"
            + "Parameters (multiple): INDEX1 INDEX2 ... (positive integers) "
            + PREFIX_WEEK + "WEEK (positive integer)\n"
            + "Parameters (group): " + PREFIX_TUTORIAL_GROUP + "TUTORIAL_GROUP "
            + PREFIX_WEEK + "WEEK (positive integer)\n"
            + "Example (single): " + COMMAND_WORD + " 1 " + PREFIX_WEEK + "2\n"
            + "Example (multiple): " + COMMAND_WORD + " 1 2 3 " + PREFIX_WEEK + "2\n"
            + "Example (group): " + COMMAND_WORD + " " + PREFIX_TUTORIAL_GROUP + "T02 " + PREFIX_WEEK + "2";

    public static final String MESSAGE_MARK_PERSON_SUCCESS = "Marked Person: %1$s";
    public static final String MESSAGE_ALREADY_MARKED = "%1$s has already been marked"
                                + " as attended for week %2$s.";
    public static final String MESSAGE_MARK_GROUP_SUCCESS =
            "Marked week %1$d for tutorial group %2$s: %3$d student(s) updated, %4$d already recorded for this week.";
    public static final String MESSAGE_MARK_MULTIPLE_SUCCESS =
            "Marked week %1$d for %2$d selected student(s): %3$d updated, %4$d already recorded for this week.";
    public static final String MESSAGE_NO_STUDENTS_IN_GROUP = "No students found in tutorial group %1$s.";
    public static final String MESSAGE_INVALID_WEEK = "Week must be a positive integer between 1 to 13.";

    private final Optional<Index> index;
    private final Optional<List<Index>> indices;
    private final Optional<TutorialGroup> tutorialGroup;
    private final int week;

    /**
     * @param index of the person in the filtered person list to mark
     * @param week of the attendance to mark
     */
    public MarkCommand(Index index, int week) {
        requireNonNull(index);
        this.index = Optional.of(index);
        this.indices = Optional.empty();
        this.tutorialGroup = Optional.empty();
        this.week = week;
    }

    /**
     * @param indices list of indices of persons in the filtered person list to mark
     * @param week of the attendance to mark
     */
    public MarkCommand(List<Index> indices, int week) {
        requireNonNull(indices);
        this.index = Optional.empty();
        this.indices = Optional.of(indices);
        this.tutorialGroup = Optional.empty();
        this.week = week;
    }

    /**
     * @param tutorialGroup tutorial group whose members are all marked for {@code week}
     * @param week of the attendance to mark
     */
    public MarkCommand(TutorialGroup tutorialGroup, int week) {
        requireNonNull(tutorialGroup);
        this.index = Optional.empty();
        this.indices = Optional.empty();
        this.tutorialGroup = Optional.of(tutorialGroup);
        this.week = week;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        if (tutorialGroup.isPresent()) {
            return executeMarkTutorialGroup(model, tutorialGroup.get(), week);
        }
        if (indices.isPresent()) {
            return executeMarkMultipleIndices(model, indices.get(), week);
        }
        return executeMarkIndex(model, index.get(), week);
    }

    private CommandResult executeMarkIndex(Model model, Index index, int week) throws CommandException {
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        if (week < 1 || week > Attendance.MAX_WEEKS) {
            throw new CommandException(MESSAGE_INVALID_WEEK);
        }

        Person personToMark = lastShownList.get(index.getZeroBased());

        if (personToMark.getAttendance().isMarked(week)) {
            throw new CommandException(String.format(MESSAGE_ALREADY_MARKED, personToMark.getName(), week));
        }

        Attendance updatedAttendance = personToMark.getAttendance().createCopyWithMarkedWeek(week);
        Person markedPerson = new Person(
                personToMark.getName(),
                personToMark.getPhone(),
                personToMark.getEmail(),
                personToMark.getTeleHandle().orElse(null),
                personToMark.getStudentId(),
                personToMark.getTutorialGroup(),
                updatedAttendance
        );

        model.setPerson(personToMark, markedPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        return new CommandResult(String.format(MESSAGE_MARK_PERSON_SUCCESS, Messages.format(markedPerson)));
    }

    private CommandResult executeMarkMultipleIndices(Model model, List<Index> indices, int week)
            throws CommandException {
        List<Person> lastShownList = model.getFilteredPersonList();

        for (Index idx : indices) {
            if (idx.getZeroBased() >= lastShownList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
            }
        }

        if (week < 1 || week > Attendance.MAX_WEEKS) {
            throw new CommandException(MESSAGE_INVALID_WEEK);
        }

        int updated = 0;
        int skipped = 0;
        Set<Integer> processedIndices = new HashSet<>();

        for (Index idx : indices) {
            int zeroBased = idx.getZeroBased();
            if (processedIndices.contains(zeroBased)) {
                skipped++;
                continue;
            }
            processedIndices.add(zeroBased);

            Person personToMark = model.getFilteredPersonList().get(zeroBased);
            if (personToMark.getAttendance().isMarked(week)) {
                skipped++;
                continue;
            }

            Attendance updatedAttendance = personToMark.getAttendance().createCopyWithMarkedWeek(week);
            Person markedPerson = new Person(
                    personToMark.getName(),
                    personToMark.getPhone(),
                    personToMark.getEmail(),
                    personToMark.getTeleHandle().orElse(null),
                    personToMark.getStudentId(),
                    personToMark.getTutorialGroup(),
                    updatedAttendance
            );
            model.setPerson(personToMark, markedPerson);
            updated++;
        }

        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(
                String.format(MESSAGE_MARK_MULTIPLE_SUCCESS, week, updated + skipped, updated, skipped));
    }

    private CommandResult executeMarkTutorialGroup(Model model, TutorialGroup group, int week)
            throws CommandException {
        List<Person> inGroup = model.getAddressBook().getPersonList().stream()
                .filter(p -> p.getTutorialGroup().equals(group))
                .toList();

        if (inGroup.isEmpty()) {
            throw new CommandException(String.format(MESSAGE_NO_STUDENTS_IN_GROUP, group.value));
        }

        int updated = 0;
        int skipped = 0;
        for (Person person : inGroup) {
            if (person.getAttendance().isMarked(week)) {
                skipped++;
                continue;
            }
            Attendance updatedAttendance = person.getAttendance().createCopyWithMarkedWeek(week);
            Person markedPerson = new Person(
                    person.getName(),
                    person.getPhone(),
                    person.getEmail(),
                    person.getTeleHandle().orElse(null),
                    person.getStudentId(),
                    person.getTutorialGroup(),
                    updatedAttendance
            );
            model.setPerson(person, markedPerson);
            updated++;
        }

        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(
                String.format(MESSAGE_MARK_GROUP_SUCCESS, week, group.value, updated, skipped));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof MarkCommand)) {
            return false;
        }

        MarkCommand otherMarkCommand = (MarkCommand) other;
        return index.equals(otherMarkCommand.index)
                && indices.equals(otherMarkCommand.indices)
                && tutorialGroup.equals(otherMarkCommand.tutorialGroup)
                && week == otherMarkCommand.week;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("indices", indices)
                .add("tutorialGroup", tutorialGroup)
                .add("week", week)
                .toString();
    }
}
