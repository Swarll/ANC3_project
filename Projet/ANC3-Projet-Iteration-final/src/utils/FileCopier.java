package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.Scanner;
import model.Correspondance;
import model.Directory;
import model.FileOrDirectory;
import model.SimpleFile;

public class FileCopier {
    //****************************************************************************************************************************************
    //****************************************************************************************************************************************
    // Utils
    //****************************************************************************************************************************************
    //****************************************************************************************************************************************
    public String getPath(FileOrDirectory fod, int x)
    {

        String Path  = "";
        String[] parts = fod.getPath().split("\\\\");
        
        for (int i = 0; i < parts.length - x; ++i)
        {
            Path += parts[i];
            if (i != parts.length - x -1)
                Path += "\\";
        }
        return (Path);
    }
    
    public String getRelativePath(FileOrDirectory fod)
    {

        int    save = 0;
        String relPath  = "";
        String[] parts = fod.getPath().split("\\\\");
        
        for (int i = 0; i < parts.length - 1; ++i)
        {
            if (fod.getLastParent() != null)
            {
                if (parts[i].compareTo(fod.getLastParent().getName()) == 0)
                {
                    save = i + 1;
                    break;
                }
            }
        }
        while (save < parts.length - 1)
        {
            relPath += parts[save];
            if (save != parts.length - 2)
                relPath += "\\";
            ++save;
        }
        //System.out.println("+++++++++++++++++++++" + relPath + "+++++++++++++++++++++");
        return (relPath);
    }
    
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
    // Copie le fichier ou dossier selectionné :: copy(File f, String path)
    //****************************************************************************************************************************************
    //****************************************************************************************************************************************
    
    private static FileOrDirectory copy4(File content) throws FileNotFoundException
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
        SimpleFile fod = new SimpleFile(content.getAbsolutePath(), content.getName(), txt, new Date(content.lastModified()), size, null);
        return (fod);
    }
    
    private static FileOrDirectory copy3(File[] content, int count, Directory parent) throws FileNotFoundException
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
        SimpleFile fod = new SimpleFile(content[count].getAbsolutePath(), content[count].getName(), txt, new Date(content[count].lastModified()), size, parent);
        return (fod);
    }

    private static FileOrDirectory copy2(File[] f, int i, Directory parent) throws FileNotFoundException
    {
        File[] children;
        Directory fod;
        
        if (f[i].exists() && f[i].isDirectory())
        {
            fod = new Directory(f[i].getAbsolutePath(), f[i].getName(), new Date(f[i].lastModified()), (int)f[i].length(), parent);
            children = f[i].listFiles();
            for(int count = 0; count < children.length; ++count)
            {
                fod.getChild().add(copy2(children, count, fod));
            }
            fod.updateSize();
            fod.updateDate();
            return (fod);
        }
        else
            return (copy3(f, i, parent));
    }
    
    public FileOrDirectory copy(File f) throws FileNotFoundException
    {
        File[] children;
        Directory fod;
        
        if (f.exists() && f.isDirectory())
        {
            fod = new Directory(f.getAbsolutePath(), f.getName(), new Date(f.lastModified()), (int)f.length(), null);
            children = f.listFiles();
            for(int count = 0; count < children.length; ++count)
            {
                fod.getChild().add(copy2(children, count, fod));
            }
            fod.updateSize();
            fod.updateDate();
            return (fod);
        }
        else
        {
            return (copy4(f));
        }
    }
    
    //****************************************************************************************************************************************
    //****************************************************************************************************************************************
    // Copie les fichiers selectionnés de gauche vers droite :: swap(FileOrDirectory fod1, FileOrDirectory fod2)
    //****************************************************************************************************************************************
    //****************************************************************************************************************************************

    private void crateNewSimpleFile(SimpleFile d, FileOrDirectory fod, int reach)
    {
        String parts[] = getRelativePath(d).split("\\\\");
        int nb = parts.length;
        if (nb == 1 && parts[0].compareTo("") == 0)
            nb = 0;
        if (nb == reach){
            String path = fod.getPath() + "\\" + d.getName();
            fod.getChild().add(new SimpleFile(d, path, (Directory)fod));
        }
        else
        {
            if (fod.isDirectory())
            {
                for (int i = 0; i < fod.getChild().size(); ++i)
                {
                    if (fod.getNChild(i).isDirectory())
                    {
                        if (reach < nb && fod.getNChild(i).getName().compareTo(parts[reach]) == 0)
                            crateNewSimpleFile(d, fod.getNChild(i), ++reach);
                    }
                }
            }
        }
    }
    
    private void crateNewDirectory(Directory d, FileOrDirectory fod, int reach)
    {
        
        String parts[] = getRelativePath(d).split("\\\\");
        int nb = parts.length;
        if (nb == 1 && parts[0].compareTo("") == 0)
            nb = 0;
        if (nb == reach){
            String path = fod.getPath() + "\\" + d.getName();
            fod.getChild().add(new Directory(d, path, (Directory)fod));
        }
        else
        {
            if (fod.isDirectory())
            {
                for (int i = 0; i < fod.getChild().size(); ++i)
                {
                    if (fod.getNChild(i).isDirectory())
                    {
                        if (reach < nb && fod.getNChild(i).getName().compareTo(parts[reach]) == 0)
                            crateNewDirectory(d, fod.getNChild(i), ++reach);
                    }
                }
            }
        }
    }
    
    private FileOrDirectory findCorrespFile(FileOrDirectory fod1, FileOrDirectory fod2)
    {
        if (fod2.isDirectory())
        {
            for (int i = 0; i < fod2.getChild().size(); ++i)
            {
                System.out.println("fod1 : " + getRelativePath(fod1) + "\nfod2 : " + getRelativePath(fod2.getNChild(i)) );
                if (fod1.getName().compareTo(fod2.getNChild(i).getName()) == 0 && getRelativePath(fod1).compareTo(getRelativePath(fod2.getNChild(i))) == 0 && fod1.isDirectory() == fod2.getNChild(i).isDirectory())
                    return (fod2.getNChild(i));
                if (fod2.getNChild(i).isDirectory())
                {
                    FileOrDirectory tmp = findCorrespFile(fod1, fod2.getNChild(i));
                    if (tmp != null)
                        return (tmp);
                }
            }
        }
        return (null);
    }
    
    
    public void swap(FileOrDirectory fod1, FileOrDirectory fod2)
    {
        if (fod1.isDirectory())
        {
            for (int i = 0; i < fod1.getChild().size(); ++i)
            {
                if (fod1.getNChild(i).getSelected())
                {
                    if (fod1.getNChild(i).isDirectory())
                    {
                        if (fod1.getNChild(i).getCorresp() == Correspondance.ORPHAN)
                        {
                            Directory tmp = (Directory)findCorrespFile(fod1.getNChild(i), fod2);
                            Directory d = (Directory)(fod1.getNChild(i));
                            if (tmp == null)
                                crateNewDirectory(d, fod2, 0);
                        }
                        else if (fod1.getNChild(i).getCorresp() == Correspondance.NEWER || fod1.getNChild(i).getCorresp() == Correspondance.OLDER)
                        {
                            Directory d = (Directory)(fod1.getNChild(i));
                            Directory tmp = (Directory)findCorrespFile(fod1.getNChild(i), fod2);
                            tmp.setDate(d.getDate());
                            tmp.setSize(d.getSize());
                        }
                        swap(fod1.getNChild(i), fod2);
                    }
                    else
                    {
                        if (fod1.getChild().get(i).getCorresp() == Correspondance.ORPHAN)
                        {
                            SimpleFile sf = (SimpleFile)(fod1.getNChild(i));
                            crateNewSimpleFile(sf, fod2, 0);
                        }
                        else if (fod1.getNChild(i).getCorresp() == Correspondance.NEWER || fod1.getNChild(i).getCorresp() == Correspondance.OLDER)
                        {
                            SimpleFile sf = (SimpleFile)(fod1.getNChild(i));
                            SimpleFile tmp = (SimpleFile)findCorrespFile(fod1.getNChild(i), fod2);
                            tmp.setContent(sf.getContent());
                            tmp.setSize(sf.getSize());
                            tmp.setDate(sf.getDate());
                            tmp.updateSize();
                        }
                    }
                }
            }
        }
        fod1.updateSize();
        fod2.updateSize();
    }
}
