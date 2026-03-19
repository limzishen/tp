package seedu.address.testutil;

import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_STUDENT_ID_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_STUDENT_ID_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TELE_HANDLE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TELE_HANDLE_BOB;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.model.AddressBook;
import seedu.address.model.person.Person;

/**
 * A utility class containing a list of {@code Person} objects to be used in tests.
 */
public class TypicalPersons {

    public static final Person ALICE = new PersonBuilder().withName("Alice Pauline")
            .withTeleHandle("@alice_pauline").withEmail("alice@u.nus.edu")
            .withPhone("94351253").withStudentId("A0123456A")
            .withTags("T01").build();
    public static final Person BENSON = new PersonBuilder().withName("Benson Meier")
            .withTeleHandle("@benson_meier")
            .withEmail("johnd@u.nus.edu").withPhone("98765432").withStudentId("A0123456B")
            .withTags("T02", "T01").build();
    public static final Person CARL = new PersonBuilder().withName("Carl Kurz").withPhone("95352563")
            .withEmail("heinz@u.nus.edu").withTeleHandle("@carl_kurz").withStudentId("A0123456C").build();
    public static final Person DANIEL = new PersonBuilder().withName("Daniel Meier").withPhone("87652533")
            .withEmail("cornelia@u.nus.edu").withTeleHandle("@daniel_meier").withStudentId("A0123456D")
            .withTags("T01").build();
    public static final Person ELLE = new PersonBuilder().withName("Elle Meyer").withPhone("94822240")
            .withEmail("werner@u.nus.edu").withTeleHandle("@elle_meyer").withStudentId("A0123456E").build();
    public static final Person FIONA = new PersonBuilder().withName("Fiona Kunz").withPhone("94824270")
            .withEmail("lydia@u.nus.edu").withTeleHandle("@fiona_kunz").withStudentId("A0123456F").build();
    public static final Person GEORGE = new PersonBuilder().withName("George Best").withPhone("94824420")
            .withEmail("anna@u.nus.edu").withTeleHandle("@george_best").withStudentId("A0123456G").build();

    // Manually added
    public static final Person HOON = new PersonBuilder().withName("Hoon Meier").withPhone("84824240")
            .withEmail("stefan@u.nus.edu").withTeleHandle("@hoon_meier").withStudentId("A0123456H").build();
    public static final Person IDA = new PersonBuilder().withName("Ida Mueller").withPhone("84821310")
            .withEmail("hans@u.nus.edu").withTeleHandle("@ida_mueller").withStudentId("A0123456I").build();

    // Manually added - Person's details found in {@code CommandTestUtil}
    public static final Person AMY = new PersonBuilder().withName(VALID_NAME_AMY).withPhone(VALID_PHONE_AMY)
            .withEmail(VALID_EMAIL_AMY).withTeleHandle(VALID_TELE_HANDLE_AMY).withStudentId(VALID_STUDENT_ID_AMY)
            .withTags(VALID_TAG_FRIEND).build();
    public static final Person BOB = new PersonBuilder().withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB)
            .withEmail(VALID_EMAIL_BOB).withTeleHandle(VALID_TELE_HANDLE_BOB).withStudentId(VALID_STUDENT_ID_BOB)
            .withTags(VALID_TAG_HUSBAND, VALID_TAG_FRIEND)
            .build();

    public static final String KEYWORD_MATCHING_MEIER = "Meier"; // A keyword that matches MEIER

    private TypicalPersons() {} // prevents instantiation

    /**
     * Returns an {@code AddressBook} with all the typical persons.
     */
    public static AddressBook getTypicalAddressBook() {
        AddressBook ab = new AddressBook();
        for (Person person : getTypicalPersons()) {
            ab.addPerson(person);
        }
        return ab;
    }

    public static List<Person> getTypicalPersons() {
        return new ArrayList<>(Arrays.asList(ALICE, BENSON, CARL, DANIEL, ELLE, FIONA, GEORGE));
    }
}
