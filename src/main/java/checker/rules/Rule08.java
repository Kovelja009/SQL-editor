package checker.rules;

import checker.rules.neon.AbstractRule;
import execute.data.Keywords;
import execute.data.RunData;
import gui.MainFrame;
import resources.enums.AttributeType;

import java.util.*;

public class Rule08 extends AbstractRule {
    @Override
    public boolean checkRule(Object data) {

        RunData runData = (RunData) data;

        if (!runData.isDeclare() && !runData.isProcedure())
            return true;

        String text = runData.getQueryText();

        int start = 0;
        int end = 0;
        for (int i = 0; i < text.length() - 2; i++) {
            if(i<(text.length()-5) && text.substring(i, i + 5).equalsIgnoreCase("begin") && (i == 0 || Keywords.getInstance().getCharacters().contains(text.charAt(i - 1))) && Keywords.getInstance().getCharacters().contains(text.charAt(i + 5))) {
                start = i + 5;
            }
            else if (text.substring(i, i + 3).equalsIgnoreCase("end") && (i == 0 || Keywords.getInstance().getCharacters().contains(text.charAt(i - 1))) && (Keywords.getInstance().getCharacters().contains(text.charAt(i + 3)) || i+3 == text.length())) {
                end = i;
                break;
            }
        }
        String tmpText = text.substring(start, end);
//        System.out.println("111111111111111111111111111111111111111111");
//        System.out.println(tmpText);
//        System.out.println("111111111111111111111111111111111111111111");
        String[] intoSplit = tmpText.split("into");
        if(intoSplit.length < 2){
            System.out.println("intoSplit error");
            return false;
        }
        List<String> left = Arrays.stream(intoSplit[0].split(" ")).toList();
        List<String> right = Arrays.stream(intoSplit[1].split(" ")).toList();

//        System.out.println("=====================================");
//        System.out.println(left);
//        System.out.println(right);
//        System.out.println("=====================================");

        List<String> tmpLevi = new ArrayList<>();
        List<String> tmpDesni = new ArrayList<>();

        for(int i = left.size()-1; i >= 0; i--){
            String tmp = left.get(i).replace(";", "").replace(",", "");
            if (Keywords.getInstance().getKeyWords().contains(tmp.toUpperCase()) || (tmp.length() == 1 && Keywords.getInstance().getCharacters().contains(tmp.charAt(0)))) {
                break;
            }
            if(tmp.equals(""))
                continue;
            tmpLevi.add(tmp);
//            System.out.println(tmp);
        }

        Collections.reverse(tmpLevi);

        for(int i = 0; i < right.size(); i++){
            String tmp = right.get(i).replace(";", "").replace(",", "");
            if (Keywords.getInstance().getKeyWords().contains(tmp.toUpperCase()) || (tmp.length() == 1 && Keywords.getInstance().getCharacters().contains(tmp.charAt(0)))) {
                break;
            }
            if(tmp.equals(""))
                continue;
            tmpDesni.add(tmp);
//            System.out.println(tmp);
        }

//        System.out.println(tmpLevi);
//        System.out.println(tmpDesni);

        int size = Integer.min(tmpDesni.size(), tmpLevi.size());
//        System.out.println(runData.getVariables().keySet() + " <- entry set");
        for(int i = 0; i < size; i++){
//            System.out.println("levi: " + tmpLevi.get(i) + getAtribute(tmpLevi.get(i)) + " desni: " + tmpDesni.get(i) + runData.getVariables().get(tmpDesni.get(i)));
            if(!provera(getAtribute(tmpLevi.get(i)), runData.getVariables().get(tmpDesni.get(i)))){
                System.out.println("usao za rec " + tmpLevi.get(i)+" "+tmpDesni.get(i));
                List<String> lista = new ArrayList<>();
                lista.add(tmpLevi.get(i));
                lista.add(tmpDesni.get(i));
                generateErrorSuggestion(lista);
                return false;
            }
            runData.getVariables().remove(tmpDesni.get(i));
        }

//        System.out.println("prosao type check");

        return poslednjaProvera(tmpText,runData.getVariables(), runData);

    }

    private boolean provera(AttributeType levi, AttributeType desni){
        if(desni.equals(AttributeType.NUMBER) && (levi.equals(AttributeType.BIGINT) || levi.equals(AttributeType.NUMERIC)
        || levi.equals(AttributeType.DECIMAL) || levi.equals(AttributeType.INT) || levi.equals(AttributeType.DECIMAL_UNSIGNED)||
        levi.equals(AttributeType.INT_UNSIGNED) || levi.equals(AttributeType.SMALLINT)))
            return true;
        if(levi == null || desni == null)
            return false;
        return levi.equals(desni);
    }

    private boolean poslednjaProvera(String text, Map<String, AttributeType> variables, RunData runData){
        if(variables.isEmpty())
            return true;

        String[] tokens = text.split("[ ,;:()]");
//        System.out.println("Tokeni:");
        for(String token : tokens){
            if(token.equals(""))
                continue;
            if(variables.containsKey(token)){
                if(Arrays.stream(tokens).toList().indexOf(token) - 2 >= 0){
                        if(!provera(getAtribute(tokens[Arrays.stream(tokens).toList().indexOf(token) - 2]), runData.getVariables().get(token))){
                            List<String> data = new ArrayList<>();
                            data.add(tokens[Arrays.stream(tokens).toList().indexOf(token) - 2]);
                            data.add(token);
                            generateErrorSuggestion(data);
                            return false;
                        }
                        runData.getVariables().remove(token);
//                    provera token i token - 2
//                    izbacivanje tokena
                }
            }
//            System.out.println(token);
        }
        return true;
    }

    private AttributeType getAtribute(String column){
        String[] tokens = column.split("\\.");
        String tbl = "";
        String att = "";

        if(tokens.length == 3){
            tbl = tokens[1];
            att = tokens[2];
        }
        if(tokens.length == 2){
            tbl = tokens[0];
            att = tokens[1];
        }
        if(tokens.length == 1){
            return MainFrame.getInstance().getAppCore().attributeType(tokens[0]);
        }
        return MainFrame.getInstance().getAppCore().getAttributeType(tbl, att);
    }

    @Override
    public void generateErrorSuggestion(Object data) {
        List<String> l = (List<String>) data;
        String tmp = getError();
        tmp = tmp.replace("%1", l.get(0));
        tmp = tmp.replace("%2", l.get(1));
        setErrorMsg(tmp);

        tmp = getSuggestion();
        tmp = tmp.replace("%1", l.get(1));
        setSuggestionMsg(tmp);
    }
}
