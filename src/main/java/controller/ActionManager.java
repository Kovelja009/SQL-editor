package controller;

import controller.actions.BulkImport;
import controller.actions.Export;
import controller.actions.Pretty;
import controller.actions.Run;
import lombok.Getter;


@Getter
public class ActionManager {
    private BulkImport bulkImport;
    private Export export;
    private Run run;
    private Pretty pretty;

    public ActionManager(){
        bulkImport = new BulkImport();
        export = new Export();
        run = new Run();
        pretty = new Pretty();
    }
}
