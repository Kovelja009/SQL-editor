package checker;

import checker.rules.AbstractRule;
import checker.rules.RuleFactory;
import controller.actions.Task;
import gui.MainFrame;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.swing.*;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class Checker {
    private List<AbstractRule> stack = new ArrayList<>();
    private Check bulkCheck;
    private Check runCheck;

    public Checker(){
        bulkCheck = new Check();
        runCheck = new Check();
        loadJson();
    }

    private void loadJson(){
        JSONParser parser = new JSONParser();
        JSONArray jsonRules = null;
        try{
            jsonRules = (JSONArray) parser.parse(new FileReader("src/main/java/jsonFiles/rules.json"));
        }catch (Exception e){
            e.printStackTrace();
        }
        if(jsonRules == null) return;

        for (Object r : jsonRules)
        {
            JSONObject rule = (JSONObject) r;
            String name = (String) rule.get("name");
            String error = (String) rule.get("error");
            String suggestion = (String) rule.get("suggestion");
            AbstractRule newRule = RuleFactory.getRule(name);
            newRule.setName(name);
            newRule.setError(error);
            newRule.setSuggestion(suggestion);
            if(name.contains("bulk"))
                bulkCheck.addRule(newRule);
            else
                runCheck.addRule(newRule);
        }
    }

    public boolean check(Task task, Object data){
        stack.clear();
        boolean passed = true;
        if(task.equals(Task.IMPORT)){
            passed = bulkCheck.check(data);
            if(!passed)
                makeStackTrace(bulkCheck);
        }
        if(task.equals(Task.RUN)){
            passed = runCheck.check(data);
            if(!passed)
                makeStackTrace(runCheck);
        }

        return passed;
    }

    public void makeStackTrace(Check check){
        String stackTrace = "";
        for(AbstractRule r : check.getStack())
            stackTrace += r.getErrorMsg() + "\n" + r.getSuggestionMsg();

        JOptionPane.showMessageDialog(MainFrame.getInstance(), stackTrace, "", JOptionPane.ERROR_MESSAGE);
    }
}
