package checker.rules.neon;

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

    //TODO set checkRUle and generateErrorSuggestion to abstract
    public  boolean checkRule(Object data){
        generateErrorSuggestion(null);
        return false;
    }
    public void generateErrorSuggestion(Object data){
     errorMsg = error;
     suggestionMsg = suggestion;
    }
    public void clearErrorSuggestion(){
        errorMsg = "";
        suggestionMsg = "";
    }

    @Override
    public String toString() {
        return name + " " + error + " " + suggestion;
    }
}
