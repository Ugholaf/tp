package soconnect.model.person.SearchPerson;

import soconnect.commons.util.StringUtil;
import soconnect.logic.parser.ArgumentMultimap;
import soconnect.model.person.Person;

import java.util.List;
import java.util.function.Predicate;

import static soconnect.logic.parser.CliSyntax.INDICATOR_ADDRESS;
import static soconnect.logic.parser.CliSyntax.INDICATOR_EMAIL;
import static soconnect.logic.parser.CliSyntax.INDICATOR_NAME;
import static soconnect.logic.parser.CliSyntax.INDICATOR_PHONE;
import static soconnect.logic.parser.CliSyntax.INDICATOR_TAG;

/**
 * Tests that a {@code Person}'s information matches the keyword given.
 */
public class ContactContainsAllKeywordsPredicate implements Predicate<Person> {
    private final ArgumentMultimap argMultimap;
    private boolean isNameContained = true;
    private boolean isAddressContained = true;
    private boolean isEmailContained = true;
    private boolean isPhoneContained = true;
    private boolean isTagContained = true;

    /**
     * Constructs the ContactContainsAllKeywordsPredicate object.
     */
    public ContactContainsAllKeywordsPredicate(ArgumentMultimap argMultimap) {
        this.argMultimap = argMultimap;
    }

    @Override
    public boolean test(Person person) {
        for (prefix : argMultimap.get) {
            String prefix = prefixes.get(i);
            List<String> keywords = searchedKeywords.get(i);
            switch (prefix) {
            case INDICATOR_NAME:
                isNameContained = keywords.stream()
                        .allMatch(keyword -> StringUtil.containsKeywordsIgnoreCase(person.getName().fullName, keyword));
                break;
            case INDICATOR_ADDRESS:
                isAddressContained = keywords.stream()
                        .allMatch(keyword -> StringUtil.containsKeywordsIgnoreCase(person.getAddress().value, keyword));
                break;
            case INDICATOR_EMAIL:
                isEmailContained = keywords.stream()
                        .allMatch(keyword -> StringUtil.containsKeywordsIgnoreCase(person.getEmail().value, keyword));
                break;
            case INDICATOR_PHONE:
                isPhoneContained = keywords.stream()
                        .allMatch(keyword -> StringUtil.containsKeywordsIgnoreCase(person.getPhone().value, keyword));
                break;
            case INDICATOR_TAG:
                isTagContained = true; // Implementation postponed, waiting for tag feature
                break;
            default:
                break;
            }
        }
        return isNameContained && isAddressContained && isEmailContained && isPhoneContained && isTagContained;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof ContactContainsAllKeywordsPredicate)) {
            return false;
        }
        ContactContainsAllKeywordsPredicate otherPred = (ContactContainsAllKeywordsPredicate) other;

        if (prefixes.size() != otherPred.prefixes.size()
                || searchedKeywords.size() != otherPred.searchedKeywords.size()) {
            return false;
        }
        boolean result = true;
        for (int i = 0; i < prefixes.size(); i++) {
            result = prefixes.get(i).equals(otherPred.prefixes.get(i))
                    && searchedKeywords.get(i).equals(otherPred.searchedKeywords.get(i));
            if (!result) {
                break;
            }
        }
        return result;
    }
}
