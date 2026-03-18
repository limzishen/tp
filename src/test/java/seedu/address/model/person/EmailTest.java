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
        assertFalse(Email.isValidEmail("@u.nus.edu.sg")); // missing local part
        assertFalse(Email.isValidEmail("peterjackexample.sg")); // missing '@' symbol
        assertFalse(Email.isValidEmail("peterjack@")); // missing domain name

        // invalid parts (per simplified spec: one @, no spaces, chars before and after @)
        assertFalse(Email.isValidEmail("peter jack@example.com")); // spaces in local part
        assertFalse(Email.isValidEmail("peterjack@exam ple.com")); // spaces in domain name
        assertFalse(Email.isValidEmail(" peterjack@example.com")); // leading space
        assertFalse(Email.isValidEmail("peterjack@example.com ")); // trailing space
        assertFalse(Email.isValidEmail("peterjack@@example.com")); // double '@' symbol
        assertFalse(Email.isValidEmail("peter@jack@example.com")); // '@' symbol in local part
        assertFalse(Email.isValidEmail("peterjack@example@com")); // '@' symbol in domain name

        // valid email (simplified spec: one @, no spaces, at least one char before and after)
        assertTrue(Email.isValidEmail("PeterJack_1190@u.nus.edu.sg")); // underscore in local part
        assertTrue(Email.isValidEmail("PeterJack.1190@u.nus.edu.sg")); // period in local part
        assertTrue(Email.isValidEmail("PeterJack+1190@u.nus.edu.sg")); // '+' symbol in local part
        assertTrue(Email.isValidEmail("PeterJack-1190@u.nus.edu.sg")); // hyphen in local part
        assertTrue(Email.isValidEmail("a@u.nus.edu.sg")); // minimal
        assertTrue(Email.isValidEmail("test@u.nus.edu.sg")); // alphabets only
        assertTrue(Email.isValidEmail("123@u.nus.edu.sg")); // numeric local part and domain name
        assertTrue(Email.isValidEmail("a1+be.d@u.nus.edu.sg")); // mixture of alphanumeric and special characters
        assertTrue(Email.isValidEmail("if.you.dream.it_you.can.do.it@u.nus.edu.sg")); // long local part
    }

    @Test
    public void equals() {
        Email email = new Email("valid@u.nus.edu.sg");

        // same values -> returns true
        assertTrue(email.equals(new Email("valid@u.nus.edu.sg")));

        // same object -> returns true
        assertTrue(email.equals(email));

        // null -> returns false
        assertFalse(email.equals(null));

        // different types -> returns false
        assertFalse(email.equals(5.0f));

        // different values -> returns false
        assertFalse(email.equals(new Email("other.valid@u.nus.edu.sg")));
    }
}
