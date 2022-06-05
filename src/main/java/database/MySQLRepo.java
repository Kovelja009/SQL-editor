package database;

import database.settings.Settings;
import org.apache.commons.lang3.tuple.MutablePair;
import resources.DBNode;
import resources.data.Row;
import resources.enums.AttributeType;
import resources.implementation.Attribute;
import resources.implementation.Entity;
import resources.implementation.InformationResource;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public class MySQLRepo implements Repository {
    private Settings settings;
    private Connection connection;
    private Map<MutablePair<String,String>, MutablePair<String,String>> foreignKeys;


    public MySQLRepo(Settings settings) {
        this.settings = settings;
    }

    private void initConnection() throws SQLException, ClassNotFoundException{
        String ip = (String) settings.getParameter("mysql_ip");
        String database = (String) settings.getParameter("mysql_database");
        String username = (String) settings.getParameter("mysql_username");
        String password = (String) settings.getParameter("mysql_password");
        //Class.forName("net.sourceforge.jtds.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://"+ip+"/"+database,username,password);


    }

    private void closeConnection(){
        try{
            connection.close();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        finally {
            connection = null;
        }
    }


    @Override
    public DBNode getSchema() {

        try{
            this.initConnection();

            DatabaseMetaData metaData = connection.getMetaData();
            InformationResource ir = new InformationResource("team_86");

            String tableType[] = {"TABLE"};
            ResultSet tables = metaData.getTables(connection.getCatalog(), null, null, tableType);
            foreignKeys = new HashMap<>();
            while (tables.next()){

                String tableName = tables.getString("TABLE_NAME");
                if(tableName.contains("trace"))continue;
                Entity newTable = new Entity(tableName, ir);
                ir.addChild(newTable);

                //Koje atribute imaja ova tabela?


                ResultSet rset = metaData.getImportedKeys(null, null, tableName);

                while(rset.next()){
                    String column_name = rset.getString("FKCOLUMN_NAME");
                    String pk_table = rset.getString("PKTABLE_NAME");
                    String pk_column = rset.getString("PKCOLUMN_NAME");
                    String constraint_name = rset.getString("PKCOLUMN_NAME");
//                    System.out.println("table "+ tableName+" column "+column_name+" reference to "+ pk_table+"("+constraint_name+")");
                    foreignKeys.put(new MutablePair<>(tableName,column_name),new MutablePair<>(pk_table,constraint_name));
                }
                rset.close();

                ResultSet columns = metaData.getColumns(connection.getCatalog(), null, tableName, null);

                while (columns.next()){

                    // COLUMN_NAME TYPE_NAME COLUMN_SIZE ....

                    String columnName = columns.getString("COLUMN_NAME");
                    String columnType = columns.getString("TYPE_NAME");


                    //System.out.println(columnType);

                    int columnSize = Integer.parseInt(columns.getString("COLUMN_SIZE"));

//                    ResultSet pkeys = metaData.getPrimaryKeys(connection.getCatalog(), null, tableName);
//
//                    while (pkeys.next()){
//                        String pkColumnName = pkeys.getString("COLUMN_NAME");
//                    }


                    Attribute attribute = new Attribute(columnName, newTable,
                            AttributeType.valueOf(
                                    Arrays.stream(columnType.toUpperCase().split(" "))
                                            .collect(Collectors.joining("_"))),
                            columnSize);
                    newTable.addChild(attribute);

                }





            }


            //TODO Ogranicenja nad kolonama? Relacije?


            return ir;
            //String isNullable = columns.getString("IS_NULLABLE");
            // ResultSet foreignKeys = metaData.getImportedKeys(connection.getCatalog(), null, table.getName());
            // ResultSet primaryKeys = metaData.getPrimaryKeys(connection.getCatalog(), null, table.getName());

        }
        catch (SQLException e1) {
            e1.printStackTrace();
        }
        catch (ClassNotFoundException e2){ e2.printStackTrace();}
        finally {
            this.closeConnection();
        }

        return null;
    }

    @Override
    public List<Row> selectQuery(String query) {

        List<Row> rows = new ArrayList<>();


        try{
            this.initConnection();

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet rs = preparedStatement.executeQuery();
            ResultSetMetaData resultSetMetaData = rs.getMetaData();

            while (rs.next()){

                Row row = new Row();

                for (int i = 1; i<=resultSetMetaData.getColumnCount(); i++){
                    row.addField(resultSetMetaData.getColumnName(i), rs.getString(i));
                }
                rows.add(row);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            this.closeConnection();
        }

        return rows;    }

    @Override
    public boolean execute(String query) {

        try{
            this.initConnection();

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            boolean rs = preparedStatement.execute();
            return rs;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            this.closeConnection();
        }
        return true;
    }
    @Override
    public List<Row> get(String from) {

        List<Row> rows = new ArrayList<>();


        try{
            this.initConnection();

            String query = "SELECT * FROM " + from;
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet rs = preparedStatement.executeQuery();
            ResultSetMetaData resultSetMetaData = rs.getMetaData();

            while (rs.next()){

                Row row = new Row();
                row.setName(from);

                for (int i = 1; i<=resultSetMetaData.getColumnCount(); i++){
                    row.addField(resultSetMetaData.getColumnName(i), rs.getString(i));
                }
                rows.add(row);


            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            this.closeConnection();
        }

        return rows;
    }

    @Override
    public boolean insert(String table, List<String> columns, List<List<String>> rows) {



        try{
            this.initConnection();
            String query="INSERT INTO "+table+"(";
            for (String column:columns) {
                query = query.concat(column);
                query = query.concat(",");
            }
            query = query.substring(0,query.length()-1);
            query=query.concat(")");
            query=query.concat(" VALUES ");
            for(List<String> row:rows) {
                String pom = "(";
                for (int i = 0; i < row.size() - 1; i++) {
                    pom=pom.concat("");
                    pom = pom.concat(row.get(i));
                    pom = pom.concat(",");
                }
                pom=pom.concat(""+row.get(row.size()-1));
                pom=pom.concat("), ");
                query=query.concat(pom);
            }
            query = query.substring(0,query.length()-2);
            query=query.concat(";");
            System.out.println(query);
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            return preparedStatement.execute();

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            System.out.println("UGASIO KONEKCIJU");
            this.closeConnection();
        }

        return false;
    }

    @Override
    public boolean isForeignKey(String table, String column, String table2, String column2) {
        MutablePair<String,String> key = new MutablePair<>(table,column);
        MutablePair<String,String> value = foreignKeys.get(key);
        System.out.println("ISFK1 "+table+" "+column);
        if(value==null)
            return false;
        System.out.println("ISFK "+table2+" "+column2);
        System.out.println("ISFK "+value.left+" "+value.right);
        return (value.left.equals(table2) && value.right.equals(column2));
    }
}
