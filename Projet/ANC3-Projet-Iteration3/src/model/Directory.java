package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Directory extends FileOrDirectory{
    private final List<FileOrDirectory> child = new ArrayList<>();
    
    //
    // Construct
    //
    public Directory(String path, String name, Date date, int size, Directory parent)
    {
        this.type = Type.D;
        this.path = path;
        this.name = name;
        this.size = size;
        this.date = date;
        this.parent = parent;
    }
    
    @Override
    public List<FileOrDirectory> getChild()
    {
        return (this.child);
    }
    
    //****************************************************************************************************************************************
    //****************************************************************************************************************************************
    // ecrit le dossier et tout ses dossiers et fichiers enfant
    //****************************************************************************************************************************************
    //****************************************************************************************************************************************
    @Override
    String toString(int i)
    {
        String str = "";
        
        ++i;
        for (int count = 0; count  < this.child.size(); ++count)
        {
            if (this.child.get(count) instanceof SimpleFile)
            {
                str += "\n" + addTab(i) + this.child.get(count).toString(i);
            }
            else
                str += "\n" + addTab(i) + this.child.get(count).toString(i);
        }
        return (this.parent.getName() + "   " +this.type + " : " + this.name + "   " 
                + this.size + "   " + this.date + "   " + this.corresp + "   " + this.selected + " " +str);
    }
    
    @Override
    public String toString()
    {
        String str = "";
        int count = 0;
        
        while (count < this.child.size())
        {
            if (this.child.get(count) instanceof SimpleFile)
            {
                str += "\n" + addTab(1) + this.child.get(count).toString(1);
            }
            else
                str += "\n" + addTab(1) + this.child.get(count).toString(1);
            ++count;
        }
        return (this.type + " : " + this.name + "   " 
                + this.size + "   " + this.date + "   " + this.corresp + "   " + this.selected + " "+str);
    }
    //****************************************************************************************************************************************
    //****************************************************************************************************************************************
    //met a jour la taille du dossier et tout les dossier en dessous de lui
    //****************************************************************************************************************************************
    //****************************************************************************************************************************************
    @Override
    public int updateSize()
    {
        int     new_size;
        
        new_size = 0;
        for(int i = 0; i < this.child.size(); ++i)
        {
            if (this.child.get(i).isDirectory())
                new_size += this.child.get(i).updateSize();
            else
                new_size += this.child.get(i).getSize();
        }
        this.size = new_size;
        return (new_size);
    }
    //****************************************************************************************************************************************
    //****************************************************************************************************************************************
    //return le premier fichier trouver, null si le dossier ne contient aucun fichier
    //****************************************************************************************************************************************
    //****************************************************************************************************************************************
    private SimpleFile getFirstFile2(Directory d)
    {
        for (int i = 0; i < d.child.size(); ++i)
        {
            if (d.child.get(i) instanceof Directory)
            {
                return (getFirstFile2((Directory) d.child.get(i)));
            }
            else
                return ((SimpleFile) this.child.get(i));
        }
        return (null);
    }
    public SimpleFile getFirstFile()
    {
        for (int i = 0; i < this.child.size(); ++i)
        {
            if (this.child.get(i) instanceof Directory)
            {
                return (getFirstFile2((Directory) this.child.get(i)));
            }
            else
                return ((SimpleFile) this.child.get(i));
        }
        return (null);
    }
    
    //****************************************************************************************************************************************
    //****************************************************************************************************************************************
    //Se compare à un autre dossier et retranscrit leur état l un par apport a l autre :: compareWith(Directory d)
    //****************************************************************************************************************************************
    //****************************************************************************************************************************************
    
    //getState 
    private void getFileState(SimpleFile f, SimpleFile tmp)
    {
        int nb_parent_same, nb_parent_f, nb_parent_tmp;
        Directory t1, t2;
        
        nb_parent_same = 0;
        nb_parent_f = f.getNbParent();
        nb_parent_tmp = tmp.getNbParent();

        if (f.getParent() != null && tmp.getParent() != null &&f.getParent().getName().compareTo(tmp.getParent().getName()) == 0)
        {
            t1 = f.getParent();
            t2 = tmp.getParent();
            ++nb_parent_same;
            while (t1.getParent() != null && t2.getParent() != null && t1.getParent().getName().compareTo(t2.getParent().getName()) == 0)
            {
                t1 = t1.getParent();
                t2 = t2.getParent();
                //System.out.println(t1.getName() + " " + t2.getName());
                ++nb_parent_same;
            }
            if (((nb_parent_tmp == nb_parent_f && nb_parent_f == nb_parent_same + 1) || (nb_parent_tmp == nb_parent_f && nb_parent_f == nb_parent_same)) && f.getName().compareTo(tmp.getName()) == 0)
            {
                //System.out.println("**************************************************************************************************");
                //System.out.print("F " + f.getLastParent().getName() + " " + f.getName() + " " + f.getDate() + " " + tmp.getLastParent().getName() + " " + tmp.getName() + " " + tmp.getDate() + " "  + nb_parent_same + " " + nb_parent_f + " " + nb_parent_tmp + " ");
                if (f.getDate().compareTo(tmp.getDate()) == 0)
                {
                    f.setCorresp(Correspondance.SAME);
                    tmp.setCorresp(Correspondance.SAME);
                }
                else if (f.getDate().compareTo(tmp.getDate()) < 0)
                {
                    f.setCorresp(Correspondance.OLDER);
                    tmp.setCorresp(Correspondance.NEWER);
                }
                else
                {
                    f.setCorresp(Correspondance.NEWER);
                    tmp.setCorresp(Correspondance.OLDER);
                }
                //System.out.println(f.getCorresp() + " " + tmp.getCorresp());
                //System.out.println("**************************************************************************************************");
            }
        }
    }
    
    //putState
    private void putState(Directory toCompare, Directory compareWith, int state[])
    {
        if (state[0] > 0 && state[1] == 0 && state[2] == 0 && state[3] == 0)
                {
                    toCompare.corresp = Correspondance.ORPHAN;
                    compareWith.corresp = Correspondance.ORPHAN;
                }
                else if (state[0] == 0 && state[1] == 0 && state[2] > 0 && state[3] == 0)
                {
                    toCompare.corresp = Correspondance.SAME;
                    compareWith.corresp = Correspondance.SAME;
                }
                else if (state[0] == 0 && state[1] > 0 && state[2] >= 0 && state[3] == 0)
                {
                    toCompare.corresp = Correspondance.OLDER;
                    compareWith.corresp = Correspondance.NEWER;
                }
                else if (state[0] == 0 && state[1] == 0 && state[2] >= 0 && state[3] > 0)
                {
                    toCompare.corresp = Correspondance.NEWER;
                    compareWith.corresp = Correspondance.OLDER;
                }
                else
                {
                    toCompare.corresp = Correspondance.PARTIAL_SAME;
                    compareWith.corresp = Correspondance.PARTIAL_SAME;
                } 
    }
    //increment
    private void increment(FileOrDirectory fod, int state[])
    {
        if (fod.corresp == Correspondance.ORPHAN)
            ++state[0];
        else if (fod.corresp == Correspondance.OLDER)
            ++state[1];
        else if (fod.corresp == Correspondance.SAME)
            ++state[2];
        else
            ++state[3];
    }
    //checkState
    private void checkState(Directory toCompare, int state[])
    {
        for (int i = 0; i < toCompare.child.size(); ++i)
        {
            if (toCompare.child.get(i).isDirectory())
            {
                checkState((Directory)toCompare.child.get(i), state);
                increment(toCompare.child.get(i), state);
            }
            else
            {
                increment(toCompare.child.get(i), state);
            }
        }
    }
    
    //get Directory State
    private void getDirectoryState(Directory toCompare, Directory compareWith)
    {
        int nb_parent_t, nb_parent_c, nb_parent_s;
        int state[] = new int[4];
        Directory t1, t2;
        
        nb_parent_t = toCompare.getNbParent();
        nb_parent_c = compareWith.getNbParent();
        nb_parent_s = 0;
        if (toCompare.getParent() != null && compareWith.getParent() != null && toCompare.getParent().getName().compareTo(compareWith.getParent().getName()) == 0)
        {
            t1 = toCompare.getParent();
            t2 = compareWith.getParent();
            ++nb_parent_s;
            while (t1.getParent() != null && t2.getParent() != null && t1.getParent().getName().compareTo(t2.getParent().getName()) == 0)
            {
                
                t1 = t1.getParent();
                t2 = t2.getParent();
                ++nb_parent_s;
            }
            if (((nb_parent_t == nb_parent_c && nb_parent_c == nb_parent_s + 1) || (nb_parent_t == nb_parent_c && nb_parent_c == nb_parent_s)) && toCompare.getName().compareTo(compareWith.getName()) == 0)
            {
                for (int i = 0; i < toCompare.child.size(); ++i)
                {
                    if (toCompare.child.get(i).isDirectory())
                    {
                        checkState((Directory)toCompare.child.get(i), state);
                        increment(toCompare.child.get(i), state);
                    }
                    else
                    {
                        increment(toCompare.child.get(i), state);
                    }
                }
                //System.out.println("**************************************************************************************************");
                //System.out.print("D " + toCompare.getLastParent().getName() + " " + toCompare.getName() + " " + toCompare.getDate() + " " + compareWith.getLastParent().getName() + " " +  compareWith.getName() + " " + compareWith.getDate() + " ");
                putState(toCompare, compareWith, state);
                //System.out.println(toCompare.getCorresp() + " " + compareWith.getCorresp());
                //System.out.println("**************************************************************************************************");
            }
        }
        else if (((toCompare.getNbParent() == 1 && compareWith.getNbParent() == 1) || (toCompare.getParent() == null && compareWith.getParent() == null)) && toCompare.getName().compareTo(compareWith.getName()) == 0)
        {
            for (int i = 0; i < toCompare.child.size(); ++i)
            {
                if (toCompare.child.get(i).isDirectory())
                {
                    checkState((Directory)toCompare.child.get(i), state);
                    increment(toCompare.child.get(i), state);
                }
                else
                {
                    increment(toCompare.child.get(i), state);
                }
            }
            //System.out.println("**************************************************************************************************");
            //System.out.print("D " + toCompare.getLastParent().getName() + " " + toCompare.getName() + " " + toCompare.getDate() + " " + compareWith.getLastParent().getName() + " " +  compareWith.getName() + " " + compareWith.getDate() + " ");
            putState(toCompare, compareWith, state);
            //System.out.println(toCompare.getCorresp() + " " + compareWith.getCorresp());
            //System.out.println("**************************************************************************************************");
        }
    }
    
    //compare with all directory
    private void compareDirectory(Directory toCompare, Directory compareWith)
    {
        for (int i = 0; i < compareWith.child.size(); ++i)
        {
            if (compareWith.child.get(i).isDirectory())
            {
                compareDirectory(toCompare, (Directory)compareWith.child.get(i));
            }
        }
        getDirectoryState(toCompare, compareWith);
    }
    
    //get all Directory
    private void compareWith3(Directory toCompare, Directory compareWith)
    {
        for (int i = 0; i < toCompare.child.size(); ++i)
        {
            if (toCompare.child.get(i).isDirectory())
            {
                compareWith3((Directory)toCompare.child.get(i), compareWith);
            }
        }
        compareDirectory(toCompare, compareWith);
    }
    
    //compare with all file
    private void compareFile(SimpleFile f, Directory c)
    {
        for (int i = 0; i < c.child.size(); ++i)
        {
            if (c.child.get(i).isDirectory())
            {
                compareFile(f, (Directory)c.child.get(i));
            }
            else
            {
                getFileState(f, (SimpleFile)c.child.get(i));
            }
        }
    }
    
    //get all file
    private void compareWith2(Directory d, Directory c)
    {
        for (int i = 0; i < d.child.size(); ++i)
        {
            if (d.child.get(i).isDirectory())
            {
                compareWith2((Directory)d.child.get(i), c);
            }
            else
            {
                compareFile((SimpleFile) d.child.get(i), c);
            }
        }
    }
    
    public void compareWith(Directory c)
    {
        for (int i = 0; i < this.child.size(); ++i)
        {
            if (this.child.get(i).isDirectory())
            {
                compareWith2((Directory)this.child.get(i), c);
            }
            else
            {
                compareFile((SimpleFile) this.child.get(i), c);
            }
        }
        for (int j = 0; j < this.child.size(); ++j)
        {
            if (this.child.get(j).isDirectory())
            {
                compareWith3((Directory)this.child.get(j), c);
                compareDirectory((Directory)this.child.get(j), c);
            }
        }
        getDirectoryState(this, c);
        System.out.println(this.toString());
        System.out.println("\n");
        System.out.println(c.toString());
//        rebootState(this);
//        rebootState(c);
    }
}