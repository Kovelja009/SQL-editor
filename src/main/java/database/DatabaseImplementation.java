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

    @Override
    public boolean isForeignKey(String table, String column, String table2, String column2)
    {
        return repository.isForeignKey(table,column,table2,column2);
    }

    @Override
    public List<Row> selectQuery(String query) {
        return this.repository.selectQuery(query);
    }

    public void execute(String query) {
        this.repository.execute(query);
    }
}
