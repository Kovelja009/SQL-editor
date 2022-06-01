package execute.implementation;

import execute.Execute;
import gui.CSVFileFilter;
import gui.MainFrame;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import resources.data.Row;

import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ExportExecute implements Execute {
    @Override
    public void execute(Object data) {
        List<Row> rows = (List<Row>)data;

        if(rows == null){
            System.out.println("Must have some rows first!");
            return;
        }

        JFileChooser jfc = new JFileChooser();
        jfc.setFileFilter(new CSVFileFilter());
        File csvFile = null;
        if(jfc.showSaveDialog(MainFrame.getInstance())==JFileChooser.APPROVE_OPTION){
            csvFile=jfc.getSelectedFile();
            if(!csvFile.getAbsolutePath().endsWith(".csv")){
                csvFile = new File(csvFile.getAbsolutePath()+".csv");
            }
        }else
            return;

        try (CSVPrinter printer = new CSVPrinter(new FileWriter(csvFile), CSVFormat.EXCEL)) {
            printer.printRecord(rows.get(0).getFields().keySet().toArray());
            for(Row row:rows)
                printer.printRecord(row.getFields().values());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
