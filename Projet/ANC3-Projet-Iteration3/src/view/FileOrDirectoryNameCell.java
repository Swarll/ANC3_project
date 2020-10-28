package view;

import model.*;

public class FileOrDirectoryNameCell extends FileOrDirectoryCell {

    @Override
    String texte(FileOrDirectory elem) {
        return elem.getName();
    }
    
}
