package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_STUDENT_ID_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TELE_HANDLE_BOB;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BOB;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

public class PersonTest {

    @Test
    public void asObservableList_modifyList_throwsUnsupportedOperationException() {
        Person person = new PersonBuilder().build();
        assertThrows(UnsupportedOperationException.class, () -> person.getTags().remove(0));
    }

    @Test
    public void isSamePerson() {
        // same object -> returns true
        assertTrue(ALICE.isSamePerson(ALICE));

        // null -> returns false
        assertFalse(ALICE.isSamePerson(null));

        // same studentId, all other attributes different -> returns true
        Person editedAlice = new PersonBuilder(ALICE).withPhone(VALID_PHONE_BOB).withEmail(VALID_EMAIL_BOB)
                .withTeleHandle(VALID_TELE_HANDLE_BOB).withTags(VALID_TAG_HUSBAND).build();
        assertTrue(ALICE.isSamePerson(editedAlice));

        // different studentId, all other attributes same -> returns false
        editedAlice = new PersonBuilder(ALICE).withStudentId(VALID_STUDENT_ID_BOB).build();
        assertFalse(ALICE.isSamePerson(editedAlice));

        // different studentId, same name -> returns false
        Person editedBob = new PersonBuilder(BOB).withStudentId("A0123456Z").build();
        assertFalse(BOB.isSamePerson(editedBob));

        // same studentId, name has trailing spaces -> returns true (studentId is identity)
        String nameWithTrailingSpaces = VALID_NAME_BOB + " ";
        editedBob = new PersonBuilder(BOB).withName(nameWithTrailingSpaces).build();
        assertTrue(BOB.isSamePerson(editedBob));
    }

    @Test
    public void equals() {
        // same values -> returns true
        Person aliceCopy = new PersonBuilder(ALICE).build();
        assertTrue(ALICE.equals(aliceCopy));

        // same object -> returns true
        assertTrue(ALICE.equals(ALICE));

        // null -> returns false
        assertFalse(ALICE.equals(null));

        // different type -> returns false
        assertFalse(ALICE.equals(5));

        // different person -> returns false
        assertFalse(ALICE.equals(BOB));

        // different name -> returns false
        Person editedAlice = new PersonBuilder(ALICE).withName(VALID_NAME_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different phone -> returns false
        editedAlice = new PersonBuilder(ALICE).withPhone(VALID_PHONE_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different email -> returns false
        editedAlice = new PersonBuilder(ALICE).withEmail(VALID_EMAIL_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different teleHandle -> returns false
        editedAlice = new PersonBuilder(ALICE).withTeleHandle(VALID_TELE_HANDLE_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different studentId -> returns false
        editedAlice = new PersonBuilder(ALICE).withStudentId(VALID_STUDENT_ID_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different tags -> returns false
        editedAlice = new PersonBuilder(ALICE).withTags(VALID_TAG_HUSBAND).build();
        assertFalse(ALICE.equals(editedAlice));
    }

    @Test
    public void equals_differentAttendance_returnsFalse() {
        // same person with different attendance -> returns false
        Attendance attendanceWithMarkedWeek = new Attendance().createCopyWithMarkedWeek(1);
        Person aliceWithMarkedAttendance = new PersonBuilder(ALICE).withAttendance(attendanceWithMarkedWeek).build();
        assertFalse(ALICE.equals(aliceWithMarkedAttendance));
    }

    @Test
    public void equals_sameAttendance_returnsTrue() {
        // same person with same attendance (both empty) -> returns true
        Person aliceCopy = new PersonBuilder(ALICE).withAttendance(ALICE.getAttendance()).build();
        assertTrue(ALICE.equals(aliceCopy));
    }

    @Test
    public void getAttendance() {
        Person person = new PersonBuilder().build();
        assertFalse(person.getAttendance().isMarked(1));
        assertFalse(person.getAttendance().isMarked(5));
        assertFalse(person.getAttendance().isMarked(13));
    }

    @Test
    public void constructorWithAttendance_createsPersonWithProvidedAttendance() {
        // Create attendance with week 3 marked
        Attendance customAttendance = new Attendance().createCopyWithMarkedWeek(3);
        Person person = new PersonBuilder(ALICE).withAttendance(customAttendance).build();

        // Verify the person has the same attendance
        assertTrue(person.getAttendance().isMarked(3));
        assertFalse(person.getAttendance().isMarked(1));
        assertFalse(person.getAttendance().isMarked(2));
    }

    @Test
    public void constructorWithAttendance_multipleWeeksMarked() {
        // Create attendance with multiple weeks marked
        Attendance customAttendance = new Attendance()
                .createCopyWithMarkedWeek(1)
                .createCopyWithMarkedWeek(5)
                .createCopyWithMarkedWeek(13);
        Person person = new PersonBuilder(ALICE).withAttendance(customAttendance).build();

        // Verify the person has the correct attendance
        assertTrue(person.getAttendance().isMarked(1));
        assertTrue(person.getAttendance().isMarked(5));
        assertTrue(person.getAttendance().isMarked(13));
        assertFalse(person.getAttendance().isMarked(2));
        assertFalse(person.getAttendance().isMarked(4));
    }

    @Test
    public void toStringMethod() {
        String expected = Person.class.getCanonicalName() + "{name=" + ALICE.getName() + ", studentId="
                + ALICE.getStudentId() + ", email=" + ALICE.getEmail() + ", phone=" + ALICE.getPhone()
                + ", teleHandle=" + ALICE.getTeleHandle() + ", tags=" + ALICE.getTags()
                + ", attendance=" + ALICE.getAttendance()
                + "}";
        assertEquals(expected, ALICE.toString());
    }
}
