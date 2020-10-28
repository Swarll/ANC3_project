package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public abstract class FileOrDirectory{
    protected Type type;
    protected String path;
    protected String name;
    protected Date date;
    protected int size;
    protected Directory parent;
    protected Correspondance corresp = Correspondance.ORPHAN;
    
    //
    // GETTER AND SETTER
    //
    public String getName()
    {
        return (this.name);
    }
    
    public void setName(String name)
    {
        this.name = name;
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
        return (this.size);
    }
    
    public void setSize(int size)
    {
        this.size = size;
    }
    
    public Date getDate()
    {
        return (this.date);
    }
    
    public void updateDate()
    {
        this.date = new Date();
    }
    
    public Directory getParent()
    {
        return (this.parent);
    }
    
    public void setParent(Directory p)
    {
        this.parent = p;
    }
    
    public Correspondance getCorresp()
    {
        return (this.corresp);
    }
    
    public Directory getLastParent()
    {
        Directory p = this.parent;
        if (p != null)
        {
            while (p.getParent() != null)
                p = p.getParent();
        }
        return (p);
    }
    
    protected void setCorresp(Correspondance c)
    {
        this.corresp = c;
    }
    
    public int getNbParent()
    {
        int i;
        Directory p;
        
        i = 0;
        if (this.getParent() != null)
        {
            p = (Directory) this.getParent();
            ++i;
            while (p.getParent() != null)
            {
                p = (Directory) p.getParent();
                ++i;
            }
        }
        return (i);
    }
    
    abstract public List<FileOrDirectory> getChild();
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
        return (this.name.equals(fod.name) && this.path.equals(fod.path));
    }
    
    abstract public int updateSize();
    abstract String toString(int i);
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
    
    //
    //compareWith
    //
    abstract public void compareWith(Directory d);
    
    //
    //reinitialise l état
    //
    protected void rebootState(FileOrDirectory fod)
    {
        if (fod instanceof Directory)
        {
            for (int i = 0; i < ((Directory)fod).getChild().size(); ++i)
            {
                rebootState(((Directory)fod).getChild().get(i));
            }
        }
        fod.corresp = Correspondance.ORPHAN;
    }
}
