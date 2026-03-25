package seedu.address.model.person;

import java.util.List;
import java.util.function.Predicate;

import seedu.address.commons.util.StringUtil;
import seedu.address.commons.util.ToStringBuilder;

/**
 * Tests that a {@code Person}'s {@code Name} matches any of the keywords given and/or
 * the {@code TutorialGroup} matches any of the tutorial groups given.
 */
public class NameAndTutorialGroupPredicate implements Predicate<Person> {
    private final List<String> nameKeywords;
    private final List<TutorialGroup> tutorialGroups;
    private final List<Email> emails;
    private final List<TeleHandle> teleHandles;

    /**
     * Constructs a predicate that matches persons by name keywords and/or tutorial groups.
     *
     * @param nameKeywords   Name keywords to match (case-insensitive, full-word match).
     * @param tutorialGroups Tutorial groups to match (exact match).
     * @param emails         Emails to match (exact match).
     * @param teleHandles    Telegram handles to match (exact match).
     */
    public NameAndTutorialGroupPredicate(List<String> nameKeywords, List<TutorialGroup> tutorialGroups,
                                         List<Email> emails, List<TeleHandle> teleHandles) {
        this.nameKeywords = nameKeywords;
        this.tutorialGroups = tutorialGroups;
        this.emails = emails;
        this.teleHandles = teleHandles;
    }

    @Override
    public boolean test(Person person) {
        if (nameKeywords.isEmpty() && tutorialGroups.isEmpty() && emails.isEmpty() && teleHandles.isEmpty()) {
            return false;
        }

        boolean matchesName = nameKeywords.isEmpty() || nameKeywords.stream()
                .anyMatch(keyword -> StringUtil.containsWordIgnoreCase(person.getName().fullName, keyword));
        boolean matchesTutorialGroup = tutorialGroups.isEmpty() || tutorialGroups.stream()
                .anyMatch(group -> person.getTutorialGroup().value.equalsIgnoreCase(group.value));
        boolean matchesEmail = emails.isEmpty() || emails.stream()
                .anyMatch(email -> person.getEmail().value.equalsIgnoreCase(email.value));
        boolean matchesTeleHandle = teleHandles.isEmpty() || teleHandles.stream()
                .anyMatch(teleHandle -> person.getTeleHandle().value.equalsIgnoreCase(teleHandle.value));

        return matchesName && matchesTutorialGroup && matchesEmail && matchesTeleHandle;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof NameAndTutorialGroupPredicate)) {
            return false;
        }

        NameAndTutorialGroupPredicate otherPredicate = (NameAndTutorialGroupPredicate) other;
        return nameKeywords.equals(otherPredicate.nameKeywords)
                && tutorialGroups.equals(otherPredicate.tutorialGroups)
                && emails.equals(otherPredicate.emails)
                && teleHandles.equals(otherPredicate.teleHandles);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("nameKeywords", nameKeywords)
                .add("tutorialGroups", tutorialGroups)
                .add("emails", emails)
                .add("teleHandles", teleHandles)
                .toString();
    }
}
