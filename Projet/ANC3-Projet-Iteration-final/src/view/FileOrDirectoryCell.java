package view;

import model.*;
import javafx.scene.control.cell.TextFieldTreeTableCell;

public abstract class FileOrDirectoryCell extends TextFieldTreeTableCell<FileOrDirectory, FileOrDirectory> {

    private static final String CSSPATH = "view/cssView.css";

    public FileOrDirectoryCell() {
        getStylesheets().add(CSSPATH);
    }

    @Override
    public void updateItem(FileOrDirectory elem, boolean isEmpty) {
        super.updateItem(elem, isEmpty);
        if (elem == null) {
            return;
        }
        this.setText(texte(elem));
        if (elem.getCorresp().toString().compareTo("ORPHAN") == 0)
        {
            this.getStyleClass().set(0, "ORPHAN");
        }
        else if (elem.getCorresp().toString().compareTo("NEWER") == 0)
        {
            this.getStyleClass().set(0, "NEWER");
        }
        else if (elem.getCorresp().toString().compareTo("SAME") == 0)
        {
            this.getStyleClass().set(0, "SAME");
        }
        else if (elem.getCorresp().toString().compareTo("OLDER") == 0)
        {
            this.getStyleClass().set(0, "OLDER");
        }
        else
            this.getStyleClass().set(0, "PARTIAL_SAME");
            
    }
  

    abstract String texte(FileOrDirectory elem);

}