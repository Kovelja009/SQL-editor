package gui;

import app.AppCore;
import controller.ActionManager;
import gui.other.CommandBar;
import lombok.Data;
import lombok.Getter;
import observer.Notification;
import observer.Subscriber;
import tree.implementation.SelectionListener;

import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;

@Data
@Getter
public class MainFrame extends JFrame implements Subscriber {

    private static MainFrame instance = null;

    private AppCore appCore;
    private ActionManager actionManager;
    private JTable jTable;
    private JScrollPane jsp;
    private JTree jTree;
    private JPanel left;
    private JPanel center;
    private JTextPane sqlEditor;
    private CommandBar commandBar;

    private MainFrame() {

    }

    public static MainFrame getInstance(){
        if (instance==null){
            instance=new MainFrame();
            instance.initialise();
        }
        return instance;
    }


    private void initialise() {

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("team_86");

        Toolkit kit = Toolkit.getDefaultToolkit();
        Image img = kit.getImage("src/main/java/images/icon_86.png");
        setIconImage(img);

        ///////////////////////////////////////////////////////////////////
        String text;

        //////////////////////////////////////////////////////////////////
        actionManager = new ActionManager();

        sqlEditor = new JTextPane();
        sqlEditor.setPreferredSize(new Dimension(400, 200));
        commandBar = new CommandBar();
        center = new JPanel();
        center.setLayout(new BorderLayout());
        center.add(commandBar, BorderLayout.NORTH);
        center.add(sqlEditor, BorderLayout.CENTER);

        jTable = new JTable();
        jTable.setGridColor(new Color(0x3100BC));
        jTable.setPreferredScrollableViewportSize(new Dimension(700, 200));
        jTable.setFillsViewportHeight(true);
        this.add(new JScrollPane(jTable), BorderLayout.SOUTH);
        this.add(center, BorderLayout.CENTER);

        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);


    }

    public void setAppCore(AppCore appCore) {
        this.appCore = appCore;
        this.appCore.addSubscriber(this);
        this.jTable.setModel(appCore.getTableModel());
        initialiseTree();
    }

    private void initialiseTree() {
        DefaultTreeModel defaultTreeModel = appCore.loadResource();
        jTree = new JTree(defaultTreeModel);
        jTree.addTreeSelectionListener(new SelectionListener());
        jsp = new JScrollPane(jTree, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED );
        left = new JPanel(new BorderLayout());
        left.add(jsp, BorderLayout.CENTER);
        left.setPreferredSize(new Dimension(200, 300));
        add(left, BorderLayout.WEST);
        pack();
    }


    @Override
    public void update(Notification notification) {
        jTable.setModel((TableModel) notification.getData());
    }
}
