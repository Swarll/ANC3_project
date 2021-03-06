package model;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import model.InvalidTransferException;

public class Model {
    private final List<String> toDoList;
    private final List<String> doneList;
    
    
    public Model()
    {
        this.toDoList = new ArrayList<>();
        this.doneList = new ArrayList<>();
    }
    
    public Model(List<String> list)
    {
        int flag = 0;
        this.toDoList = new ArrayList<>();
        
        for (int i = 0; i < list.size(); ++i)
        {
            flag = 0;
            for (int j = 0; j < toDoList.size(); ++j)
            {
                if (list.get(i).compareTo(toDoList.get(j)) == 0)
                    flag = 1;
            }
            if (flag == 0 && stringNotEmpty(list.get(i)) == true && list.get(i).length() > 2)
                toDoList.add(list.get(i));
        }
        this.doneList = new ArrayList<>();
    }
    
    public List getToDoList()
    {
        return Collections.unmodifiableList(this.toDoList);
    }
    
    public List getDoneList()
    {
        return  Collections.unmodifiableList(this.doneList);
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
        if (stringNotEmpty(txt) == true && txt.length() > 2)
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
