package controller.actions;

import controller.AbstractButtonAction;
import execute.data.ImportData;
import gui.MainFrame;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import resources.DBNode;
import resources.implementation.Entity;
import tree.TreeItem;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BulkImportBtn extends AbstractButtonAction {
    public BulkImportBtn(){
        putValue(NAME, "Bulk import");
        putValue(SHORT_DESCRIPTION, "Imports data from .csv file");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(MainFrame.getInstance().getJTree().getLastSelectedPathComponent()==null){
            System.out.println("Must select table first!");
            return;
        }
        TreeItem<DBNode> ti = (TreeItem<DBNode>) MainFrame.getInstance().getJTree().getLastSelectedPathComponent();
        if(ti == null ||  !(ti.getDbNode() instanceof Entity)){
            System.out.println("Must select Entity!");
            return;
        }

        JFileChooser jfc = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV files", "csv");
        jfc.setFileFilter(filter);
        FileReader in = null;
        if(jfc.showOpenDialog(MainFrame.getInstance())==JFileChooser.APPROVE_OPTION){
            try {
                in = new FileReader(jfc.getSelectedFile());
                // provera i izrvsavanje se desava ovde
                MainFrame.getInstance().getAppCore().checkAndRunTask(Task.IMPORT, createImportData(in, ti));
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        System.out.println("BuLk iMpoRT");
    }

    private ImportData createImportData(FileReader in, TreeItem<DBNode> ti) throws Exception{

        List<String> columns = new ArrayList<>();
        List<List<String>> rows = new ArrayList<>();
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(in);
        CSVRecord firstRow= records.iterator().next();
        for(int i=0;i<firstRow.size();i++)
            columns.add(firstRow.get(i));
        while(records.iterator().hasNext())
        {
            List<String> tmp = new ArrayList<>();
            CSVRecord row = records.iterator().next();
            for(int i=0;i<row.size();i++){
                if(row.get(i).equalsIgnoreCase(""))
                    tmp.add("NULL");
                else
                    tmp.add("\""+row.get(i)+"\"");
            }
            rows.add(tmp);
            ImportData importData = new ImportData((Entity) ti.getDbNode(),columns, rows);
        }
        return new ImportData((Entity) ti.getDbNode(),columns, rows);
    }
}
