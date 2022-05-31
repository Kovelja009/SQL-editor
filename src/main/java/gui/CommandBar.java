package gui;

import javax.swing.*;
import java.awt.*;

public class CommandBar extends JToolBar {
    public CommandBar(){
        super(SwingConstants.HORIZONTAL);
        setFloatable(false);
        setPreferredSize(new Dimension(100, 35));

        addSeparator(new Dimension(250,50));
        add(MainFrame.getInstance().getActionManager().getBulkImportBtn());
        addSeparator(new Dimension(5,50));
        add(MainFrame.getInstance().getActionManager().getPrettyBtn());
        addSeparator(new Dimension(5,50));
        add(MainFrame.getInstance().getActionManager().getExportBtn());
        addSeparator(new Dimension(15,50));
        add(MainFrame.getInstance().getActionManager().getRunBtn());

    }
}
