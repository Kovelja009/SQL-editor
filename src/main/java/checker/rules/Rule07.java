package checker.rules;

import checker.rules.neon.AbstractRule;
import execute.data.Keywords;
import execute.data.RunData;
import execute.data.Statement;

// where doesn`t have aggregate function
public class Rule07 extends AbstractRule {
    @Override
    public boolean checkRule(Object data) {

        RunData runData = (RunData) data;

        String txt = "";
        for(Statement statement : runData.getStatementList()){
            if(statement.getKeyword().equalsIgnoreCase("where")){
                txt = statement.getText();
            }
        }
        if(txt.equals(""))
            return true;

        String[] tokens = txt.split("[ :;=,><]");

        for (String token : tokens){
            if(token.equals(""))
                continue;
            if(Keywords.getInstance().isAggreateFunction(token)){
                generateErrorSuggestion(token.substring(0, 3));
                return false;
            }
        }

        return true;
    }

    @Override
    public void generateErrorSuggestion(Object data) {
        setErrorMsg(getError().replace("%1", (String)data));
        setSuggestionMsg(getSuggestion().replace("%1", (String)data));
    }
}
