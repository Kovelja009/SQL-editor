package controller.actions;

import controller.AbstractButtonAction;

import java.awt.event.ActionEvent;

public class Export extends AbstractButtonAction {
    public Export(){
        putValue(NAME, "Export");
        putValue(SHORT_DESCRIPTION, "Exports .csv file on selected location");
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        System.out.println("ExpoRTinG");
    }
}
