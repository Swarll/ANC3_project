package model;

import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Model {
    private final ObservableList<String> toDoList = FXCollections.observableArrayList();
    private final ObservableList<String> doneList = FXCollections.observableArrayList();
    private final static int MIN_WORD_LENGTH = 2;
   
    public Model(){}
    
    public Model(List<String> list)
    {
        int flag = 0;
        
        for (int i = 0; i < list.size(); ++i)
        {
            flag = 0;
            for (int j = 0; j < toDoList.size(); ++j)
            {
                if (list.get(i).compareTo(toDoList.get(j)) == 0)
                    flag = 1;
            }
            if (flag == 0 && stringNotEmpty(list.get(i)) == true && list.get(i).length() > MIN_WORD_LENGTH)
                toDoList.add(list.get(i));
        }
    }
    
    public ObservableList<String> getToDoList()
    {
        return FXCollections.unmodifiableObservableList(this.toDoList);
    }
    
    public ObservableList<String> getDoneList()
    {
        return  FXCollections.unmodifiableObservableList(this.doneList);
    }
    
    public void setDone(int nb) throws InvalidTransferException
    {
        if (nb < 0 || nb >= this.toDoList.size())
            throw new InvalidTransferException();
        this.doneList.add(this.toDoList.get(nb));
        this.toDoList.remove(nb);
    }
    
    public void setToDo(int nb) throws InvalidTransferException
    {
        if (nb < 0 || nb >= this.doneList.size())
            throw new InvalidTransferException();
        this.toDoList.add(this.doneList.get(nb));
        this.doneList.remove(nb);
    }
    
    public boolean addToDo(String txt)
    {
        if (txt == null)
            return (false);
        for (int i = 0; i < doneList.size(); ++i)
        {
            if (txt.compareTo(doneList.get(i)) == 0)
                return (false);
        }
        for (int i = 0; i < toDoList.size(); ++i)
        {
            if (txt.compareTo(toDoList.get(i)) == 0)
                return (false);
        }
        if (stringNotEmpty(txt) == true && txt.length() > MIN_WORD_LENGTH)
        {
            this.toDoList.add(txt);
            return (true);
        }
        return(false);
    }
    
    public boolean stringNotEmpty(String txt)
    {
        boolean flag = false;
        for (int i = 0; i < txt.length(); ++i)
        {
            if (txt.charAt(i) != ' ')
                flag = true;
        }
        return (flag);
    }
}
