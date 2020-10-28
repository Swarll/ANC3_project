package ctrl;

import java.util.List;
import javafx.scene.control.ListView;
import model.Model;

public class Ctrl {
    
    private final Model model;
    
    public Ctrl(Model model)
    {
        this.model = model;
    }
    
    public List getToDoList()
    {
        return (this.model.getToDoList());
    }
    
    public List getDoneList()
    {
        return (this.model.getDoneList());
    }
    
    public void setDone(int index)
    {
        this.model.setDone(index);
    }
    
    public void setToDo(int index)
    {
        this.model.setToDo(index);
    }
    
    public boolean addToDo(String txt)
    {
        return (this.model.addToDo(txt));
    }
   
    public void transfer(ListView<String> fromList, ListView<String> toList, boolean ok) {
        final int index = fromList.getSelectionModel().getSelectedIndex();
        if (index != -1) {
            if (ok == true)
                model.setDone(index);
            else
                model.setToDo(index);
        }
    }
}
