package view;

import model.*;

public class FileOrDirectoryTypeCell extends FileOrDirectoryCell {

    @Override
    String texte(FileOrDirectory elem) {
        return elem.getType();
    }
    
}
