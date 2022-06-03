package database;

import resources.DBNode;
import resources.data.Row;
import resources.enums.AttributeType;

import java.util.List;

public interface Repository {
    DBNode getSchema();

    List<Row> get(String from);

    boolean insert(String table, List<String> columns, List<List<String>> rows);

    public AttributeType getAttributeType(String table, String column);
}
