package viewModel;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.TreeItem;
import javafx.stage.Stage;
import model.Correspondance;
import model.Directory;
import model.FileOrDirectory;
import model.Model;
import model.SimpleFile;
import utils.DirectoryComparator;
import view.EditView;

public class ViewModel {
    private final BooleanProperty all = new SimpleBooleanProperty(true);
    private final BooleanProperty newer = new SimpleBooleanProperty(false);
    private final BooleanProperty older = new SimpleBooleanProperty(false);
    private final BooleanProperty orphan = new SimpleBooleanProperty(false);
    private final BooleanProperty same = new SimpleBooleanProperty(false);
    private final BooleanProperty foldersOnly = new SimpleBooleanProperty(false);
    private final BooleanProperty chooser1 = new SimpleBooleanProperty(false);
    private final BooleanProperty chooser2 = new SimpleBooleanProperty(false);
    private final ObjectProperty<TreeItem<FileOrDirectory>> op1 = new SimpleObjectProperty<>();
    private final ObjectProperty<TreeItem<FileOrDirectory>> op2 = new SimpleObjectProperty<>();
    private final Model model;
    
    public ViewModel(Model mod)
    {
        this.model = mod;
        configureListener();
        op1.setValue(this.model.getT1());
        op2.setValue(this.model.getT2());
    }
    
    private void configureListener()
    {
        configureSortButtonsListers();
        configureChooserButtonListeners();
    }
      
    private void configureSortButtonsListers()
    {
        configureAllButtonListener();
        configureSortButton(this.newer, Correspondance.NEWER, Correspondance.OLDER);
        configureSortButton(this.older, Correspondance.OLDER, Correspondance.NEWER);
        configureSortButton(this.same, Correspondance.SAME, Correspondance.SAME);
        configureSortButton(this.orphan, Correspondance.ORPHAN, Correspondance.ORPHAN);
        configureFoldersOnlyButtonListener();
    }
    
    private void configureAllButtonListener()
    {  
        this.all.addListener((obs, old, nw) -> {
            if(all.getValue())
            {
                this.newer.setValue(false);
                this.older.setValue(false);
                this.same.setValue(false);
                this.orphan.setValue(false);
                this.model.addAllToFilterLists();
            }
            else
            {
                this.model.removeAllFromFilterLists();
                setAllTorF();
            }
            refactorFolders();
        });
    }
    
    private void configureSortButton(BooleanProperty sbp, Correspondance c1, Correspondance c2)
    {
        sbp.addListener((obs, old, nw) -> {
            buttonAction(nw, c1, c2);
        });
    }
    
    private void buttonAction(Boolean nw, Correspondance c1, Correspondance c2)
    {
        setAllTorF();
        if (nw)
        {
            this.model.addCorrespToFilterList1(c1);
            this.model.addCorrespToFilterList2(c2);                
        }
        else
        {
            this.model.removeCorrespFromFilterList1(c1);
            this.model.removeCorrespFromFilterList2(c2);
        }
        refactorFolders();
    }
    
    private void setAllTorF()
    {
         this.all.setValue(this.newer.getValue() == false && this.older.getValue() == false && this.same.getValue() == false && this.orphan.getValue() == false);
    }
    
    private void configureFoldersOnlyButtonListener()
    {
        this.foldersOnly.addListener((obs, old, nw) -> {
            if (nw)
                this.model.getFilter().setFolderOnly(true);                          
            else
                this.model.getFilter().setFolderOnly(false);
            refactorFolders();
        });
    }
    
    private void refactorFolders()
    {
        this.model.refactorFolders();
        setValuesToTreeItems();
    }
    
    private void setValuesToTreeItems()
    {
        op1.setValue(this.model.getT1());
        op2.setValue(this.model.getT2());
    }
    
    public void refactorAndCompareFolders()
    {
        final DirectoryComparator fc = new DirectoryComparator();
        this.model.refactorFolders();
        fc.rebootCorresp(getD1());
        fc.rebootCorresp(getD2());
        fc.compare((Directory)getD1(), (Directory)getD2());
        setValuesToTreeItems();
    }
    
    private void configureChooserButtonListeners()
    {
        this.chooser1.addListener((obs, old, nw) -> {
            System.out.println(obs);
        });
    }
    
    public void chooseLeftDirectory()
    {
        this.model.chooseLeftDirectory();
        refactorFolders();
    }
    
    public void chooseRightDirectory()
    {
        this.model.chooseRightDirectory();
        refactorFolders();
    }

    public void editSimpleFile(FileOrDirectory fod)
    {
        Stage stage = new Stage();
        if (fod.isSimpleFile())
        {
            EditView editView = new EditView(stage, new EditVM((SimpleFile)fod, this));
            stage.show();
        }
    }
    
    public FileOrDirectory getD1()
    {
        return (this.model.getD1());
    }
    
    public FileOrDirectory getD2()
    {
        return (this.model.getD2());
    }
    
    public BooleanProperty allButtonProperty(){
        return (all);
    }
    
    public BooleanProperty newerButtonProperty(){
        return (newer);
    }
    
    public BooleanProperty olderButtonProperty(){
        return (older);
    }
    
    public BooleanProperty orphanButtonProperty(){
        return (orphan);
    }
    
    public BooleanProperty sameButtonProperty(){
        return (same);
    }
    
    public BooleanProperty foldersOnlyButtonProperty(){
        return (foldersOnly);
    }
    
    public BooleanProperty chooser1Property()
    {
        return (this.chooser1);
    }
    
    public BooleanProperty chooser2Property()
    {
        return (this.chooser2);
    }
    
    public ObjectProperty<TreeItem<FileOrDirectory>> OP1Property()
    {
        return (this.op1);
    }
    
    public ObjectProperty<TreeItem<FileOrDirectory>> OP2Property()
    {
        return (this.op2);
    }
}
