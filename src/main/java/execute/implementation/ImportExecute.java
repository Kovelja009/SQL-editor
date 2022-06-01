package execute.implementation;

import execute.Execute;
import execute.data.ImportData;
import gui.MainFrame;

public class ImportExecute implements Execute {
    @Override
    public void execute(Object data) {
        ImportData importData = (ImportData) data;
        MainFrame.getInstance().getAppCore().getDatabase().insertIntoTable(importData.getEntity().getName(),importData.getColumns(),importData.getRows());
    }
}
