package seedu.address.model.tag;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class TagTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Tag(null));
    }

    @Test
    public void constructor_invalidTagName_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Tag(""));
        assertThrows(IllegalArgumentException.class, () -> new Tag("friend"));
        assertThrows(IllegalArgumentException.class, () -> new Tag("T1")); // only 1 digit
        assertThrows(IllegalArgumentException.class, () -> new Tag("T123")); // 3 digits
    }

    @Test
    public void isValidTagName() {
        // null tag name
        assertThrows(NullPointerException.class, () -> Tag.isValidTagName(null));

        // invalid tag names
        assertFalse(Tag.isValidTagName("")); // empty
        assertFalse(Tag.isValidTagName("friend")); // not T + 2 digits
        assertFalse(Tag.isValidTagName("T1")); // only 1 digit after T
        assertFalse(Tag.isValidTagName("T123")); // 3 digits after T
        assertFalse(Tag.isValidTagName("t01")); // lowercase t
        assertFalse(Tag.isValidTagName("A01")); // wrong starting letter
        assertFalse(Tag.isValidTagName("TAB")); // letters instead of digits
        assertFalse(Tag.isValidTagName("#friend")); // special characters

        // valid tag names (T + exactly 2 digits)
        assertTrue(Tag.isValidTagName("T01"));
        assertTrue(Tag.isValidTagName("T12"));
        assertTrue(Tag.isValidTagName("T00"));
        assertTrue(Tag.isValidTagName("T99"));
    }

}
