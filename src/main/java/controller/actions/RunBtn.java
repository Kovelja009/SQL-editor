package controller.actions;

import controller.AbstractButtonAction;

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

        System.out.println("RUnNiNg");
    }
}
