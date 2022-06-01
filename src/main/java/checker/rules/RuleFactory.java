package checker.rules;

public class RuleFactory {
    public static AbstractRule getRule(String name){
        if(name.contains("rule01"))
            return new Rule01();
        if(name.contains("rule02"))
            return new Rule02();
        if(name.contains("rule03"))
            return new Rule03();
        System.out.println("Error: Rule Factory can't create rule: " + name +"!");
        return null;
    }
}
