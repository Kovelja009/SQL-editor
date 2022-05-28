package controller.actions;

import controller.AbstractButtonAction;

import java.awt.event.ActionEvent;

public class BulkImport extends AbstractButtonAction {
    public BulkImport(){
        putValue(NAME, "Bulk import");
        putValue(SHORT_DESCRIPTION, "Imports data from .csv file");
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        System.out.println("BuLk iMpoRT");
    }
}
