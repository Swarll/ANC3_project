package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SimpleFile extends FileOrDirectory{
    
    //
    //construct
    //
    public SimpleFile(String path, String name, Date date, int size, Directory parent)
    {
        this.type = Type.F;
        this.path = path;
        this.name = name;
        this.size = size;
        this.date = date;
        this.parent = parent;
    }
    
    public List<FileOrDirectory> getChild()
    {
        throw new RuntimeException("pas d enfants ppur un fichier");
    }
    public void compareWith(Directory c)
    {
        throw new RuntimeException("non");
    }
    
    //****************************************************************************************************************************************
    //****************************************************************************************************************************************
    //ecrit le fichier :: toString()
    //****************************************************************************************************************************************
    //****************************************************************************************************************************************
    @Override
    public String toString()
    {
        if (this.getParent() != null)
            return (this.getParent().getName() + "   " + this.type + " : " + this.name + "   " + this.size + "   " + this.date + "   " + this.corresp);
        else
            return (this.type + " : " + this.name + "   " + this.size + "   " + this.date + "   " + this.corresp);
    }
    
    @Override
    public String toString(int i)
    {
        return (this.toString());
    }

    //****************************************************************************************************************************************
    //****************************************************************************************************************************************
    //met a jour la taille de tout l arboressence :: updateSize()
    //****************************************************************************************************************************************
    //****************************************************************************************************************************************
    @Override
    public int updateSize()
    {
        Directory temp = this.parent;
        
        temp = this.getLastParent();
        if (temp != null)
            temp.updateSize();
        return (size);
    }
}