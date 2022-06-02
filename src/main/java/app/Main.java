package app;

import execute.data.RunData;
import gui.MainFrame;

public class Main {
    public static void main(String[] args) {
//        AppCore appCore = new AppCore();    // globalni model
//        MainFrame mainFrame = MainFrame.getInstance();  // globalni view
//        mainFrame.setAppCore(appCore);

        new RunData("Select * from (select * from asdf where as = asd) where a Group by fd having sadf order by sdfa");
    }
}
