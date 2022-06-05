package database;

import resources.DBNode;
import resources.data.Row;
import resources.enums.AttributeType;

import java.util.List;

public interface Repository {
    DBNode getSchema();

    List<Row> get(String from);

    boolean insert(String table, List<String> columns, List<List<String>> rows);

    boolean isForeignKey(String table, String column, String table2, String column2);

    List<Row> selectQuery(String query);

    boolean execute(String query);
}
