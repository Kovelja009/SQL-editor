package checker.rules;

import checker.rules.neon.AbstractRule;
import execute.data.RunData;
import execute.data.Statement;
import gui.MainFrame;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.MutableTriple;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Rule02 extends AbstractRule {
    @Override
    public boolean checkRule(Object data) {
        RunData runData = (RunData) data;

        if(runData.isProcedure())
            return true;

        Map<String,String> aliasTable = new HashMap<>();
        String lastTable="";

        for(Statement statement : runData.getStatementList()){
            if(statement.getKeyword().equalsIgnoreCase("from") && !statement.getText().toLowerCase().contains(" join ")){
                MutablePair<String,String> pair = runData.getFromArguments(statement.getText());
                if(pair.right!=null)
                    aliasTable.put(runData.refurb(pair.right), runData.refurb(pair.left));
                lastTable= runData.refurb(pair.left);
            }
            if(statement.getKeyword().equalsIgnoreCase("from") && statement.getText().toLowerCase().contains(" join ")){
                String str = trimToJoin(statement.getText());
                MutablePair<String,String> pair = runData.getFromArguments(str);
                if(pair.right!=null)
                    aliasTable.put(runData.refurb(pair.right), runData.refurb(pair.left));
                lastTable = runData.refurb(pair.left);

                MutableTriple<String, String, String> arg = runData.getJoinArguments(statement.getText()).get(0);
                String table = runData.refurb(lastTable);
                String joinTable = runData.refurb(arg.left);
                if(joinTable.contains(" ")) {
                    int ind = joinTable.indexOf(" ");
                    String table2 = runData.refurb(joinTable.substring(0,ind).strip());
                    String alias = runData.refurb(joinTable.substring(ind + 1).strip());
                    joinTable = runData.refurb(table2);
                    aliasTable.put(alias,joinTable);
                }
                String column1 = runData.refurb(arg.middle);
                String column2 = runData.refurb(arg.right);
                if (column1.contains(".")) {
                    int ind=column1.indexOf(".");
                    table = runData.refurb(column1.substring(0,ind));
                    if(aliasTable.containsKey(table))
                        table=aliasTable.get(table);
                    column1 = runData.refurb(column1.substring(ind+1));
                }
                if (column2.contains(".")) {
                    int ind=column2.indexOf(".");
                    joinTable = runData.refurb(column2.substring(0,ind));
                    if(aliasTable.containsKey(joinTable))
                        joinTable=aliasTable.get(joinTable);
                    column2 = runData.refurb(column2.substring(ind+1));
                }
                lastTable = runData.refurb(table);
                System.out.println("JOIN CHECK "+table+" "+column1+" "+joinTable+" "+column2);
                if(!MainFrame.getInstance().getAppCore().getDatabase().isForeignKey(runData.refurb(table),runData.refurb(column1), runData.refurb(joinTable), runData.refurb(column2))) {
                    List<String> dta = new ArrayList<>();
                    dta.add(column1);
                    dta.add(table);
                    dta.add(column2);
                    dta.add(joinTable);
                    generateErrorSuggestion(dta);
                    return false;
                }
            }

        }

        return true;
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
//        return new MutablePair<>(errors[0].replaceFirst("%s",column1).replaceFirst("%s",table)
//                .replaceFirst("%s",column2).replaceFirst("%s",joinTable),
//                suggestions[0].replace("%s",column1));
        List<String> l = (List<String>) data;
        String err = getError().replaceFirst("%s",l.get(0)).replaceFirst("%s", l.get(1)).replaceFirst("%s", l.get(2)).replaceFirst("%s", l.get(3));
        setErrorMsg(err);
        String sgt = getSuggestion().replace("%s",l.get(0));
        setSuggestionMsg(sgt);
    }
}
