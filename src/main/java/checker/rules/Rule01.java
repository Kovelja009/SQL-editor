package checker.rules;

import checker.rules.neon.AbstractRule;
import execute.data.RunData;
import execute.data.Statement;
import gui.MainFrame;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.MutableTriple;

import java.util.HashMap;
import java.util.Map;

// column exist?
public class Rule01 extends AbstractRule {
    @Override
    public boolean checkRule(Object data) {
        RunData runData = (RunData) data;

        if(runData.isProcedure())
            return true;

        Map<String,String> aliasTable = new HashMap<>();

        for(Statement stat : runData.getStatementList()){
            if(stat.getKeyword().equalsIgnoreCase("from") && !stat.getText().toLowerCase().contains(" join ")){

               MutablePair<String,String> pair = runData.getFromArguments(stat.getText());
                    if(pair.right!=null)
                        aliasTable.put(pair.right,pair.left);
            }
            if(stat.getKeyword().equalsIgnoreCase("from") && stat.getText().toLowerCase().contains(" join ")){
                String str = trimToJoin(stat.getText());
                MutablePair<String,String> pair = runData.getFromArguments(str);
                if(pair.right!=null)
                    aliasTable.put(pair.right,pair.left);

                for(MutableTriple<String,String,String> triple: runData.getJoinArguments(stat.getText())) {
                    if(triple.left.contains(" ")) {
                        int ind = triple.left.indexOf(" ");
                        String table = triple.left.substring(0,ind).strip();
                        String alias = triple.left.substring(ind+1).strip();
                        aliasTable.put(alias,table);
                    }
                }

            }
        }

        for(Statement stat : runData.getStatementList()){
            if(stat.getKeyword().equalsIgnoreCase("select")){
                for (MutablePair<String, String> pair : runData.getSelectArguments(stat.getText())){
                    if (pair.left.equals("*") && runData.getSelectArguments(stat.getText()).size() == 1)
                        continue;
                    if (pair.left.contains(".")) {
                        int ind = pair.left.indexOf(".");
                        String table = pair.left.substring(0, ind);
                        String column = pair.left.substring(ind + 1);
                        if (aliasTable.containsKey(table))
                            table = aliasTable.get(table);
                        if (!exist(table, null)) {
                            generateErrorSuggestion(table);
                            return false;
                        }
                        if (!exist(table, column)) {
                            setErrorMsg(column + " " + " doesn`t exist in " + table);
                            setSuggestionMsg(getSuggestion());
                            return false;
                        }
                    } else if (!exist(null, pair.left))
                        generateErrorSuggestion(pair.left);
                        return false;
                }
            }

            if(stat.getKeyword().equalsIgnoreCase("from") && !stat.getText().toLowerCase().contains(" join ")){
                MutablePair<String,String> pair = runData.getFromArguments(stat.getText());
                if(!exist(pair.left,null)){
                    generateErrorSuggestion(pair.left);
                    return false;
                }
            }

            if(stat.getKeyword().equalsIgnoreCase("from") && stat.getText().toLowerCase().contains(" join ")){
                String str = trimToJoin(stat.getText());
                MutablePair<String,String> pair = runData.getFromArguments(str);
                if(!exist(pair.left,null)){
                    generateErrorSuggestion(pair.left);
                    return false;
                }
                for (MutableTriple<String, String, String> arg : runData.getJoinArguments(stat.getText())) {
                    String table = arg.left;
                    if (table.contains(" "))
                        table = table.substring(0, table.indexOf(" "));
                    if(!exist(table, null)){
                        generateErrorSuggestion(table);
                        return false;
                    }
                    if (arg.middle.contains(".")) {
                        int ind = arg.middle.indexOf(".");
                        String table2 = arg.middle.substring(0, ind);
                        String column = arg.middle.substring(ind + 1);
                        if (aliasTable.containsKey(table2))
                            table2 = aliasTable.get(table2);
                        if (!exist(table2, column)){
                            setErrorMsg(column + " " + " doesn`t exist in " + table2);
                            setSuggestionMsg(getSuggestion());
                            return false;
                        }
                    } else if (!exist(null, arg.middle)){
                        generateErrorSuggestion(arg.middle);
                        return false;
                    }
                    if (arg.right.contains(".")) {
                        int ind = arg.right.indexOf(".");
                        String table2 = arg.right.substring(0, ind);
                        String column = arg.right.substring(ind + 1);
                        if (aliasTable.containsKey(table2))
                            table2 = aliasTable.get(table2);
                        if (!exist(table2, column)) {
                            setErrorMsg(column + " " + " doesn`t exist in " + table2);
                            setSuggestionMsg(getSuggestion());
                            return false;
                        }
                    }else if (!exist(null, arg.right)){
                        generateErrorSuggestion(arg.right);
                        return false;
                    }
                }

            }

        }
        return true;
    }

    boolean exist(String table, String column){
        return  MainFrame.getInstance().getAppCore().existInDatabase(table,column);
    }

    private String trimToJoin(String text){
        int full = text.toLowerCase().indexOf(" full ");
        int left = text.toLowerCase().indexOf(" left ");
        int right = text.toLowerCase().indexOf(" right ");
        int outer = text.toLowerCase().indexOf(" outer ");
        int inner = text.toLowerCase().indexOf(" innner ");


        if(full != -1)
            return text.substring(0, full).strip();
        if(left != -1)
            return text.substring(0, left).strip();
        if(right != -1)
            return text.substring(0, right).strip();
        if(outer != -1)
            return text.substring(0, outer).strip();
        if(inner != -1)
            return text.substring(0, inner).strip();

        return text.substring(0, text.indexOf(" join ")).strip();
    }

    @Override
    public void generateErrorSuggestion(Object data) {
        setErrorMsg(getError().replace("%1", (String)data));
        setSuggestionMsg(getSuggestion());
    }
}
