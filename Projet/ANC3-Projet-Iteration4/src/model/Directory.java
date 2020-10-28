package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Directory extends FileOrDirectory{
    private final List<FileOrDirectory> child = new ArrayList<>();
    
    //
    // Construct
    //
    public Directory(String path, String name, Date date, int size, Directory parent)
    {
        this.type = Type.D;
        this.path = path;
        this.name = new SimpleStringProperty(name);
        this.size = new SimpleIntegerProperty(size);
        this.date = date;
        this.parent = parent;
    }
    
    @Override
    public List<FileOrDirectory> getChild()
    {
        return (this.child);
    }
    
    public int getNbChild(){
        int nb_child;
        
        nb_child = 0;
        for (int i = 0; i < this.child.size(); ++i){
            if (this.child.get(i).isSimpleFile())
                nb_child +=1;
            else
            {
                nb_child +=1;
                nb_child += ((Directory)(this.child.get(i))).getNbChild();
            }
        }
        return (nb_child);
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
                str += "\n" + addTab(i) + this.child.get(count).toString(i);
            else
                str += "\n" + addTab(i) + this.child.get(count).toString(i);
        }
        return (this.parent.getName() + "   " +this.type + " : " + this.name.getValue() + "   " 
                + this.size.getValue() + "   " + this.date + "   " + this.corresp + "   " + this.selected.getValue() + " " +str);
    }
    
    @Override
    public String toString()
    {
        String str = "";
        int count = 0;
        
        while (count < this.child.size())
        {
            if (this.child.get(count) instanceof SimpleFile)
                str += "\n" + addTab(1) + this.child.get(count).toString(1);
            else
                str += "\n" + addTab(1) + this.child.get(count).toString(1);
            ++count;
        }
        return (this.type + " : " + this.name.getValue() + "   " 
                + this.size.getValue() + "   " + this.date + "   " + this.corresp + "   " + this.selected.getValue() + " "+str);
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
        for(int i = 0; i < this.child.size(); ++i){
            if (this.child.get(i).isDirectory())
                new_size += this.child.get(i).updateSize();
            else
                new_size += this.child.get(i).getSize();
        }
        this.size.setValue(new_size);
        return (new_size);
    }
    
    //****************************************************************************************************************************************
    //****************************************************************************************************************************************
    //met a jour la date du dossier et tout les dossier en dessous de lui
    //****************************************************************************************************************************************
    //****************************************************************************************************************************************
    public void setDate(Date d)
    {
        this.date = new Date(d.getTime());
    }
    
    private void getAllFod(Directory d)
    {
        Directory tmp;
        for (int i = 0; i < d.child.size(); ++i){
            if (d.child.get(i).isDirectory()){
                tmp = (Directory)(d.getChild().get(i));
                getAllFod(tmp);
            }
            if (this.getDate().compareTo(d.getChild().get(i).getDate()) < 0)
                this.setDate(d.getChild().get(i).getDate());                   
        }
    }
    
    @Override
    public void updateDate()
    {
        Directory tmp;
        
        getAllFod(this);
        for (int i = 0; i < this.child.size(); ++i){
            if (this.child.get(i).isDirectory()){
                tmp = (Directory)(this.child.get(i));
                tmp.updateDate();
            }
        }
    }
}