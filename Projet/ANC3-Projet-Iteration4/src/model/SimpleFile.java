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
        throw new RuntimeException("pas d enfants ppur un fichier");
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
                    + this.size.getValue() + "   " + this.date + "   " + this.corresp+ " " + this.selected.getValue() );
        else
            return (this.type + " : " + this.name.getValue() + "   " + this.size.getValue() + "   " + this.date + "   "
                    + this.corresp + " " + this.selected.getValue());
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