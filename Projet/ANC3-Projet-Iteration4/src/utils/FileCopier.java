package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.Scanner;
import model.Directory;
import model.FileOrDirectory;
import model.SimpleFile;

public class FileCopier {
    
    private static String getExtension(String str)
    {
        int i;
        String extension;
        
        i = str.length() - 1;
        extension = "";
        while (i >= 0 && str.charAt(i) != '.')
        {
            --i;
        }
        ++i;
        while (i < str.length())
        {
            extension += str.charAt(i);
            ++i;
        }
        return(extension);
    }
    //****************************************************************************************************************************************
    //****************************************************************************************************************************************
    // Copie le fichier ou dossier selectionné
    //****************************************************************************************************************************************
    //****************************************************************************************************************************************
    
    private static FileOrDirectory copy4(File content, String path) throws FileNotFoundException
    {
        String txt;
        String extension;
        int    size;
        
        txt = null;
        size = (int)content.length();
        extension = getExtension(content.getName());
        if (extension != null && extension.compareTo("txt") == 0)
        {
            Scanner scan = new Scanner(content);
            txt = "";
            while (scan.hasNextLine())
            {
                txt += scan.nextLine();
                if (scan.hasNextLine())
                    txt += "\n";
            }
            size = txt.length();
            scan.close();
        }
        SimpleFile fod = new SimpleFile(path, content.getName(), txt, new Date(content.lastModified()), size, null);
        return (fod);
    }
    
    private static FileOrDirectory copy3(File[] content, String path, int count, Directory parent) throws FileNotFoundException
    {
        String txt;
        String extension;
        int    size;
        
        txt = null;
        size = (int)content[count].length();
        extension = getExtension(content[count].getName());
        if (extension != null && extension.compareTo("txt") == 0)
        {
            Scanner scan = new Scanner(content[count]);
            txt = "";
            while (scan.hasNextLine())
            {
                txt += scan.nextLine();
                if (scan.hasNextLine())
                    txt += "\n";               
            }
            size = txt.length();
            scan.close();
        }
        SimpleFile fod = new SimpleFile(path, content[count].getName(), txt, new Date(content[count].lastModified()), size, parent);
        return (fod);
    }

    private static FileOrDirectory copy2(File[] f, String path, int i, Directory parent) throws FileNotFoundException
    {
        File[] children;
        Directory fod;
        
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
            fod.updateDate();
            return (fod);
        }
        else
        {
            return (copy3(f, path, i, parent));
        }
    }
    
    public FileOrDirectory copy(File f, String path) throws FileNotFoundException
    {
        File[] children;
        Directory fod;
        
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
            fod.updateDate();
            return (fod);
        }
        else
        {
            return (copy4(f, path));
        }
    }
}
