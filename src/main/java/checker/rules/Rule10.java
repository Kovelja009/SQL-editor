package checker.rules;

import checker.data.Rule01Data;
import checker.rules.neon.AbstractRule;
import execute.data.ImportData;
import resources.DBNode;

import java.util.ArrayList;
import java.util.List;

// bulk_import rule -> validating columns before import
public class Rule10 extends AbstractRule {

    @Override
    public boolean checkRule(Object data) {
        ImportData importData = (ImportData) data;
        List<String> realColumns = new ArrayList<>();
        for(DBNode node : importData.getEntity().getChildren())
            realColumns.add(node.getName());
        List<String> wrongColumns = new ArrayList<>();
        boolean ok = true;
        for(String column : importData.getColumns()){
            if(!realColumns.contains(column)){
                wrongColumns.add(column);
                ok = false;
            }
        }
        if(!ok)
            generateErrorSuggestion(new Rule01Data(wrongColumns, importData.getEntity()));
        else
            clearErrorSuggestion();
        return ok;
    }

    @Override
    public void generateErrorSuggestion(Object data) {
        Rule01Data rule01Data = (Rule01Data) data;

        String tmp = "";
        for(String column : rule01Data.getWrongColumns())
            tmp += column + ",";
        tmp = tmp.substring(0, tmp.length()-2);

        String er = getError().replace("%1", tmp);
        er = er.replace("%2", rule01Data.getEntity().getName());
        setErrorMsg(er);

        tmp = "";
        for(DBNode node : rule01Data.getEntity().getChildren())
            tmp += node.getName() + ",";
        tmp = tmp.substring(0, tmp.length()-1);
        String sug = getSuggestion().replace("%1", tmp);
        setSuggestionMsg(sug);
    }

}
