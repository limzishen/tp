package seedu.address.model.person;

import java.util.List;
import java.util.function.Predicate;

import seedu.address.commons.util.StringUtil;
import seedu.address.commons.util.ToStringBuilder;

/**
 * Tests that a {@code Person}'s {@code Name} matches all of the name keywords given (each as a word prefix,
 * case-insensitive) and/or the {@code TutorialGroup} matches any of the tutorial groups given.
 */
public class NameAndTutorialGroupPredicate implements Predicate<Person> {
    private final List<String> nameKeywords;
    private final List<TutorialGroup> tutorialGroups;

    /**
     * Constructs a predicate that matches persons by name keywords and/or tutorial groups.
     *
     * @param nameKeywords   Name keywords to match; every keyword must match (case-insensitive, word-prefix match).
     * @param tutorialGroups Tutorial groups to match (exact match).
     */
    public NameAndTutorialGroupPredicate(List<String> nameKeywords, List<TutorialGroup> tutorialGroups) {
        this.nameKeywords = nameKeywords;
        this.tutorialGroups = tutorialGroups;
    }

    @Override
    public boolean test(Person person) {
        if (nameKeywords.isEmpty() && tutorialGroups.isEmpty()) {
            return false;
        }

        boolean matchesName = nameKeywords.isEmpty() || nameKeywords.stream()
                .allMatch(keyword -> StringUtil.containsWordPrefixIgnoreCase(person.getName().fullName, keyword));
        boolean matchesTutorialGroup = tutorialGroups.isEmpty() || tutorialGroups.stream()
                .anyMatch(group -> person.getTutorialGroup().value.equalsIgnoreCase(group.value));

        return matchesName && matchesTutorialGroup;
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
                && tutorialGroups.equals(otherPredicate.tutorialGroups);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("nameKeywords", nameKeywords)
                .add("tutorialGroups", tutorialGroups)
                .toString();
    }
}
