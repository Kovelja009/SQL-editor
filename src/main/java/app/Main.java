package app;

import gui.MainFrame;
import interfaces.functionalities.Pretty;

public class Main {
    public static void main(String[] args) {
//        AppCore appCore = new AppCore();    // globalni model
//        MainFrame mainFrame = MainFrame.getInstance();  // globalni view
//        mainFrame.setAppCore(appCore);
        Pretty.makePretty("Select * from bla bla using(namesptace)");
    }
}
