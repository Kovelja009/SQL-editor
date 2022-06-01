package controller.actions;

import controller.AbstractButtonAction;
import gui.MainFrame;

import java.awt.event.ActionEvent;

public class ExportBtn extends AbstractButtonAction {
    public ExportBtn(){
        putValue(NAME, "Export");
        putValue(SHORT_DESCRIPTION, "Exports .csv file on selected location");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        MainFrame.getInstance().getAppCore().runTask(Task.EXPORT, MainFrame.getInstance().getAppCore().getTableModel().getRows());
    }
}
