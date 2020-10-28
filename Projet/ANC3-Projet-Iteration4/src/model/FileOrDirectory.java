package model;

import java.util.Date;
import java.util.List;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.StringProperty;

public abstract class FileOrDirectory{
    protected Type type;
    protected String path;
    protected StringProperty name;
    protected Date date;
    protected IntegerProperty size;
    protected Directory parent;
    protected Correspondance corresp = Correspondance.ORPHAN;
    protected BooleanProperty selected = new SimpleBooleanProperty(true);
    
    //
    // GETTER AND SETTER
    //
    public String getName()
    {
        return (this.name.getValue());
    }
    
    public void setName(String name)
    {
        this.name.setValue(name);
    }
    
    public String getPath()
    {
        return (this.path);
    }
    
    public void addPath(String path)
    {
        this.path += path;
    }
    
    public String getType()
    {
        return (this.type.name());
    }
    
    public int getSize()
    {
        return (this.size.getValue());
    }
    
    public void setSize(int size)
    {
        this.size.setValue(size);
    }
    
    public ReadOnlyIntegerProperty sizeProperty()
    {
        return (this.size);
    }
    
    public Date getDate()
    {
        return (this.date);
    }
    
    abstract public void updateDate();
    
    public Directory getParentDirectory()
    {
        return (this.parent);
    }
    
    public void setParent(Directory p)
    {
        this.parent = p;
    }
    
    public void setCorresp(Correspondance c)
    {
        this.corresp = c;
    }
    
    public Correspondance getCorresp()
    {
        return (this.corresp);
    }
    
    public void setSelected(boolean s)
    {
        this.selected.setValue(s);
    }
    
    public boolean getSelected()
    {
        return (this.selected.getValue());
    }
    
     
    public ReadOnlyBooleanProperty selectedProperty()
    {
        return(this.selected);
    }
    
    public int getNbParent()
    {
        int i;
        Directory p;
        
        i = 0;
        if (this.getParentDirectory() != null)
        {
            p = (Directory) this.getParentDirectory();
            ++i;
            while (p.getParentDirectory() != null)
            {
                p = (Directory) p.getParentDirectory();
                ++i;
            }
        }
        return (i);
    }
    
    public Directory getLastParent()
    {
        Directory p = this.parent;
        if (p != null)
        {
            while (p.getParentDirectory() != null)
                p = p.getParentDirectory();
        }
        return (p);
    }
       
    //
    //File Or Direcotry
    //    
    public boolean isSimpleFile()
    {
        if (this instanceof SimpleFile)
        {
            return (true);
        }
        return (false);
    }
    
    public boolean isDirectory()
    {
        if (this instanceof Directory)
        {
            return (true);
        }
        return (false);
    }
    
    public boolean areEquals(FileOrDirectory fod)
    {
        return (this.name.getValue().equals(fod.name.getValue()) && this.path.equals(fod.path));
    }
    
    //
    //Pour methode toString
    //
    protected String addTab(int i)
    {
        String tab;
        
        tab = "";
        for (int k = 0; k < i; ++k)
        {
            tab += "    ";
        }
        return (tab);
    }
    
    abstract public List<FileOrDirectory> getChild();
    abstract public int updateSize();
    abstract String toString(int i);
}