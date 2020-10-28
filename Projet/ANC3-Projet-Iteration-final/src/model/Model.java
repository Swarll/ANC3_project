package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.TreeItem;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import utils.DirectoryComparator;
import utils.FileCopier;
import view.View;

public class Model {
    private final Filter filter = new Filter();
    private FileOrDirectory fod1;
    private FileOrDirectory fod2;
    private TreeItem<FileOrDirectory> t1;
    private TreeItem<FileOrDirectory> t2;
    
    public Model(FileOrDirectory d1, FileOrDirectory d2)
    {
        this.fod1 = d1;
        this.fod2 = d2;
        t1 = itemBuild(d1);
        t2 = itemBuild(d2);
    }
    
    public Filter getFilter()
    {
        return (this.filter);
    }
    
    public FileOrDirectory getD1()
    {
        return (this.fod1);
    }
    
    public FileOrDirectory getD2()
    {
        return (this.fod2);
    }
    
    public TreeItem<FileOrDirectory> getT1()
    {
        return (this.t1);
    }
    
    public TreeItem<FileOrDirectory> getT2()
    {
        return (this.t2);
    }
    
    public void addCorrespToFilterList1(Correspondance c)
    {
        this.filter.addCorrespToFilterList(this.filter.getFilterListD1(), c);
    }
    
    public void addCorrespToFilterList2(Correspondance c)
    {
        this.filter.addCorrespToFilterList(this.filter.getFilterListD2(), c);
    }
    
    public void removeCorrespFromFilterList1(Correspondance c)
    {
        this.filter.removeCorrespFromFilerList(this.filter.getFilterListD1(), c);
    }
    
    public void removeCorrespFromFilterList2(Correspondance c)
    {
        this.filter.removeCorrespFromFilerList(this.filter.getFilterListD2(), c);
    }
    
    public void addAllToFilterLists()
    {
        this.filter.addAllToFilterList(this.filter.getFilterListD1());
        this.filter.addAllToFilterList(this.filter.getFilterListD2());
    }
    
    public void removeAllFromFilterLists()
    {
        this.filter.removeAllFromFilterList(this.filter.getFilterListD1()); //retire tout les filtres
        this.filter.removeAllFromFilterList(this.filter.getFilterListD2());
    }
    
    private TreeItem<FileOrDirectory> itemBuild(FileOrDirectory fod)
    {
        TreeItem<FileOrDirectory> tree = new TreeItem<>(fod);

        tree.setExpanded(true);
        if (fod.isDirectory() && fod.getSelected()) {
            fod.getChild().forEach(child -> {
                if(child.getSelected())
                    tree.getChildren().add(itemBuild(child));
            });
        }
        return (tree);
    }
    
    public void refactorFolders()
    {
        this.filter.select(this.fod1, this.filter.getFilterListD1(), this.filter.getFolderOnly());
        this.filter.select(this.fod2, this.filter.getFilterListD2(), this.filter.getFolderOnly());
        this.t1 = itemBuild(fod1);
        this.t2 = itemBuild(fod2);
    }
    
    public void chooseLeftDirectory()
    {
        chooseDirectory(1);
    }
    
    public void chooseRightDirectory()
    {
        chooseDirectory(2);
    }
    
    private void chooseDirectory(int pointer)
    {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        Stage chooseStage = new Stage();
        if (pointer == 1)
            directoryChooser.setInitialDirectory(new File(fod1.getPath()));
        else
            directoryChooser.setInitialDirectory(new File(fod2.getPath()));
        File f = directoryChooser.showDialog(chooseStage);
        if (f != null){
            try {
                final FileCopier fcBis = new FileCopier();
                if (pointer == 1)
                    this.fod1 = fcBis.copy(f);
                else
                    this.fod2 = fcBis.copy(f);
                final DirectoryComparator fc = new DirectoryComparator();
                fc.rebootCorresp(fod1);
                fc.rebootCorresp(fod2);
                fc.compare((Directory)fod1, (Directory)fod2);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void swap()
    {
        FileCopier fc = new FileCopier();
        
        fc.swap(this.fod1, this.fod2);
    }
}
