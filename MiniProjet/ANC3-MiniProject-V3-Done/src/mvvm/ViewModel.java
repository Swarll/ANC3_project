package mvvm;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.ListView;
import model.Model;

public class ViewModel {
    
    private final StringProperty addText = new SimpleStringProperty();    
    private final BooleanProperty setToDoButton = new SimpleBooleanProperty(false);
    private final BooleanProperty setDoneButton = new SimpleBooleanProperty(false);
    private final BooleanProperty addButton = new SimpleBooleanProperty(false);
    private final IntegerProperty indexToDo = new SimpleIntegerProperty(-1);
    private final IntegerProperty indexDone = new SimpleIntegerProperty(-1);

    private final Model model;
    
    public ViewModel(Model model)
    {
        this.model = model;
       
    }
    
    public SimpleListProperty<String> ToDoListProperty() {
        return new SimpleListProperty<>(model.getToDoList());
    }
    
    public SimpleListProperty<String> DoneListProperty() {
        return new SimpleListProperty<>(model.getDoneList());
    }
    
    public BooleanProperty setDoneProperty()
    {
        return setDoneButton;
    }
    
    public BooleanProperty setToDoProperty()
    {
        return setToDoButton;
    }
    
    public BooleanProperty addButtonProperty()
    {
        return addButton;
    }
    
    public StringProperty addTextProperty()
    {
        return addText;
    }
   
    public IntegerProperty indexToDoProperty(){
        return indexToDo;
    }
   
    public IntegerProperty indexDoneProperty(){
        return indexDone;
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
