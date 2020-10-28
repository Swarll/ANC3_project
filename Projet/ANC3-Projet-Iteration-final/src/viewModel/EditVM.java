package viewModel;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import model.SimpleFile;

public class EditVM {
    private final ViewModel vm;
    private final SimpleFile file;
    private final StringProperty content = new SimpleStringProperty();
    
    public EditVM(SimpleFile file, ViewModel vm)
    {
        this.vm = vm;
        this.file = file;
        this.content.setValue(this.file.getContent());
    }
    
    public String getFileName()
    {
        return (this.file.getName());
    }
    
    public int getFileSize()
    {
        return (this.file.getSize());
    }
    
    public StringProperty contentProperty()
    {
        return(this.content);
    }
    
    public void save()
    {     
        this.file.setContent(this.content.getValue());
        this.file.setSize(this.file.getContent().length());
        this.file.updateSize();
        this.file.updateDate();
        this.vm.refactorAndCompareFolders();
    }
}
