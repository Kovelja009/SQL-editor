package tree.implementation;

import gui.MainFrame;
import resources.implementation.Entity;
import tree.TreeItem;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

public class SelectionListener implements TreeSelectionListener {
    @Override
    public void valueChanged(TreeSelectionEvent e) {

        JTree tree = (JTree) e.getSource();
        TreeItem node = (TreeItem) tree.getLastSelectedPathComponent();
        /* if nothing is selected */
        if (node == null || !(node.getDbNode() instanceof Entity)) return;

        MainFrame.getInstance().getAppCore().readDataFromTable(node.getName());


    }
}
