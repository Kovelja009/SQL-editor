package app;

import checker.Checker;
import controller.actions.Task;
import database.Database;
import database.DatabaseImplementation;
import database.MySQLRepo;
import database.settings.Settings;
import database.settings.SettingsImplementation;
import execute.ExecuteManager;
import gui.table.TableModel;
import lombok.Getter;
import lombok.Setter;
import observer.Notification;
import observer.enums.NotificationCode;
import observer.implementation.PublisherImplementation;
import resources.implementation.InformationResource;
import tree.Tree;
import tree.implementation.TreeImplementation;
import utils.Constants;


import javax.swing.tree.DefaultTreeModel;

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
        return this.tree.generateTree(ir);
    }

    public void readDataFromTable(String fromTable){

        tableModel.setRows(this.database.readDataFromTable(fromTable));

        //Zasto ova linija moze da ostane zakomentarisana?
        this.notifySubscribers(new Notification(NotificationCode.DATA_UPDATED, this.getTableModel()));
    }

    public void checkAndRunTask(Task task, Object data){
        if(!checker.check(task, data))
            return;
        executeManager.getExecute(task).execute(data);
    }

    public void runTask(Task task,Object data){
        executeManager.getExecute(task).execute(data);
    }
}
