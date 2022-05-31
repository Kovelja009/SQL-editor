package controller.actions;

import controller.AbstractButtonAction;

import java.awt.event.ActionEvent;

public class BulkImportBtn extends AbstractButtonAction {
    public BulkImportBtn(){
        putValue(NAME, "Bulk import");
        putValue(SHORT_DESCRIPTION, "Imports data from .csv file");
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        System.out.println("BuLk iMpoRT");
    }
}
