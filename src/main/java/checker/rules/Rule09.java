package checker.rules;

import checker.rules.neon.AbstractRule;
import execute.data.RunData;

public class Rule09 extends AbstractRule {

    @Override
    public boolean checkRule(Object data) {
        RunData runData = (RunData) data;

        if(!runData.isProcedure())
            return true;



        return true;
    }

    @Override
    public void generateErrorSuggestion(Object data) {
        super.generateErrorSuggestion(data);
    }
}
