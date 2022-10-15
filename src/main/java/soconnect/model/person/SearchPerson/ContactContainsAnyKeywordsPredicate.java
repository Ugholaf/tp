package soconnect.model.person.SearchPerson;

import soconnect.commons.util.StringUtil;
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
public class ContactContainsAnyKeywordsPredicate implements Predicate<Person> {
    private final List<List<String>> searchedKeywords;
    private final List<String> prefixes;
    private boolean isNameContained = false;
    private boolean isAddressContained = false;
    private boolean isEmailContained = false;
    private boolean isPhoneContained = false;
    private boolean isTagContained = false;

    /**
     * Constructs the ContactContainsAnyKeywordsPredicate object.
     */
    public ContactContainsAnyKeywordsPredicate(List<String> prefixes, List<List<String>> searchedKeywords) {
        this.searchedKeywords = searchedKeywords;
        this.prefixes = prefixes;
    }

    @Override
    public boolean test(Person person) {
        for (int i = 0; i < prefixes.size(); i++) {
            String prefix = prefixes.get(i);
            List<String> keywords = searchedKeywords.get(i);
            switch (prefix) {
            case INDICATOR_NAME:
                isNameContained = keywords.stream()
                        .anyMatch(keyword -> StringUtil.containsKeywordsIgnoreCase(person.getName().fullName, keyword));
                break;
            case INDICATOR_ADDRESS:
                isAddressContained = keywords.stream()
                        .anyMatch(keyword -> StringUtil.containsKeywordsIgnoreCase(person.getAddress().value, keyword));
                break;
            case INDICATOR_EMAIL:
                isEmailContained = keywords.stream()
                        .anyMatch(keyword -> StringUtil.containsKeywordsIgnoreCase(person.getEmail().value, keyword));
                break;
            case INDICATOR_PHONE:
                isPhoneContained = keywords.stream()
                        .anyMatch(keyword -> StringUtil.containsKeywordsIgnoreCase(person.getPhone().value, keyword));
                break;
            case INDICATOR_TAG:
                isTagContained = true; // Implementation postponed, waiting for tag feature
                break;
            default:
                break;
            }
        }
        return isNameContained || isAddressContained || isEmailContained || isPhoneContained || isTagContained;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ContactContainsAnyKeywordsPredicate // instanceof handles nulls
                // state check
                && searchedKeywords.equals(((ContactContainsAnyKeywordsPredicate) other).searchedKeywords));
    }

}
