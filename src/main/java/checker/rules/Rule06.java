package checker.rules;

import checker.rules.neon.AbstractRule;
import execute.data.Keywords;
import execute.data.RunData;
import execute.data.Statement;
import org.apache.commons.lang3.tuple.MutablePair;

import java.util.List;


// aggregate function - group by - correlation
public class Rule06 extends AbstractRule {

    @Override
    public boolean checkRule(Object data) {
        RunData runData = (RunData) data;

        String selectTxt = "";
        String groupByTxt = "";
        for(Statement statement : runData.getStatementList()){
            if(statement.getKeyword().equalsIgnoreCase("group by"))
                groupByTxt = statement.getText();
            if(statement.getKeyword().equalsIgnoreCase("select"))
                selectTxt = statement.getText();
        }

        if(selectTxt.equals("") || groupByTxt.equals(""))
            return true;

        if(!Keywords.getInstance().isAggreateFunction(selectTxt))
            return true;

        List<String> groupByArguments = runData.getGroupByArguments(groupByTxt);
        List<MutablePair<String, String>> selectArguments = runData.getSelectArguments(selectTxt);

        for(MutablePair<String, String> par : selectArguments) {
            if(Keywords.getInstance().isAggreateFunction(par.getLeft()))
                continue;
            if(!groupByArguments.contains(par.getLeft()) && !groupByArguments.contains(par.getRight())) {
                generateErrorSuggestion(par.getLeft());
                return false;
            }
        }
        return true;
    }

    @Override
    public void generateErrorSuggestion(Object data) {
        String rmp = (String) data;

        setErrorMsg(getError().replace("%1", rmp));
        setSuggestionMsg(getSuggestion().replace("%1", rmp));
    }
}
