package execute.data;

import lombok.Getter;
import resources.implementation.Entity;

import java.util.List;

@Getter
public class ImportData {
    private Entity entity;
    private List<String> columns;
    private List<List<String>> rows;

    public ImportData(Entity entity, List<String> columns, List<List<String>> rows) {
        this.entity = entity;
        this.columns = columns;
        this.rows = rows;
    }
}
