package app;

import checker.Checker;
import controller.actions.Task;
import database.Database;
import database.DatabaseImplementation;
import database.MySQLRepo;
import database.settings.Settings;
import database.settings.SettingsImplementation;
import execute.ExecuteManager;
import execute.data.Keywords;
import execute.data.RunData;
import gui.table.TableModel;
import lombok.Getter;
import lombok.Setter;
import observer.Notification;
import observer.enums.NotificationCode;
import observer.implementation.PublisherImplementation;
import resources.DBNode;
import resources.DBNodeComposite;
import resources.enums.AttributeType;
import resources.implementation.Attribute;
import resources.implementation.InformationResource;
import tree.Tree;
import tree.TreeItem;
import tree.implementation.TreeImplementation;
import utils.Constants;


import javax.swing.tree.DefaultTreeModel;
import java.util.List;

@Getter
@Setter
public class AppCore extends PublisherImplementation {
    private Database database;
    private Settings settings;
    private TableModel tableModel;
    private DefaultTreeModel defaultTreeModel;
    private Tree tree;
    private Checker checker;
    private ExecuteManager executeManager;

    public AppCore() {
        this.settings = initSettings();
        this.database = new DatabaseImplementation(new MySQLRepo(this.settings));
        this.tableModel = new TableModel();
        this.tree = new TreeImplementation();
        this.checker = new Checker();
        this.executeManager = new ExecuteManager();
    }

    private Settings initSettings() {
        Settings settingsImplementation = new SettingsImplementation();
        settingsImplementation.addParameter("mysql_ip", Constants.MYSQL_IP);
        settingsImplementation.addParameter("mysql_database", Constants.MYSQL_DATABASE);
        settingsImplementation.addParameter("mysql_username", Constants.MYSQL_USERNAME);
        settingsImplementation.addParameter("mysql_password", Constants.MYSQL_PASSWORD);
        return settingsImplementation;
    }

    public DefaultTreeModel loadResource(){
        InformationResource ir = (InformationResource) this.database.loadResource();
        this.defaultTreeModel = this.tree.generateTree(ir);
        return this.defaultTreeModel;
    }

    public void readDataFromTable(String fromTable){

        tableModel.setRows(this.database.readDataFromTable(fromTable));

        //Zasto ova linija moze da ostane zakomentarisana?
        this.notifySubscribers(new Notification(NotificationCode.DATA_UPDATED, this.getTableModel()));
    }

    public void checkAndRunTask(Task task, Object data){
        if(!(boolean)checker.check(task, data))
            return;
        executeManager.getExecute(task).execute(data);
        this.notifySubscribers(new Notification(NotificationCode.DATA_UPDATED, this.getTableModel()));
    }

    public void execute(String query) {
        this.database.execute(query);
        this.notifySubscribers(new Notification(NotificationCode.DATA_UPDATED, this.getTableModel()));
    }

    public void runTaske(Task type, Object data) {
        Object o = getChecker().check(type, data);
        if(o instanceof Boolean) {
            boolean ok = (Boolean) o;
            if(!ok) return;
            System.out.println("Prosao CHECK");
            executeManager.getExecute(type).execute(data);
            this.notifySubscribers(new Notification(NotificationCode.DATA_UPDATED, this.getTableModel()));
            return;
        }
        execute(((RunData) data).rawQuery);
        this.notifySubscribers(new Notification(NotificationCode.DATA_UPDATED, this.getTableModel()));
    }

    public void runQuery(String query){
        tableModel.setRows(this.database.selectQuery(query));
        this.notifySubscribers(new Notification(NotificationCode.DATA_UPDATED, this.getTableModel()));
    }

    public void runTask(Task task,Object data){
        executeManager.getExecute(task).execute(data);
        this.notifySubscribers(new Notification(NotificationCode.DATA_UPDATED, this.getTableModel()));
    }

    public boolean existInDatabase(String table, String column, List<String> tablesForCheck)
    {
        if(column!=null)
            column = stripOfFunction(column);
        System.out.println("EXIST CHECK " + table+" "+column);
        if(column != null && column.equals("*"))
            return true;


        InformationResource ir = (InformationResource) ((TreeItem)defaultTreeModel.getRoot()).getDbNode();

        for(DBNode e: ir.getChildren())
            if(table==null || e.getName().equalsIgnoreCase(table))
            {
                if(column==null)
                    return true;
                for(DBNode c: ((DBNodeComposite)e).getChildren())
                    if(c.getName().equalsIgnoreCase(column.strip()))
                        return true;
            }
        if(table == null){
            boolean contains = false;
            for(String tblcheck : tablesForCheck){
                for(DBNode e: ir.getChildren()){
                    if(e.getName().equalsIgnoreCase(tblcheck)){
                        for(DBNode c: ((DBNodeComposite)e).getChildren()){
                            if(c.getName().equalsIgnoreCase(column))
                                return true;
                        }

                    }
                }
            }

        }
        return false;
    }

    public AttributeType getAttributeType(String table, String column)
    {
        if(column!=null)
            column = stripOfFunction(column);
        System.out.println("EXIST CHECK " + table+" "+column);
        if(column != null && column.equals("*"))
            return null;
        InformationResource ir = (InformationResource) ((TreeItem)defaultTreeModel.getRoot()).getDbNode();
        for(DBNode e: ir.getChildren())
            if(table==null || e.getName().equals(table))
            {
                if(column==null)
                    return null;
                for(DBNode c: ((DBNodeComposite)e).getChildren())
                    if(c.getName().equals(column))
                        return ((Attribute)c).getAttributeType();
            }
        return null;
    }

    public String stripOfFunction(String txt){

        for(String fun : Keywords.getInstance().getAggregateFunctions()){
            if(txt.indexOf(fun) == 0 && txt.charAt(txt.indexOf(fun) + fun.length()) == '('){
                int start = txt.indexOf("(") + 1;
                int end = txt.indexOf(")");
                return txt.substring(start,end);
            }
        }
        return txt;
    }

    public AttributeType attributeType(String column)
    {

        InformationResource ir = (InformationResource) ((TreeItem)defaultTreeModel.getRoot()).getDbNode();
        for(DBNode e: ir.getChildren())
        {
            for(DBNode c: ((DBNodeComposite)e).getChildren())
                if(c.getName().equals(column))
                    return ((Attribute)c).getAttributeType();
        }
        return null;
    }
}
