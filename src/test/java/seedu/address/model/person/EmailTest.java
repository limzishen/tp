package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class EmailTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Email(null));
    }

    @Test
    public void constructor_invalidEmail_throwsIllegalArgumentException() {
        String invalidEmail = "";
        assertThrows(IllegalArgumentException.class, () -> new Email(invalidEmail));
    }

    @Test
    public void isValidEmail() {
        // null email
        assertThrows(NullPointerException.class, () -> Email.isValidEmail(null));

        // blank email
        assertFalse(Email.isValidEmail("")); // empty string
        assertFalse(Email.isValidEmail(" ")); // spaces only

        // missing parts
        assertFalse(Email.isValidEmail("@u.nus.edu")); // missing local part
        assertFalse(Email.isValidEmail("peterjackexample.sg")); // missing '@' symbol
        assertFalse(Email.isValidEmail("peterjack@")); // missing domain name

        // invalid parts
        assertFalse(Email.isValidEmail("peter jack@u.nus.edu")); // spaces in local part
        assertFalse(Email.isValidEmail(" peterjack@u.nus.edu")); // leading space
        assertFalse(Email.isValidEmail("peterjack@u.nus.edu ")); // trailing space
        assertFalse(Email.isValidEmail("peterjack@@u.nus.edu")); // double '@' symbol
        assertFalse(Email.isValidEmail("peter@jack@u.nus.edu")); // '@' symbol in local part
        assertFalse(Email.isValidEmail("peterjack@example.com")); // wrong domain
        assertFalse(Email.isValidEmail("peterjack@u.nus.edu.sg")); // wrong domain (.sg suffix)

        // valid email
        assertTrue(Email.isValidEmail("PeterJack_1190@u.nus.edu")); // underscore in local part
        assertTrue(Email.isValidEmail("PeterJack.1190@u.nus.edu")); // period in local part
        assertTrue(Email.isValidEmail("PeterJack+1190@u.nus.edu")); // '+' symbol in local part
        assertTrue(Email.isValidEmail("PeterJack-1190@u.nus.edu")); // hyphen in local part
        assertTrue(Email.isValidEmail("a@u.nus.edu")); // minimal
        assertTrue(Email.isValidEmail("test@u.nus.edu")); // alphabets only
        assertTrue(Email.isValidEmail("123@u.nus.edu")); // numeric local part
        assertTrue(Email.isValidEmail("a1+be.d@u.nus.edu")); // mixture of alphanumeric and special characters
        assertTrue(Email.isValidEmail("if.you.dream.it_you.can.do.it@u.nus.edu")); // long local part
    }

    @Test
    public void equals() {
        Email email = new Email("valid@u.nus.edu");

        // same values -> returns true
        assertTrue(email.equals(new Email("valid@u.nus.edu")));

        // same object -> returns true
        assertTrue(email.equals(email));

        // null -> returns false
        assertFalse(email.equals(null));

        // different types -> returns false
        assertFalse(email.equals(5.0f));

        // different values -> returns false
        assertFalse(email.equals(new Email("other.valid@u.nus.edu")));
    }
}
