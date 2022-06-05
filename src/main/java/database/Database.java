package database;

import resources.DBNode;
import resources.data.Row;
import resources.enums.AttributeType;

import java.util.List;

public interface Database {
    DBNode loadResource();

    List<Row> readDataFromTable(String tableName);

    boolean insertIntoTable(String table, List<String> columns, List<List<String>> rows);

    boolean isForeignKey(String table, String column, String table2, String column2);

    List<Row> selectQuery(String query);

    void execute(String query);
}
