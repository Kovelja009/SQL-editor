package database;

import resources.DBNode;
import resources.data.Row;

import java.util.List;

public interface Database {
    DBNode loadResource();

    List<Row> readDataFromTable(String tableName);
}
