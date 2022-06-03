package database;

import lombok.AllArgsConstructor;
import lombok.Data;
import resources.DBNode;
import resources.data.Row;
import resources.enums.AttributeType;

import java.util.List;

@Data
@AllArgsConstructor

public class DatabaseImplementation implements Database{
    private Repository repository;


    @Override
    public DBNode loadResource() {
        return repository.getSchema();
    }

    @Override
    public List<Row> readDataFromTable(String tableName) {
        return repository.get(tableName);
    }

    @Override
    public boolean insertIntoTable(String table, List<String> columns, List<List<String>> rows) {
        return  repository.insert(table,columns,rows);
    }

    public AttributeType getAttributeType(String table, String column){return repository.getAttributeType(table, column);}
}
