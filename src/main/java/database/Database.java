package database;

import resources.DBNode;
import resources.data.Row;
import resources.enums.AttributeType;

import java.util.List;

public interface Database {
    DBNode loadResource();

    List<Row> readDataFromTable(String tableName);

    boolean insertIntoTable(String table, List<String> columns, List<List<String>> rows);

    public AttributeType getAttributeType(String table, String column);
}
