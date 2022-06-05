package execute.implementation;

import execute.Execute;
import execute.data.RunData;
import gui.MainFrame;

public class RunExecute implements Execute {
    @Override
    public void execute(Object data) {
        System.out.println("Prosao sve provere");
        RunData runData = (RunData) data;
        MainFrame.getInstance().getAppCore().runQuery(runData.getQueryText());
    }
}
