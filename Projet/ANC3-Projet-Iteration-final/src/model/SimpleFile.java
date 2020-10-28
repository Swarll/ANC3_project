package model;

import java.util.Date;
import java.util.List;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class SimpleFile extends FileOrDirectory{
    private String content;
    //
    //construct
    //
    public SimpleFile(String path, String name, String content, Date date, int size, Directory parent)
    {
        this.type = Type.F;
        this.path = path;
        this.name = new SimpleStringProperty(name);
        this.content = content;
        this.size = new SimpleIntegerProperty(size);
        this.date = date;
        this.parent = parent;
    }
    
    public SimpleFile(SimpleFile sf, String path, Directory p)
    {
        this.type = Type.F;
        this.path = path;
        this.name = new SimpleStringProperty(sf.getName());
        this.content = sf.content;
        this.size = new SimpleIntegerProperty(sf.getSize());
        this.date = new Date(sf.date.getTime());
        this.parent = p;
        this.content = sf.content;
        this.selected = sf.selected;
    }
    
    public String getContent()
    {
        return (this.content);
    }
    
    public void setContent(String content)
    {
        this.content = content;
    }
    
    @Override
    public List<FileOrDirectory> getChild()
    {
        throw new RuntimeException("Un fichier n a pas d enfants");
    }
    
    @Override
    public FileOrDirectory getNChild(int i)
    {
        throw new RuntimeException("Un fichier n a pas d enfants");
    }
    
    //****************************************************************************************************************************************
    //****************************************************************************************************************************************
    //ecrit le fichier :: toString()
    //****************************************************************************************************************************************
    //****************************************************************************************************************************************
    @Override
    public String toString()
    {
        if (this.getParentDirectory() != null)
            return (this.getParentDirectory().getName() + "   " + this.type + " : " + this.name.getValue() + "   " 
                    + this.size.getValue() + "   " + this.date + "   " + this.corresp+ " " + this.selected.getValue()) + " " + this.path;
        else
            return (this.type + " : " + this.name.getValue() + "   " + this.size.getValue() + "   " + this.date + "   "
                    + this.corresp + " " + this.selected.getValue()  + " " + this.path);
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
        Directory temp;
        
        temp = this.getLastParent();
        if (temp != null)
            temp.updateSize();
        return (size.getValue());
    }
    
    @Override
    public void updateDate()
    {
        Directory p;
        
        p = this.getLastParent();
        this.date = new Date();
            p.updateDate();
    }
}