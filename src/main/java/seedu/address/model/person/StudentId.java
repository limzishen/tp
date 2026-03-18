package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's student ID in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidStudentId(String)}.
 * Format: Exactly 9 alphanumeric characters. Case-insensitive.
 */
public class StudentId {

    public static final String MESSAGE_CONSTRAINTS =
            "Invalid Student ID! Student ID should only contain 9 alphanumeric characters.";
    public static final String MESSAGE_CONSTRAINTS_LENGTH =
            "Invalid Student ID! Student ID should only contain 9 characters.";
    public static final String MESSAGE_CONSTRAINTS_ALPHANUMERIC =
            "Invalid Student ID! Student ID should only contain alphanumeric characters.";

    // Exactly 9 alphanumeric characters
    public static final String VALIDATION_REGEX = "^[A-Za-z0-9]{9}$";

    public final String value;

    /**
     * Constructs a {@code StudentId}.
     * Leading/trailing spaces are trimmed. Multiple internal spaces are collapsed to one.
     * Value is normalized to uppercase.
     *
     * @param studentId A valid student ID.
     */
    public StudentId(String studentId) {
        requireNonNull(studentId);
        String normalized = studentId.trim().replaceAll("\\s+", " ").replace(" ", "").toUpperCase();
        checkArgument(isValidStudentId(normalized), getConstraintMessage(normalized));
        value = normalized;
    }

    /**
     * Returns true if a given string is a valid student ID.
     */
    public static boolean isValidStudentId(String test) {
        if (test == null) {
            return false;
        }
        if (test.length() != 9) {
            return false;
        }
        return test.matches("^[A-Za-z0-9]+$");
    }

    private static String getConstraintMessage(String test) {
        if (test.length() != 9) {
            return MESSAGE_CONSTRAINTS_LENGTH;
        }
        return MESSAGE_CONSTRAINTS_ALPHANUMERIC;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof StudentId)) {
            return false;
        }
        StudentId otherStudentId = (StudentId) other;
        return value.equals(otherStudentId.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
