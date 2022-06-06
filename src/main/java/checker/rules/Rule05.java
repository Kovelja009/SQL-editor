package checker.rules;

import checker.rules.neon.AbstractRule;
import execute.data.RunData;
import execute.data.Statement;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.MutableTriple;

import java.util.ArrayList;
import java.util.List;


// alias rule
public class Rule05 extends AbstractRule {
    @Override
    public boolean checkRule(Object data) {
        RunData runData = (RunData) data;

        if(runData.isProcedure() || runData.isDeclare())
            return true;

        for(Statement stat : runData.getStatementList()){
            if(stat.getKeyword().equalsIgnoreCase("select")){
                String wrongAlias = wrongAlias(runData.getSelectArguments(stat.getText()));
                if(!wrongAlias.equals("")){
                    generateErrorSuggestion(wrongAlias);
                    return false;
                }
            }
            if(stat.getKeyword().equalsIgnoreCase("from") && !stat.getText().toLowerCase().contains(" join ")){
                List<MutablePair<String, String>> lst = new ArrayList<>();
                lst.add(runData.getFromArguments(stat.getText()));
                String wrongAlias = wrongAlias(lst);
                if(!wrongAlias.equals("")){
                    generateErrorSuggestion(wrongAlias);
                    return false;
                }
            }
            if(stat.getKeyword().equalsIgnoreCase("from") && stat.getText().toLowerCase().contains(" join ")){
                String str = runData.trimToJoin(stat.getText());
                String arg = runData.getAli(str);  //stat.getText().substring(0, stat.getText().toLowerCase().indexOf(" join ")).strip()

                if(!arg.equals("") && !checkinAli(arg).equals("")){
                    generateErrorSuggestion(arg);
                    return false;
                }

                for(MutableTriple<String,String,String> triple: runData.getJoinArguments(stat.getText())) {
                        System.out.println(triple.left.strip() + "   " + triple.middle.strip() + "     " + triple.right.strip());
                        String leftali = runData.getDotAli(triple.left.strip());
                        String midali = runData.getDotAli(triple.middle.strip());
                        String rightali = runData.getDotAli(triple.right.strip());


                        leftali = checkinAli(leftali);
                        midali = checkinAli(midali);
                        rightali = checkinAli(rightali);

                        if(!leftali.equals("")){
                            generateErrorSuggestion(leftali);
                            return false;
                        }
                        if(!rightali.equals("")){
                            generateErrorSuggestion(rightali);
                            return false;
                        }
                        if(!midali.equals("")){
                            generateErrorSuggestion(midali);
                            return false;
                        }
                }
            }
        }
        return true;
    }

    private String wrongAlias(List<MutablePair<String, String>> data){
        for(MutablePair<String,String> pair: data) {
            if(pair.right!=null)
            {
                String alias = pair.right.strip();
                if(alias.contains(" ") && (alias.charAt(0)!='"' || alias.charAt(alias.length()-1)!='"')){
                    return alias;
                }
            }
        }
        return "";
    }

    private String checkinAli(String alias){
        if(alias.contains(" ") && (alias.charAt(0)!='"' || alias.charAt(alias.length()-1)!='"')){
            return alias;
        }
        return "";
    }

    @Override
    public void generateErrorSuggestion(Object data) {
        String str = (String) data;
        setErrorMsg(getError().replace("%1", str));
        setSuggestionMsg(getSuggestion());
    }
}
