package checker.rules;

import checker.rules.neon.AbstractRule;
import execute.data.Keywords;
import execute.data.RunData;
import resources.enums.AttributeType;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Rule09 extends AbstractRule {

    @Override
    public boolean checkRule(Object data) {
        RunData runData = (RunData) data;

        if (!runData.isDeclare() && !runData.isProcedure())
            return true;

        Map<String, AttributeType> var = new HashMap<>();
        for(Map.Entry<String, AttributeType> entry : runData.getVariables().entrySet()){
            var.put(entry.getKey(), entry.getValue());
        }

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


        String [] words = tmpText.split("[ ,:;*()]");
        for(String s : words){
            if (var.keySet().contains(s)) var.keySet().remove(s);
        }

        if(!var.isEmpty()){
            generateErrorSuggestion(var.keySet());
            System.out.println("lista je empty");
            return false;
        }
        System.out.println("lista je empty");
        return true;

    }

    @Override
    public void generateErrorSuggestion(Object data) {
        Set<String> set = (Set<String>) data;
        String tmp = "";
        for(String str : set)
            tmp += ","+str;

        tmp = tmp.substring(1);

        String msg = getError();
        msg = msg.replace("%1", tmp);
        setErrorMsg(msg);

        msg = getSuggestion();
        msg = msg.replace("%1", tmp);
        setSuggestionMsg(msg);
    }
}
