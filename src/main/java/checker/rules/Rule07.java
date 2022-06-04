package checker.rules;

import checker.rules.neon.AbstractRule;

public class Rule07 extends AbstractRule {
    @Override
    public boolean checkRule(Object data) {
        generateErrorSuggestion(null);
        setErrorMsg("");
        setSuggestionMsg("Return rule07 to true - everything else is fine");
        return false;
    }

    @Override
    public void generateErrorSuggestion(Object data) {
        super.generateErrorSuggestion(data);
    }
}
