package model;

import java.util.ArrayList;
import java.util.List;

public class Filter {
    private final List<Correspondance> filterListD1 = new ArrayList<>();
    private final List<Correspondance> filterListD2 = new ArrayList<>();
    private boolean folder_only = false;
    private boolean all = true;
    
    public Filter()
    {
        filterListD1.add(Correspondance.SAME);
        filterListD1.add(Correspondance.NEWER);
        filterListD1.add(Correspondance.OLDER);
        filterListD1.add(Correspondance.ORPHAN);
        filterListD2.addAll(filterListD1);
    }
    
    public List<Correspondance> getFilterListD1()
    {
        return (this.filterListD1);
    }
    
    public List<Correspondance> getFilterListD2()
    {
        return (this.filterListD2);
    }
    
    public void setFolderOnly(boolean value)
    {
        this.folder_only = value;
    }
    
    public boolean getFolderOnly()
    {
        return (this.folder_only);
    }
    
    //
    //Ajoute tout les filtre a la liste
    //
    public void addAllToFilterList(List<Correspondance> l)
    {
        removeAllFromFilterList(l);
        l.add(Correspondance.SAME);
        l.add(Correspondance.NEWER);
        l.add(Correspondance.OLDER);
        l.add(Correspondance.ORPHAN);
        all = true;
    }
    
    //
    //Enleve tout les filtres des liste
    //
    public void removeAllFromFilterList(List<Correspondance> l)
    {
        l.clear();
        all = false;
    }
    
    //
    //Ajoute un filtre a la liste si il n'est pas dedans
    //
    public void addCorrespToFilterList(List<Correspondance> l, Correspondance c)
    {
        if (l.size() == 4)
            removeAllFromFilterList(l);
        if (!(l.contains(c)))
            l.add(c);
    }
    
    //
    //Retire un filtre de la liste si il est dedans
    //
    public void removeCorrespFromFilerList(List<Correspondance> l, Correspondance c)
    {
        if (l.contains(c))
            l.remove(c);
        if (all)
            l.add(c);
    }
  
    
    //****************************************************************************************************************************************
    //****************************************************************************************************************************************
    //Selectionne les dossiers et fichiers en fonction des filtres
    //****************************************************************************************************************************************
    //****************************************************************************************************************************************
    public void select(FileOrDirectory fod, List<Correspondance> l, boolean folder_only)
    {
        selectNan(fod);
        selectByFilter(fod, l);
        selectFolder(fod, folder_only);
    }
    
    private void selectNan(FileOrDirectory fod)
    {
        if (fod != null && fod instanceof Directory)
        {
            for (int i = 0; i < ((Directory)fod).getChild().size(); ++i)
            {
                selectNan(((Directory)fod).getChild().get(i));
            }
        }
        fod.selected.setValue(false);
    }
    
    private void selectByFilter(FileOrDirectory fod, List<Correspondance> l)
    {
        if (fod.isDirectory())
        {
            for (int i = 0; i < fod.getChild().size(); ++i)
            {
                selectByFilter(fod.getChild().get(i), l);
            }
            if (checkCorresp(fod, l))
                fod.setSelected(true);
            if (checkCorrespFromChild(fod, l))
                fod.setSelected(true);
        }
        else
        {
            if (checkCorresp(fod, l))
                fod.setSelected(true);
        }       
    }

    private void selectFolder(FileOrDirectory fod, boolean folder_only)
    {
        if (fod != null && fod instanceof Directory)
        {
            for (int i = 0; i < ((Directory)fod).getChild().size(); ++i)
            {
                selectFolder(((Directory)fod).getChild().get(i), folder_only);
            }
        }
        if (folder_only)
        {
            if (fod.selected.getValue())
                fod.selected.setValue(fod instanceof Directory ? (true) : false);
        }
    }
    
    //
    //Fonctions de selection
    //
    private boolean checkCorrespFromChild(FileOrDirectory fod, List<Correspondance> l)
    {
        if (fod.isDirectory())
        {
            for (int i = 0; i < fod.getChild().size(); ++i)
            {
                if (checkCorrespFromChild(fod.getChild().get(i), l))
                    return (true);
            }
            if (checkCorresp(fod, l))
                return (true);
            else
                return (false);
        }
        else
        {
            if (checkCorresp(fod, l))
                return (true);
            else
                return (false);
        }
    }
    
    private boolean checkCorresp(FileOrDirectory fod, List<Correspondance> l)
    {
        for (int i = 0; i < l.size(); ++i)
        {
            if (fod.getCorresp() == l.get(i))             
                return (true);
        }
        return (false);
    }
}
