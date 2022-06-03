package checker.rules;

import checker.rules.neon.AbstractRule;
import execute.data.RunData;
import execute.data.Statement;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Rule03 extends AbstractRule {
    @Override
    public boolean checkRule(Object data) {
        RunData runData = (RunData) data;

        if(runData.isProcedure())
            return true;

        boolean isOk = true;
        for(int i = 1; i < runData.getStatementList().size(); i++){
            if(runData.getStatementList().get(i).getPriority() <= runData.getStatementList().get(i-1).getPriority()){
                List<String> data1 = new ArrayList<>();
                data1.add(runData.getStatementList().get(i).getKeyword());
                data1.add(runData.getStatementList().get(i-1).getKeyword());
                generateErrorSuggestion(data1);
                return false;
            }
        }

        for(Statement statement : runData.getStatementList()){
            if(!statement.getKeyword().equalsIgnoreCase("from") && statement.getText().contains("join")){
                List<String> dt = new ArrayList<>();
                dt.add("join");
                generateErrorSuggestion(dt);
                return false;
            }
        }

        return true;
    }

    @Override
    public void generateErrorSuggestion(Object data) {
        List<String> dt = (List<String>) data;
        if(dt.size() == 1){
            setErrorMsg("Join must be in where");
            setSuggestionMsg("Put join in where");
        }else{
            String str = getError();
            str = str.replace("%1", dt.get(0));
            str = str.replace("%2", dt.get(1));
            setErrorMsg(str);

            str = getSuggestion();
            str = str.replace("%1",dt.get(1));
            str = str.replace("%2", dt.get(0));
            setSuggestionMsg(str);
        }

    }
}
