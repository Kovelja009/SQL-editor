package checker.rules.neon;

import checker.rules.*;
import checker.rules.neon.AbstractRule;

public class RuleFactory {
    public static AbstractRule getRule(String name){
        if(name.contains("rule01"))
            return new Rule01();
        if(name.contains("rule02"))
            return new Rule02();
        if(name.contains("rule03"))
            return new Rule03();
        if(name.contains("rule04"))
            return new Rule04();
        if(name.contains("rule05"))
            return new Rule05();
        if(name.contains("rule06"))
            return new Rule06();
        if(name.contains("rule07"))
            return new Rule07();
        if(name.contains("rule08"))
            return new Rule08();
        if(name.contains("rule09"))
            return new Rule09();
        if(name.contains("rule10"))
            return new Rule10();

        System.out.println("Error: Rule Factory can't create rule: " + name +"!");
        return null;
    }
}
