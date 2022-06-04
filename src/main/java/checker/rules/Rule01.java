package checker.rules;

import checker.rules.neon.AbstractRule;
import execute.data.RunData;
import execute.data.Statement;

public class Rule01 extends AbstractRule {
    @Override
    public boolean checkRule(Object data) {
        RunData runData = (RunData) data;

        if(runData.isProcedure())
            return true;

        for(Statement statement : runData.getStatementList()){
            checkStatement(statement);
        }

        generateErrorSuggestion(runData);
        return false;
    }

    private boolean checkStatement(Statement statement){
        return false;
    }

    @Override
    public void generateErrorSuggestion(Object data) {
        setErrorMsg(((RunData)data).getQueryText());
    }
}
