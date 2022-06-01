package checker.rules;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractRule {

    private String name;
    private String suggestion;
    private String error;
    private String errorMsg = "";
    private String suggestionMsg = "";

    public abstract boolean checkRule(Object data);
    public abstract void generateErrorSuggestion(Object data);
    public void clearErrorSuggestion(){
        errorMsg = "";
        suggestionMsg = "";
    }
}
