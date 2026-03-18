package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.Assert;

public class TeleHandleTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new TeleHandle(null));
    }

    @Test
    public void constructor_invalidTeleHandle_throwsIllegalArgumentException() {
        Assert.assertThrows(IllegalArgumentException.class, () -> new TeleHandle(""));
        Assert.assertThrows(IllegalArgumentException.class, () -> new TeleHandle("notAHandle"));
    }

    @Test
    public void isValidTeleHandle() {
        // null
        assertFalse(TeleHandle.isValidTeleHandle(null));

        // invalid formats
        assertFalse(TeleHandle.isValidTeleHandle("")); // empty
        assertFalse(TeleHandle.isValidTeleHandle("notAHandle")); // missing '@'
        assertFalse(TeleHandle.isValidTeleHandle("@ab")); // too short
        assertFalse(TeleHandle.isValidTeleHandle("@has-hyphen")); // invalid char

        // valid
        assertTrue(TeleHandle.isValidTeleHandle("@abcde"));
        assertTrue(TeleHandle.isValidTeleHandle("@abcde_123"));
    }

    @Test
    public void equals() {
        TeleHandle teleHandle = new TeleHandle("@abcde");

        // same values -> returns true
        assertTrue(teleHandle.equals(new TeleHandle("@abcde")));

        // same object -> returns true
        assertTrue(teleHandle.equals(teleHandle));

        // null -> returns false
        assertFalse(teleHandle.equals(null));

        // different types -> returns false
        assertFalse(teleHandle.equals(5.0f));

        // different values -> returns false
        assertFalse(teleHandle.equals(new TeleHandle("@xyz12")));
    }

    @Test
    public void hashCode_sameValue_sameHashCode() {
        TeleHandle teleHandle1 = new TeleHandle("@abcde");
        TeleHandle teleHandle2 = new TeleHandle("@abcde");
        assertEquals(teleHandle1.hashCode(), teleHandle2.hashCode());
    }

    @Test
    public void toString_returnsValue() {
        TeleHandle teleHandle = new TeleHandle("@abcde");
        assertEquals("@abcde", teleHandle.toString());
    }
}

