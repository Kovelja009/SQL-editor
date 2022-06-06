package checker.rules;

import checker.rules.neon.AbstractRule;
import execute.data.RunData;
import execute.data.Statement;
import gui.MainFrame;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.MutableTriple;
import resources.DBNode;
import resources.DBNodeComposite;
import resources.implementation.InformationResource;
import tree.TreeItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// column exist?
public class Rule01 extends AbstractRule {
    Map<String,String> aliasTable = new HashMap<>();
    List<String> columnsForCheck = new ArrayList<>();

    @Override
    public boolean checkRule(Object data) {
        RunData runData = (RunData) data;

        if(runData.isProcedure())
            return true;



        for(Statement stat : runData.getStatementList()){
            if(stat.getKeyword().equalsIgnoreCase("from") && !stat.getText().toLowerCase().contains(" join ")){
                System.out.println("Stateee: " + stat.getText());
               MutablePair<String,String> pair = runData.getFromArguments(stat.getText());
                    if(pair.right!=null){
                        if(stat.isHasSubquery())
                            aliasTable.put(runData.stripOfFunction(pair.right), "subquery");
                        else
                            aliasTable.put(runData.stripOfFunction(pair.right),pair.left);
                    }
            }
            if(stat.isHasSubquery())
                continue;

            if(stat.getKeyword().equalsIgnoreCase("from") && stat.getText().toLowerCase().contains(" join ")){
                String str = runData.trimToJoin(stat.getText());
                MutablePair<String,String> pair = runData.getFromArguments(str);
                columnsForCheck.add(pair.left);
                if(pair.right!=null)
                    aliasTable.put(pair.right,pair.left);

                for(MutableTriple<String,String,String> triple: runData.getJoinArguments(stat.getText())) {
                    if(triple.left.contains(" ")) {
                        int ind = triple.left.indexOf(" ");
                        String table = triple.left.substring(0,ind).strip();
                        String alias = triple.left.substring(ind+1).strip();
                        columnsForCheck.add(table);
                        aliasTable.put(alias,table);
                    }
                }

            }
        }

        System.out.println(columnsForCheck + "<- idew");

        for(Statement stat : runData.getStatementList()){
            if(stat.isHasSubquery())
                continue;

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
                        if(table.charAt(0) == '(' && table.charAt(table.length() - 1) == ')')
                            continue;
                        if (!exist(table, null, columnsForCheck)) {
                            generateErrorSuggestion(table);
                            System.out.println("Usao 1");
                            return false;
                        }
                        if (!exist(table, column, columnsForCheck)) {
                            setErrorMsg(column + " " + " doesn`t exist in " + table);
                            setSuggestionMsg(getSuggestion());
                            System.out.println("Usao 2");
                            return false;
                        }
                    } else if (!existSelect(runData.stripOfFunction(pair.left.strip()), columnsForCheck)){
                        generateErrorSuggestion(pair.left);
                        System.out.println("Usao 3" + pair.left);
                        return false;

                    }
                }
            }

            if(stat.getKeyword().equalsIgnoreCase("from") && !stat.getText().toLowerCase().contains(" join ")){
                MutablePair<String,String> pair = runData.getFromArguments(stat.getText());
                if(pair.left.charAt(0) == '(' && pair.left.charAt(pair.left.length()-1) == ')')
                    continue;
                if(!exist(pair.left,null, columnsForCheck)){
                    generateErrorSuggestion(pair.left);
                    System.out.println("Usao 4 i bio je zahtev");
                    return false;
                }
            }

            if(stat.getKeyword().equalsIgnoreCase("from") && stat.getText().toLowerCase().contains(" join ")){
                String str = runData.trimToJoin(stat.getText());
                MutablePair<String,String> pair = runData.getFromArguments(str);
                if(!exist(pair.left,null, columnsForCheck)){
                    generateErrorSuggestion(pair.left);
                    System.out.println("Usao 5");
                    return false;
                }
                for (MutableTriple<String, String, String> arg : runData.getJoinArguments(stat.getText())) {
                    String table = arg.left;
                    if (table.contains(" "))
                        table = table.substring(0, table.indexOf(" "));
                    if(!exist(table, null, columnsForCheck)){
                        System.out.println("Usao 6");
                        generateErrorSuggestion(table);
                        return false;
                    }
                    if (arg.middle.contains(".")) {
                        int ind = arg.middle.indexOf(".");
                        String table2 = arg.middle.substring(0, ind);
                        table2 = runData.refurb(table2);
                        String column = arg.middle.substring(ind + 1);
                        if (aliasTable.containsKey(table2))
                            table2 = aliasTable.get(table2);
                        if (!exist(table2, column, columnsForCheck)){
                            setErrorMsg(column + " " + " doesn`t exist in " + table2);
                            System.out.println("Usao 7");
                            setSuggestionMsg(getSuggestion());
                            return false;
                        }
                    } else if (!exist(null, arg.middle, columnsForCheck)){
                        generateErrorSuggestion(arg.middle);
                        System.out.println("Usao 8");
                        return false;
                    }
                    if (arg.right.contains(".")) {
                        int ind = arg.right.indexOf(".");
                        String table2 = arg.right.substring(0, ind);
                        String column = arg.right.substring(ind + 1);
                        column = runData.refurb(column);
                        if (aliasTable.containsKey(table2))
                            table2 = aliasTable.get(table2);
                        if (!exist(table2, column, columnsForCheck)) {
                            System.out.println("Usao 9");
                            setErrorMsg(column + " " + " doesn`t exist in " + table2);
                            setSuggestionMsg(getSuggestion());
                            return false;
                        }
                    }else if (!exist(null, arg.right, columnsForCheck)){
                        System.out.println("Usao 10");
                        generateErrorSuggestion(arg.right);
                        return false;
                    }
                }

            }

        }
        return true;
    }

    boolean existSelect(String column, List<String> tablesForCheck){
        boolean contains = false;

        InformationResource ir = (InformationResource) ((TreeItem)MainFrame.getInstance().getAppCore().getDefaultTreeModel().getRoot()).getDbNode();

        for(String tblcheck : tablesForCheck){
            for(DBNode e: ir.getChildren()){
                if(e.getName().equalsIgnoreCase(tblcheck)){
                    for(DBNode c: ((DBNodeComposite)e).getChildren()){
                        if(c.getName().equalsIgnoreCase(column))
                            return true;
                    }
                }
            }
        }
        return false;
    }

    boolean exist(String table, String column, List<String> columnsForCheck){
        if((aliasTable.get(table) != null && aliasTable.get(table).equalsIgnoreCase("subquery"))
                || (aliasTable.get(column) != null && aliasTable.get(column).equalsIgnoreCase("subquery")))
            return true;
        if((table != null && table.equalsIgnoreCase("subquery")) ||
                (column != null && column.equalsIgnoreCase("subquery")))
            return true;
        System.out.println("");
        return  MainFrame.getInstance().getAppCore().existInDatabase(table,column, columnsForCheck);
    }



    @Override
    public void generateErrorSuggestion(Object data) {
        setErrorMsg(getError().replace("%1", (String)data));
        setSuggestionMsg(getSuggestion());
    }
}
