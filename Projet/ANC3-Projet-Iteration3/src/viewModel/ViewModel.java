package viewModel;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import model.Model;

public class ViewModel {
    private final BooleanProperty all = new SimpleBooleanProperty(true);
    private final BooleanProperty newer_left = new SimpleBooleanProperty(false);
    private final BooleanProperty newer_right = new SimpleBooleanProperty(false);
    private final BooleanProperty orphan = new SimpleBooleanProperty(false);
    private final BooleanProperty same = new SimpleBooleanProperty(false);
    private final BooleanProperty foldersOnly = new SimpleBooleanProperty(false);
    private Model model;
    
    public ViewModel(Model mod)
    {
        this.model = mod;
    }
    
    public Model getModel()
    {
        return (this.model);
    }
    
    public BooleanProperty allButtonProperty(){
        return (all);
    }
    
    public BooleanProperty newerLeftButtonProperty(){
        return (newer_left);
    }
    
    public BooleanProperty newerRightButtonProperty(){
        return (newer_right);
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
}
