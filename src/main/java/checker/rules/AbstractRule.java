package checker.rules;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractRule {

    private String name;
    private String suggestion;
    private String error;

    public abstract boolean checkRule(Object data);

}
