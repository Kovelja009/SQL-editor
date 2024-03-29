package execute.data;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Statement {
    private String keyword;
    private String text;
    private int priority;
    private boolean hasSubquery;

    @Override
    public String toString() {
        return "keyword: " + keyword + " text: " + text + " priority: " + priority + " hasSubquery: " + hasSubquery;
    }
}
