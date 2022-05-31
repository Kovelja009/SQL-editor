package controller;

import controller.actions.BulkImportBtn;
import controller.actions.ExportBtn;
import controller.actions.PrettyBtn;
import controller.actions.RunBtn;
import lombok.Getter;


@Getter
public class ActionManager {
    private BulkImportBtn bulkImportBtn;
    private ExportBtn exportBtn;
    private RunBtn runBtn;
    private PrettyBtn prettyBtn;

    public ActionManager(){
        bulkImportBtn = new BulkImportBtn();
        exportBtn = new ExportBtn();
        runBtn = new RunBtn();
        prettyBtn = new PrettyBtn();
    }
}
