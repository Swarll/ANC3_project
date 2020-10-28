package view;

import model.*;

public class FileOrDirectorySizeCell extends FileOrDirectoryCell {

    @Override
    String texte(FileOrDirectory elem) {
        return ""+elem.getSize();
    }
    
}
