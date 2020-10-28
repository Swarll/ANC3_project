package model;

import java.util.Date;
import java.util.List;
import javafx.scene.control.TreeItem;

public abstract class FileOrDirectory{
    protected Type type;
    protected String path;
    protected String name;
    protected Date date;
    protected int size;
    protected Directory parent;
    protected Correspondance corresp = Correspondance.ORPHAN;
    protected Boolean selected = true;
    
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
    
    public boolean getSelected()
    {
        return (this.selected);
    }
    
    public void setSelected(boolean s)
    {
        this.selected = s;
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
    public void rebootState(FileOrDirectory fod)
    {
        if (fod != null && fod instanceof Directory)
        {
            for (int i = 0; i < ((Directory)fod).getChild().size(); ++i)
            {
                rebootState(((Directory)fod).getChild().get(i));
            }
        }
        fod.corresp = Correspondance.ORPHAN;
    }
    
    //
    //reinitialise la sélection à true
    //
    
    public void selectAll(FileOrDirectory fod)
    {
        if (fod != null && fod instanceof Directory)
        {
            for (int i = 0; i < ((Directory)fod).getChild().size(); ++i)
            {
                selectAll(((Directory)fod).getChild().get(i));
            }
        }
        fod.selected = true;
    }
    
    public void selectNan(FileOrDirectory fod)
    {
        if (fod != null && fod instanceof Directory)
        {
            for (int i = 0; i < ((Directory)fod).getChild().size(); ++i)
            {
                selectNan(((Directory)fod).getChild().get(i));
            }
        }
        fod.selected = false;
    }
    
    private static boolean checkChild(FileOrDirectory fod, Correspondance c)
    {
        if (fod != null && fod instanceof Directory)
        {
            for (int i = 0; i < ((Directory)fod).getChild().size(); ++i)
            {
                checkChild(((Directory)fod).getChild().get(i), c);
            }
        }
        return (fod.selected == true ? (true) : (false));
    }
    
    public void select(FileOrDirectory fod, Correspondance c)
    {
        if (fod != null && fod instanceof Directory)
        {
            for (int i = 0; i < ((Directory)fod).getChild().size(); ++i)
            {
                select(((Directory)fod).getChild().get(i), c);
            }
        }
        if (fod.corresp == c)
            fod.selected = true;
        if (fod != null && fod instanceof Directory)
        {
            for (int i = 0; i < ((Directory)fod).getChild().size(); ++i)
            {
                 if (checkChild(((Directory)fod).getChild().get(i), c) == true)
                     fod.selected = true;
            }
        }
    }
    
    public void unselect(FileOrDirectory fod, Correspondance c)
    {
        
        if (fod != null && fod instanceof Directory)
        {
            for (int i = 0; i < ((Directory)fod).getChild().size(); ++i)
            {
                unselect(((Directory)fod).getChild().get(i), c);
            }
        }
        fod.selected = (fod.corresp == c ? (true) : (false));
    }
    
    public void selectFolder(FileOrDirectory fod)
    {
        if (fod != null && fod instanceof Directory)
        {
            for (int i = 0; i < ((Directory)fod).getChild().size(); ++i)
            {
                selectFolder(((Directory)fod).getChild().get(i));
            }
        }
        fod.selected = (fod instanceof Directory ? (true) : false);
    }
}