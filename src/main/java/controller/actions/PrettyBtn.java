package controller.actions;

import controller.AbstractButtonAction;
import gui.MainFrame;
import lombok.SneakyThrows;

import java.awt.event.ActionEvent;

public class PrettyBtn extends AbstractButtonAction {
    public PrettyBtn(){
        putValue(NAME, "Pretty");
        putValue(SHORT_DESCRIPTION, "formats SQL query");
    }

    @SneakyThrows
    @Override
    public void actionPerformed(ActionEvent e) {
        String str = MainFrame.getInstance().getSqlEditor().getText();
        MainFrame.getInstance().getAppCore().runTask(Task.PRETTY, str);
    }
}
