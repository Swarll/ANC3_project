package main;

import java.io.File;
import java.util.Date;
import java.util.Scanner;
import model.Directory;
import model.FileOrDirectory;
import model.SimpleFile;

public class SortFile {

    public static FileOrDirectory copy4(File content, String path)
    {
        SimpleFile fod = new SimpleFile(path, content.getName(), new Date(content.lastModified()), (int)content.length(), null);
        return (fod);
    }
    
    public static FileOrDirectory copy3(File[] content, String path, int count, Directory parent)
    {
        SimpleFile fod = new SimpleFile(path, content[count].getName(), new Date(content[count].lastModified()), (int)content[count].length(), parent);
        return (fod);
    }

    public static FileOrDirectory copy2(File[] f, String path, int i, Directory parent)
    {
        File[] children;
        Directory fod = null;
        
        if (f[i].exists() && f[i].isDirectory())
        {
            fod = new Directory(path, f[i].getName(), new Date(f[i].lastModified()), (int)f[i].length(), parent);
            path += f[i].getName() + "/";
            children = f[i].listFiles();
            for(int count = 0; count < children.length; ++count)
            {
                fod.getChild().add(copy2(children, path, count, fod));
            }
            fod.updateSize();
            return (fod);
        }
        else
        {
            return (copy3(f, path, i, parent));
        }
    }
    
    public static FileOrDirectory copy1(File f, String path)
    {
        File[] children;
        Directory fod = null;
        
        if (f.exists() && f.isDirectory())
        {
            fod = new Directory(path, f.getName(), new Date(f.lastModified()), (int)f.length(), null);
            path += f.getName() + "/";
            children = f.listFiles();
            for(int count = 0; count < children.length; ++count)
            {
                fod.getChild().add(copy2(children, path, count, fod));
            }
            fod.updateSize();
            return (fod);
        }
        else
        {
            return (copy4(f, path));
        }
    }
    
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.println("Veuillez entrer le chemin du fichier ou dossier.");
        
        File f1, f2;
        SimpleFile test;
        f1 = new File("BLANC");
        f2 = new File("NOIRE");
        FileOrDirectory fod1 = copy1(f1, "./");
        FileOrDirectory fod2 = copy1(f2, "./");
        ((Directory)fod1).compareWith((Directory)fod2);
        //System.out.println("\n\n");
        //System.out.println(fod.toString());
    }
}