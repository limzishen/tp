package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

public class NameAndTutorialGroupPredicateTest {

    @Test
    public void test_nameKeywords_matchesName() {
        Person person = new PersonBuilder().withName("Alice Pauline").withTutorialGroup("T01").build();
        NameAndTutorialGroupPredicate predicate =
                new NameAndTutorialGroupPredicate(Arrays.asList("Alice", "Bob"), List.of(), List.of(), List.of());

        assertTrue(predicate.test(person));
    }

    @Test
    public void test_tutorialGroup_matchesTutorialGroup() {
        Person person = new PersonBuilder().withName("Alice Pauline").withTutorialGroup("T01").build();
        NameAndTutorialGroupPredicate predicate =
                new NameAndTutorialGroupPredicate(List.of(), List.of(new TutorialGroup("T01")), List.of(), List.of());

        assertTrue(predicate.test(person));
    }

    @Test
    public void test_bothNameAndTutorialGroup_mustMatchBoth() {
        Person person = new PersonBuilder().withName("Alice Pauline").withTutorialGroup("T01").build();
        NameAndTutorialGroupPredicate predicate =
                new NameAndTutorialGroupPredicate(List.of("Alice"), List.of(new TutorialGroup("T01")), List.of(),
                        List.of());

        assertTrue(predicate.test(person));

        NameAndTutorialGroupPredicate nameMismatch =
                new NameAndTutorialGroupPredicate(List.of("Bob"), List.of(new TutorialGroup("T01")), List.of(),
                        List.of());
        assertFalse(nameMismatch.test(person));

        NameAndTutorialGroupPredicate tutorialMismatch =
                new NameAndTutorialGroupPredicate(List.of("Alice"), List.of(new TutorialGroup("T02")), List.of(),
                        List.of());
        assertFalse(tutorialMismatch.test(person));
    }

    @Test
    public void test_email_matchesEmail() {
        Person person = new PersonBuilder().withName("Alice Pauline").withEmail("alice@u.nus.edu")
                .withTutorialGroup("T01").build();
        NameAndTutorialGroupPredicate predicate =
                new NameAndTutorialGroupPredicate(List.of(), List.of(),
                        List.of(new Email("alice@u.nus.edu")), List.of());

        assertTrue(predicate.test(person));
    }

    @Test
    public void test_teleHandle_matchesTeleHandle() {
        Person person = new PersonBuilder().withName("Alice Pauline").withTeleHandle("@alice_pauline")
                .withTutorialGroup("T01").build();
        NameAndTutorialGroupPredicate predicate =
                new NameAndTutorialGroupPredicate(List.of(), List.of(), List.of(),
                        List.of(new TeleHandle("@alice_pauline")));

        assertTrue(predicate.test(person));
    }

    @Test
    public void test_emptyCriteria_returnsFalse() {
        Person person = new PersonBuilder().withName("Alice Pauline").withTutorialGroup("T01").build();
        NameAndTutorialGroupPredicate predicate =
                new NameAndTutorialGroupPredicate(List.of(), List.of(), List.of(), List.of());

        assertFalse(predicate.test(person));
    }

    @Test
    public void equals() {
        NameAndTutorialGroupPredicate firstPredicate =
                new NameAndTutorialGroupPredicate(Arrays.asList("Alice"), List.of(new TutorialGroup("T01")), List.of(),
                        List.of());
        NameAndTutorialGroupPredicate secondPredicate =
                new NameAndTutorialGroupPredicate(Arrays.asList("Bob"), List.of(new TutorialGroup("T02")), List.of(),
                        List.of());

        assertTrue(firstPredicate.equals(firstPredicate));
        assertTrue(firstPredicate.equals(new NameAndTutorialGroupPredicate(
                Arrays.asList("Alice"), List.of(new TutorialGroup("T01")), List.of(), List.of())));
        assertFalse(firstPredicate.equals(1));
        assertFalse(firstPredicate.equals(null));
        assertFalse(firstPredicate.equals(secondPredicate));
    }
}
