package database;

import resources.DBNode;
import resources.data.Row;

import java.util.List;

public interface Repository {
    DBNode getSchema();

    List<Row> get(String from);
}
