package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's Telegram handle in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidTeleHandle(String)}.
 */
public class TeleHandle {

    public static final String MESSAGE_CONSTRAINTS =
            "Telegram handle should start with '@' and be 5 to 32 characters long (letters, numbers, underscores).";

    // Telegram usernames are 5-32 chars, letters/digits/underscore. We store with leading '@'.
    public static final String VALIDATION_REGEX = "^@[A-Za-z0-9_]{5,32}$";

    public final String value;

    /**
     * Constructs a {@code TeleHandle}.
     *
     * @param teleHandle A valid Telegram handle.
     */
    public TeleHandle(String teleHandle) {
        requireNonNull(teleHandle);
        checkArgument(isValidTeleHandle(teleHandle), MESSAGE_CONSTRAINTS);
        value = teleHandle;
    }

    public static boolean isValidTeleHandle(String test) {
        return test != null && test.matches(VALIDATION_REGEX);
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
        if (!(other instanceof TeleHandle)) {
            return false;
        }
        TeleHandle otherTeleHandle = (TeleHandle) other;
        return value.equals(otherTeleHandle.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}

