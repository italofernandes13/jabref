package org.jabref.gui.search;

import java.util.EnumSet;
import java.util.List;

import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import org.jabref.gui.search.rules.describer.ContainsAndRegexBasedSearchRuleDescriber;
import org.jabref.gui.util.TooltipTextUtil;
import org.jabref.model.search.rules.SearchRules;
import org.jabref.model.search.rules.SearchRules.SearchFlags;
import org.jabref.testutils.category.GUITest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

@GUITest
@ExtendWith(ApplicationExtension.class)
class ContainsAndRegexBasedSearchRuleDescriberTest {

    @Start
    void onStart(Stage stage) {
        // Needed to init JavaFX thread
        stage.show();
    }

    @Test
    void testSimpleTerm() {
        String query = "test";
        List<Text> expectedTexts = List.of(
                TooltipTextUtil.createText("This search contains entries in which any field contains the term "),
                TooltipTextUtil.createText("test", TooltipTextUtil.TextType.BOLD),
                TooltipTextUtil.createText(" (case insensitive). "));
        TextFlow description = new ContainsAndRegexBasedSearchRuleDescriber(EnumSet.noneOf(SearchFlags.class), query).getDescription();

        TextFlowEqualityHelper.assertEquals(expectedTexts, description);
    }

    @Test
    void testNoAst() {
        String query = "a b";
        List<Text> expectedTexts = List.of(
                TooltipTextUtil.createText("This search contains entries in which any field contains the term "),
                TooltipTextUtil.createText("a", TooltipTextUtil.TextType.BOLD),
                TooltipTextUtil.createText(" and "),
                TooltipTextUtil.createText("b", TooltipTextUtil.TextType.BOLD),
                TooltipTextUtil.createText(" (case insensitive). "));
        TextFlow description = new ContainsAndRegexBasedSearchRuleDescriber(EnumSet.noneOf(SearchFlags.class), query).getDescription();

        TextFlowEqualityHelper.assertEquals(expectedTexts, description);
    }

    @Test
    void testNoAstRegex() {
        String query = "a b";
        List<Text> expectedTexts = List.of(
                TooltipTextUtil.createText("This search contains entries in which any field contains the regular expression "),
                TooltipTextUtil.createText("a", TooltipTextUtil.TextType.BOLD),
                TooltipTextUtil.createText(" and "),
                TooltipTextUtil.createText("b", TooltipTextUtil.TextType.BOLD),
                TooltipTextUtil.createText(" (case insensitive). "));
        TextFlow description = new ContainsAndRegexBasedSearchRuleDescriber(EnumSet.of(SearchRules.SearchFlags.REGULAR_EXPRESSION), query).getDescription();

        TextFlowEqualityHelper.assertEquals(expectedTexts, description);
    }

    @Test
    void testNoAstRegexCaseSensitive() {
        String query = "a b";
        List<Text> expectedTexts = List.of(
                TooltipTextUtil.createText("This search contains entries in which any field contains the regular expression "),
                TooltipTextUtil.createText("a", TooltipTextUtil.TextType.BOLD),
                TooltipTextUtil.createText(" and "),
                TooltipTextUtil.createText("b", TooltipTextUtil.TextType.BOLD),
                TooltipTextUtil.createText(" (case sensitive). "));
        TextFlow description = new ContainsAndRegexBasedSearchRuleDescriber(EnumSet.of(SearchRules.SearchFlags.CASE_SENSITIVE, SearchRules.SearchFlags.REGULAR_EXPRESSION), query).getDescription();

        TextFlowEqualityHelper.assertEquals(expectedTexts, description);
    }

    @Test
    void testNoAstCaseSensitive() {
        String query = "a b";
        List<Text> expectedTexts = List.of(
                TooltipTextUtil.createText("This search contains entries in which any field contains the term "),
                TooltipTextUtil.createText("a", TooltipTextUtil.TextType.BOLD),
                TooltipTextUtil.createText(" and "),
                TooltipTextUtil.createText("b", TooltipTextUtil.TextType.BOLD),
                TooltipTextUtil.createText(" (case sensitive). "));
        TextFlow description = new ContainsAndRegexBasedSearchRuleDescriber(EnumSet.of(SearchRules.SearchFlags.CASE_SENSITIVE), query).getDescription();

        TextFlowEqualityHelper.assertEquals(expectedTexts, description);
    }
    
    import org.jabref.model.entry.BibEntry;
    import org.jabref.model.entry.field.StandardField;
    import org.jabref.model.entry.types.StandardEntryType;
    import org.jabref.model.search.rules.SearchRules;
    import org.jabref.model.search.rules.SearchRules.SearchFlags;
    
    @Test
    // teste para verificar se a lista da buscar esta vazia
    // DECISÃO: 276
    public void testNoMatchesFromEmptyDatabaseWithInvalidSearchExpression() {
        List<BibEntry> matches = new DatabaseSearcher(INVALID_SEARCH_QUERY, database).getMatches();
        assertEquals(Collections.emptyList(), matches);
    }

    @Test
    // teste para verificar se a lista da buscar é uma regex valida
    // DECISÃO: 284
    public void testIsValidQueryAsRegEx() {
        assertTrue(new SearchQuery("asdf", EnumSet.of(SearchRules.SearchFlags.CASE_SENSITIVE, SearchRules.SearchFlags.REGULAR_EXPRESSION)).isValid());
    }

    @Test
    public void testIsMatch() {
        // teste para verificar se a listar da busca foi encontrada
        // DECISÃO: 290
        BibEntry entry = new BibEntry();
        entry.setType(StandardEntryType.Article);
        entry.setField(StandardField.AUTHOR, "asdf");

        assertFalse(new SearchQuery("BiblatexEntryType", EnumSet.of(SearchRules.SearchFlags.CASE_SENSITIVE, SearchRules.SearchFlags.REGULAR_EXPRESSION)).isMatch(entry));
        assertTrue(new SearchQuery("asdf", EnumSet.of(SearchRules.SearchFlags.CASE_SENSITIVE, SearchRules.SearchFlags.REGULAR_EXPRESSION)).isMatch(entry));
        assertTrue(new SearchQuery("author=asdf", EnumSet.of(SearchRules.SearchFlags.CASE_SENSITIVE, SearchRules.SearchFlags.REGULAR_EXPRESSION)).isMatch(entry));
    }
}
