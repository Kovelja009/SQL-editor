package app;

import execute.data.RunData;
import gui.MainFrame;

public class Main {
    public static void main(String[] args) {
        AppCore appCore = new AppCore();    // globalni model
        MainFrame mainFrame = MainFrame.getInstance();  // globalni view
        mainFrame.setAppCore(appCore);
//        new RunData("select nessst alij1, nesto as alij2 alij3  from ass as \"sdf asdf basdf\" where \"sdf asdf basdf\".baza = (select * from employee)");
//        new RunData("Select * from (select sa asdf asdff from asdf where as = asd) where ass Group by fd having sadf order by sdfa ");
//        new RunData("\t\tSELECT column_name(s)\n" +
//                "\tFROM table1\n" +
//                "\t\tRIGHT JOIN table2\n as fdsa asf basf " +
//                "\t\t\nON table1.column_name = table2.column_name;");
    }
}
// where ass sdf = (select * from employee)