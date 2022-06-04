package checker.rules;

import checker.rules.neon.AbstractRule;
import execute.data.RunData;
import execute.data.Statement;

import java.util.List;

public class Rule04 extends AbstractRule {

    @Override
    public boolean checkRule(Object data) {
        RunData runData = (RunData) data;
        if(runData.isProcedure())
            return true;




        boolean noSelect = true;
        boolean noFrom = true;
        boolean other = false;

        boolean global = true;

        for(Statement statement : runData.getStatementList()){
            if(statement.getKeyword().equalsIgnoreCase("select")){
                noSelect = false;
            }
            if(statement.getKeyword().equalsIgnoreCase("from")){
                noFrom = false;
            }
            if(statement.getKeyword().equalsIgnoreCase("update") || statement.getKeyword().equalsIgnoreCase("set")
            || statement.getKeyword().equalsIgnoreCase("insert") || statement.getKeyword().equalsIgnoreCase("into")
            || statement.getKeyword().equalsIgnoreCase("delete") || statement.getKeyword().equalsIgnoreCase("exec"))
                other = true;
        }
        if(other){
            global = other_check(runData.getStatementList());
        }

        if(noSelect && !other){
            generateErrorSuggestion("select");
            return false;
        }

        if(noFrom && !other){
            generateErrorSuggestion("from");
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
            boolean isJoin = text.toLowerCase().contains(" join ");

            if(!statement.getKeyword().equalsIgnoreCase("from") && isJoin){
                generateErrorSuggestion("join in from");
                return false;
            }
        }

        return global;
    }

    boolean other_check(List<Statement> statementList){
        boolean isUpdate = false;
        boolean isSet = false;
        boolean isInsert = false;
        boolean isInto = false;

        for(Statement statement : statementList){
            if(statement.getKeyword().equalsIgnoreCase("update"))
                isUpdate = true;
            if(statement.getKeyword().equalsIgnoreCase("set"))
                isSet = true;
            if(statement.getKeyword().equalsIgnoreCase("insert"))
                isInsert = true;
            if(statement.getKeyword().equalsIgnoreCase("into"))
                isInto = true;
        }

        if(isUpdate && !isSet){
            generateErrorSuggestion("set");
            return false;
        }
        if((!isUpdate && isSet)){
            generateErrorSuggestion("update");
            return false;
        }
        if((isInsert && !isInto)){
            generateErrorSuggestion("into");
            return false;
        }
        if(!isInsert && isInto){
            generateErrorSuggestion("insert");
            return false;
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
