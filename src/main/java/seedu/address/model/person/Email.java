package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's email in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidEmail(String)}
 * Format: local-part@u.nus.edu. Local part must contain only alphanumeric characters and/or
 * special characters (+_.-), and the domain must be @u.nus.edu.
 */
public class Email {

    public static final String SPECIAL_CHARACTERS = "+_.-";
    public static final int MAX_LOCAL_PART_LENGTH = 50;
    // alphanumeric and special characters
    public static final String LOCAL_PART_REGEX =
            "^" + "[^\\W_]+" + "([" + SPECIAL_CHARACTERS + "]" + "[^\\W_]+" + ")*";
    public static final String DOMAIN_REGEX = "@u\\.nus\\.edu$";
    public static final String VALIDATION_REGEX = LOCAL_PART_REGEX + DOMAIN_REGEX;
    public static final String MESSAGE_CONSTRAINTS = "Emails should be of the format local-part@u.nus.edu "
        + "and adhere to the following constraints:\n"
        + "1. The local-part should only contain alphanumeric characters and these special characters, excluding "
        + "the parentheses, (" + SPECIAL_CHARACTERS + ").\n"
        + "2. Each special character must be surrounded by alphanumeric characters "
        + "(i.e. the local-part cannot start or end with a special character, and cannot contain consecutive special "
        + "characters).\n"
        + "3. The local-part must be at most " + MAX_LOCAL_PART_LENGTH + " characters long.\n"
        + "4. The domain must be exactly u.nus.edu.";

    public final String value;

    /**
     * Constructs an {@code Email}.
     * Leading and trailing spaces are trimmed.
     *
     * @param email A valid email address.
     */
    public Email(String email) {
        requireNonNull(email);
        String trimmedEmail = email.trim();
        checkArgument(isValidEmail(trimmedEmail), MESSAGE_CONSTRAINTS);
        value = trimmedEmail;
    }

    /**
     * Returns if a given string is a valid email.
     * Must match local-part@u.nus.edu format.
     */
    public static boolean isValidEmail(String test) {
        if (test == null) {
            throw new NullPointerException();
        }
        if (!test.matches(VALIDATION_REGEX)) {
            return false;
        }
        int atIndex = test.indexOf('@');
        return atIndex <= MAX_LOCAL_PART_LENGTH;
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

        // instanceof handles nulls
        if (!(other instanceof Email)) {
            return false;
        }

        Email otherEmail = (Email) other;
        return value.equals(otherEmail.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
