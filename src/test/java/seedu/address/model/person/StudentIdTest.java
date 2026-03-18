package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.Assert;

public class StudentIdTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new StudentId(null));
    }

    @Test
    public void constructor_invalidStudentId_throwsIllegalArgumentException() {
        Assert.assertThrows(IllegalArgumentException.class, () -> new StudentId(""));
        Assert.assertThrows(IllegalArgumentException.class, () -> new StudentId("invalid"));
        Assert.assertThrows(IllegalArgumentException.class, () -> new StudentId("A123")); // too short
        Assert.assertThrows(IllegalArgumentException.class, () -> new StudentId("A0123456")); // 8 chars
    }

    @Test
    public void isValidStudentId() {
        // null
        assertFalse(StudentId.isValidStudentId(null));

        // invalid formats
        assertFalse(StudentId.isValidStudentId("")); // empty
        assertFalse(StudentId.isValidStudentId("invalid")); // wrong format
        assertFalse(StudentId.isValidStudentId("A123")); // too short
        assertFalse(StudentId.isValidStudentId("A0123456")); // 8 chars

        // valid (9 alphanumeric chars)
        assertTrue(StudentId.isValidStudentId("A0123456X"));
        assertTrue(StudentId.isValidStudentId("10123456X")); // digits allowed
        assertTrue(StudentId.isValidStudentId("a0123456x")); // lowercase
        assertTrue(StudentId.isValidStudentId("B1234567Y"));
        assertTrue(StudentId.isValidStudentId("Z9999999A"));
    }

    @Test
    public void constructor_normalizesToUppercase() {
        StudentId lowerCase = new StudentId("a0123456x");
        assertEquals("A0123456X", lowerCase.value);
    }

    @Test
    public void equals() {
        StudentId studentId = new StudentId("A0123456X");

        // same values -> returns true
        assertTrue(studentId.equals(new StudentId("A0123456X")));

        // same object -> returns true
        assertTrue(studentId.equals(studentId));

        // null -> returns false
        assertFalse(studentId.equals(null));

        // different types -> returns false
        assertFalse(studentId.equals(5.0f));

        // different values -> returns false
        assertFalse(studentId.equals(new StudentId("B0123456Y")));
    }

    @Test
    public void hashCode_sameValue_sameHashCode() {
        StudentId studentId1 = new StudentId("A0123456X");
        StudentId studentId2 = new StudentId("A0123456X");
        assertEquals(studentId1.hashCode(), studentId2.hashCode());
    }

    @Test
    public void toString_returnsValue() {
        StudentId studentId = new StudentId("A0123456X");
        assertEquals("A0123456X", studentId.toString());
    }
}
