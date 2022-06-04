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
            if(Keywords.getInstance().isAggreateFunction(token)){
                generateErrorSuggestion(token.substring(0, 3));
                return false;
            }
//            System.out.println(token + " is agregate-> " + Keywords.getInstance().isAggreateFunction(token));
        }
        //////////////////////////////////////////////////////////////
//        ako je sve proslo
        setErrorMsg("");
        setSuggestionMsg("Return rule07 to true - everything else is fine");
        return false;
    }

    @Override
    public void generateErrorSuggestion(Object data) {
        setErrorMsg(getError().replace("%1", (String)data));
        setSuggestionMsg(getSuggestion().replace("%1", (String)data));
    }
}
