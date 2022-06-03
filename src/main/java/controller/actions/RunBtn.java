package controller.actions;

import controller.AbstractButtonAction;
import execute.data.RunData;
import gui.MainFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class RunBtn extends AbstractButtonAction {
    public RunBtn(){
        putValue(NAME, "Run");
        putValue(SHORT_DESCRIPTION, "Runs SQL query");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        MainFrame.getInstance().getAppCore().checkAndRunTask(Task.RUN, new RunData(MainFrame.getInstance().getSqlEditor().getText()));
    }
}
