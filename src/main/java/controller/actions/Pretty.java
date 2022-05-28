package controller.actions;

import controller.AbstractButtonAction;

import java.awt.event.ActionEvent;

public class Pretty extends AbstractButtonAction {
    public Pretty(){
        putValue(NAME, "Pretty");
        putValue(SHORT_DESCRIPTION, "formats SQL query");
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        System.out.println("PreTtY");
    }
}
