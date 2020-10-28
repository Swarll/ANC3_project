package model;

public class Model {
    private final FileOrDirectory fod1;
    private final FileOrDirectory fod2;
    
    public Model(FileOrDirectory d1, FileOrDirectory d2)
    {
        this.fod1 = d1;
        this.fod2 = d2;
    }
    
    public FileOrDirectory getD1()
    {
        return (this.fod1);
    }
    
    public FileOrDirectory getD2()
    {
        return (this.fod2);
    }
    
}
