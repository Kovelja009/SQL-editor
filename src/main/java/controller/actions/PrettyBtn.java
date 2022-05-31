package controller.actions;

import controller.AbstractButtonAction;
import gui.MainFrame;
import interfaces.functionalities.Pretty;
import lombok.SneakyThrows;

import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
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
        Pretty.makePretty(str);
    }
}