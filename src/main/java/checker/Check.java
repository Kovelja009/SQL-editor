package checker;

import checker.rules.neon.AbstractRule;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Check {
    List<AbstractRule> rules;
    private List<AbstractRule> stack;

    public Check(){
        rules = new ArrayList<>();
        stack = new ArrayList<>();
    }

    public boolean check(Object data){
        stack.clear();
        for(AbstractRule r : rules){
            if(!r.checkRule(data))
                stack.add(r);
        }
        System.out.println("prosao check stack je empty: " + stack.isEmpty());
        return stack.isEmpty();
    }
    public void addRule(AbstractRule rule) {
        rules.add(rule);
    }
}
