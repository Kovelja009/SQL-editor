package execute.implementation;

import controller.actions.Task;
import execute.Execute;
import gui.MainFrame;

public class RunExecute implements Execute {
    @Override
    public void execute(Object data) {
        MainFrame.getInstance().getAppCore().runTask(Task.RUN, "Select * from lolcina");

    }
}
