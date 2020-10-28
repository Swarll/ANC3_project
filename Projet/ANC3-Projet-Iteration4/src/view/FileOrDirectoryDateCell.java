package view;

import model.*;

public class FileOrDirectoryDateCell extends FileOrDirectoryCell {

    @Override
    String texte(FileOrDirectory elem) {
        return ""+elem.getDate();
    }
    
}
