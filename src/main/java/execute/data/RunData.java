package execute.data;

import lombok.Getter;

import java.util.List;

@Getter
public class RunData {
    private List<String> columns;

    public RunData(List<String> columns) {
        this.columns = columns;
    }
}
