package execute;

import controller.actions.Task;
import execute.implementation.ExportExecute;
import execute.implementation.ImportExecute;
import execute.implementation.PrettyExecute;
import execute.implementation.RunExecute;

public class ExecuteManager {
    Execute importExecute;
    Execute exportExecute;
    Execute runExecute;
    Execute prettyExecute;

    public ExecuteManager() {
        this.importExecute = new ImportExecute();
        this.exportExecute = new ExportExecute();
        this.runExecute = new RunExecute();
        this.prettyExecute = new PrettyExecute();
    }

    public Execute getExecute(Task task){
        if(task.equals(Task.RUN))
            return runExecute;
        if(task.equals(Task.IMPORT))
            return importExecute;
        if(task.equals(Task.EXPORT))
            return exportExecute;
        if(task.equals(Task.PRETTY))
            return prettyExecute;
        System.out.println("Invalid task in executor!");
        return null;
    }
}
