package checker.rules;

import checker.rules.neon.AbstractRule;
import execute.data.RunData;
import execute.data.Statement;

public class Rule04 extends AbstractRule {

    @Override
    public boolean checkRule(Object data) {
        RunData runData = (RunData) data;
        if(runData.isProcedure())
            return true;
        boolean noSelect = true;
        for(Statement statement : runData.getStatementList()){
            if(statement.getKeyword().equalsIgnoreCase("select")){
                noSelect = false;
            }
        }
        if(noSelect){
            generateErrorSuggestion("select");
            return false;
        }

        boolean groupBy = false;
        boolean having = false;

        for(Statement statement : runData.getStatementList()){
            if(statement.getKeyword().equalsIgnoreCase("group by")){
                groupBy = true;
            }
            if(statement.getKeyword().equalsIgnoreCase("having")){
                having = true;
            }
        }

        if(!groupBy && having){
            generateErrorSuggestion("group by");
            return false;
        }


        for(Statement statement : runData.getStatementList()){
            String text = statement.getText();
            boolean isJoin = text.toLowerCase().contains("join");
            if(!statement.getKeyword().equalsIgnoreCase("from") && isJoin){
                generateErrorSuggestion("join in from");
                return false;
            }
        }

        return true;
    }

    @Override
    public void generateErrorSuggestion(Object data) {
        String s = (String) data;
        String str = getError();
        str = str.replace("%1", s);
        setErrorMsg(str);
        str = getSuggestion();
        str = str.replace("%1", s);
        setSuggestionMsg(str);
    }
}
