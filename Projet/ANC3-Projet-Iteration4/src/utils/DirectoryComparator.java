package utils;

import model.Correspondance;
import model.Directory;
import model.FileOrDirectory;
import model.SimpleFile;

public class DirectoryComparator {
    //****************************************************************************************************************************************
    //****************************************************************************************************************************************
    //Compare 2 dossiers et tout leurs enfants et leur fourni leur correspondances respectives
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

        if (f.getParentDirectory() != null && tmp.getParentDirectory() != null &&f.getParentDirectory().getName().compareTo(tmp.getParentDirectory().getName()) == 0)
        {
            t1 = f.getParentDirectory();
            t2 = tmp.getParentDirectory();
            ++nb_parent_same;
            while (t1.getParentDirectory() != null && t2.getParentDirectory() != null && t1.getParentDirectory().getName().compareTo(t2.getParentDirectory().getName()) == 0)
            {
                t1 = t1.getParentDirectory();
                t2 = t2.getParentDirectory();
                //System.out.println(t1.getName() + " " + t2.getName());
                ++nb_parent_same;
            }
            if (((nb_parent_tmp == nb_parent_f && nb_parent_f == nb_parent_same + 1) || (nb_parent_tmp == nb_parent_f && nb_parent_f == nb_parent_same)) && f.getName().compareTo(tmp.getName()) == 0)
            {
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
            }
        }
    }
    
    //putState
    private void putState(Directory toCompare, Directory compareWith, int state[])
    {
        if (state[0] > 0 && state[1] == 0 && state[2] == 0 && state[3] == 0)
                {
                    toCompare.setCorresp(Correspondance.ORPHAN);
                    compareWith.setCorresp(Correspondance.ORPHAN);
                }
                else if (state[0] == 0 && state[1] == 0 && state[2] > 0 && state[3] == 0 && toCompare.getNbChild() == compareWith.getNbChild())
                {
                    toCompare.setCorresp(Correspondance.SAME);
                    compareWith.setCorresp(Correspondance.SAME);
                }
                else if (state[0] == 0 && state[1] > 0 && state[2] >= 0 && state[3] == 0 && toCompare.getNbChild() == compareWith.getNbChild())
                {
                    toCompare.setCorresp(Correspondance.OLDER);
                    compareWith.setCorresp(Correspondance.NEWER);
                }
                else if (state[0] == 0 && state[1] == 0 && state[2] >= 0 && state[3] > 0 && toCompare.getNbChild() == compareWith.getNbChild())
                {
                    toCompare.setCorresp(Correspondance.NEWER);
                    compareWith.setCorresp(Correspondance.OLDER);
                }
                else
                {
                    toCompare.setCorresp(Correspondance.PARTIAL_SAME);
                    compareWith.setCorresp(Correspondance.PARTIAL_SAME);
                } 
    }
    //increment
    private void increment(FileOrDirectory fod, int state[])
    {
        if (fod.getCorresp() == Correspondance.ORPHAN)
            ++state[0];
        else if (fod.getCorresp() == Correspondance.OLDER)
            ++state[1];
        else if (fod.getCorresp() == Correspondance.SAME)
            ++state[2];
        else
            ++state[3];
    }
    //checkState
    private void checkState(Directory toCompare, int state[])
    {
        for (int i = 0; i < toCompare.getChild().size(); ++i)
        {
            if (toCompare.getChild().get(i).isDirectory())
            {
                checkState((Directory)toCompare.getChild().get(i), state);
                increment(toCompare.getChild().get(i), state);
            }
            else
            {
                increment(toCompare.getChild().get(i), state);
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
        if (toCompare.getParentDirectory() != null && compareWith.getParentDirectory() != null && toCompare.getParentDirectory().getName().compareTo(compareWith.getParentDirectory().getName()) == 0)
        {
            t1 = toCompare.getParentDirectory();
            t2 = compareWith.getParentDirectory();
            ++nb_parent_s;
            while (t1.getParentDirectory() != null && t2.getParentDirectory() != null && t1.getParentDirectory().getName().compareTo(t2.getParentDirectory().getName()) == 0)
            {
                
                t1 = t1.getParentDirectory();
                t2 = t2.getParentDirectory();
                ++nb_parent_s;
            }
            if (((nb_parent_t == nb_parent_c && nb_parent_c == nb_parent_s + 1) || (nb_parent_t == nb_parent_c && nb_parent_c == nb_parent_s)) && toCompare.getName().compareTo(compareWith.getName()) == 0)
            {
                for (int i = 0; i < toCompare.getChild().size(); ++i)
                {
                    if (toCompare.getChild().get(i).isDirectory())
                    {
                        checkState((Directory)toCompare.getChild().get(i), state);
                        increment(toCompare.getChild().get(i), state);
                    }
                    else
                    {
                        increment(toCompare.getChild().get(i), state);
                    }
                }
                putState(toCompare, compareWith, state);
            }
        }
        else if (((toCompare.getNbParent() == 1 && compareWith.getNbParent() == 1) || (toCompare.getParentDirectory() == null && compareWith.getParentDirectory() == null))
                && toCompare.getName().compareTo(compareWith.getName()) == 0)
        {
            for (int i = 0; i < toCompare.getChild().size(); ++i)
            {
                if (toCompare.getChild().get(i).isDirectory())
                {
                    checkState((Directory)toCompare.getChild().get(i), state);
                    increment(toCompare.getChild().get(i), state);
                }
                else
                {
                    increment(toCompare.getChild().get(i), state);
                }
            }
            putState(toCompare, compareWith, state);
        }
    }
    
    //compare with all directory
    private void compareDirectory(Directory toCompare, Directory compareWith)
    {
        for (int i = 0; i < compareWith.getChild().size(); ++i)
        {
            if (compareWith.getChild().get(i).isDirectory())
            {
                compareDirectory(toCompare, (Directory)compareWith.getChild().get(i));
            }
        }
        getDirectoryState(toCompare, compareWith);
    }
    
    //get all Directory
    private void compareWith3(Directory toCompare, Directory compareWith)
    {
        for (int i = 0; i < toCompare.getChild().size(); ++i)
        {
            if (toCompare.getChild().get(i).isDirectory())
            {
                compareWith3((Directory)toCompare.getChild().get(i), compareWith);
            }
        }
        compareDirectory(toCompare, compareWith);
    }
    
    //compare with all file
    private void compareFile(SimpleFile f, Directory c)
    {
        for (int i = 0; i < c.getChild().size(); ++i)
        {
            if (c.getChild().get(i).isDirectory())
            {
                compareFile(f, (Directory)c.getChild().get(i));
            }
            else
            {
                getFileState(f, (SimpleFile)c.getChild().get(i));
            }
        }
    }
    
    //get all file
    private void compareWith2(Directory d, Directory c)
    {
        for (int i = 0; i < d.getChild().size(); ++i)
        {
            if (d.getChild().get(i).isDirectory())
            {
                compareWith2((Directory)d.getChild().get(i), c);
            }
            else
            {
                compareFile((SimpleFile) d.getChild().get(i), c);
            }
        }
    }
    
    public void compare(Directory d1, Directory d2)
    {
        for (int i = 0; i < d1.getChild().size(); ++i)
        {
            if (d1.getChild().get(i).isDirectory())
            {
                compareWith2((Directory)d1.getChild().get(i), d2);
            }
            else
            {
                compareFile((SimpleFile) d1.getChild().get(i), d2);
            }
        }
        for (int j = 0; j < d1.getChild().size(); ++j)
        {
            if (d1.getChild().get(j).isDirectory())
            {
                compareWith3((Directory)d1.getChild().get(j), d2);
                compareDirectory((Directory)d1.getChild().get(j), d2);
            }
        }
        getDirectoryState(d1, d2);
    }
    
    //****************************************************************************************************************************************
    //****************************************************************************************************************************************
    //Remet la correspondance du dossier et celle de tout ses enfants à l'état ORPHAN
    //****************************************************************************************************************************************
    //****************************************************************************************************************************************
    public void rebootCorresp(FileOrDirectory fod)
    {
        if (fod != null && fod instanceof Directory)
        {
            for (int i = 0; i < ((Directory)fod).getChild().size(); ++i)
            {
                rebootCorresp(((Directory)fod).getChild().get(i));
            }
        }
        fod.setCorresp(Correspondance.ORPHAN);
    }
}
