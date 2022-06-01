package execute.implementation;

import execute.Execute;
import execute.data.ImportData;
import gui.MainFrame;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import resources.implementation.Entity;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class ImportExecute implements Execute {
    @Override
    public void execute(Object data) {
        ImportData importData = (ImportData) data;
            MainFrame.getInstance().getAppCore().getDatabase().insertIntoTable(importData.getEntity().getName(),importData.getColumns(),importData.getRows());
    }
}
