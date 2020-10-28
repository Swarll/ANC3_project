package view;

import java.nio.file.Paths;
import javafx.application.Application;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import model.*;

public class ViewDirectory{
    private final int width = 1150  ;
    private final int height = 700;
    private FileOrDirectory d1;
    private FileOrDirectory d2;
    private TreeItem<FileOrDirectory> t1;
    private TreeItem<FileOrDirectory> t2;
    private TreeTableView<FileOrDirectory> v1;
    private TreeTableView<FileOrDirectory> v2;
    private Label orphan = new Label("ORPHAN");
    private Label same = new Label("SAME");
    private Label partial_same = new Label("PARTIAL_SAME");
    private Label newer = new Label("NEWER");
    private Label older = new Label("OLDER");
    private BorderPane boxe = new BorderPane();
    private static final String CSSPATH = "view/cssView.css";
    
    public ViewDirectory (Stage primaryStage, FileOrDirectory d1, FileOrDirectory d2)
    {
        this.d1 = d1;
        this.d2 = d2;
        ((Directory)this.d1).compareWith((Directory)this.d2);
        this.t1 = itemBuild(d1);
        this.t2 = itemBuild(d2);
        this.v1 = new TreeTableView<>(t1);
        this.v2 = new TreeTableView<>(t2);
        
        configureInterface();
        
        Scene scene = new Scene(boxe, width, height);
        primaryStage.setTitle("Beyond Compare");
        primaryStage.setScene(scene);
    }
    
    private void configureInterface()
    {
        configureV(this.v1);
        configureV(this.v2);
        configureBoxe();
    }
    
    private void configureBoxe()
    {
        HBox folders = new HBox();
        
        folders.getChildren().addAll(v1, v2);
        boxe.setCenter(folders);
        
        HBox hb = new HBox();
        orphan.getStylesheets().add(CSSPATH);
        orphan.getStyleClass().set(0, "ORPHAN");
        same.getStylesheets().add(CSSPATH);
        same.getStyleClass().set(0, "SAME");
        partial_same.getStylesheets().add(CSSPATH);
        partial_same.getStyleClass().set(0, "PARTIAL_SAME");
        newer.getStylesheets().add(CSSPATH);
        newer.getStyleClass().set(0, "NEWER");
        older.getStylesheets().add(CSSPATH);
        older.getStyleClass().set(0, "OLDER");
        hb.getChildren().addAll(orphan, same, partial_same, newer, older);
        hb.setSpacing(10);
        hb.setAlignment(Pos.CENTER);
        boxe.setBottom(hb);
        
        BorderPane top = new BorderPane();
        HBox pathLeft = new HBox();
        pathLeft.getChildren().addAll(new Label(Paths.get(d1.getName()).toAbsolutePath().toString()));
        HBox pathRight = new HBox();
        pathRight.getChildren().addAll(new Label(Paths.get(d2.getName()).toAbsolutePath().toString()));
        top.setLeft(pathLeft);
        top.setRight(pathRight);
        boxe.setTop(top);
    }
    
    private void configureV(TreeTableView<FileOrDirectory> v)
    {
        TreeTableColumn<FileOrDirectory, FileOrDirectory> c1 = new TreeTableColumn<>("Name");
        TreeTableColumn<FileOrDirectory, FileOrDirectory> c2 = new TreeTableColumn<>("Type");
        TreeTableColumn<FileOrDirectory, FileOrDirectory> c3 = new TreeTableColumn<>("Modification Date");
        TreeTableColumn<FileOrDirectory, FileOrDirectory> c4 = new TreeTableColumn<>("Size");
        c1.setPrefWidth(width / 5);
        c2.setPrefWidth(width / 10);
        c3.setPrefWidth(width / 10);
        c4.setPrefWidth(width / 10);
        c1.setCellValueFactory(r -> new SimpleObjectProperty<>(r.getValue().getValue()));
        c2.setCellValueFactory(r -> new SimpleObjectProperty<>(r.getValue().getValue()));
        c3.setCellValueFactory(r -> new SimpleObjectProperty<>(r.getValue().getValue()));
        c4.setCellValueFactory(r -> new SimpleObjectProperty<>(r.getValue().getValue()));
        c1.setCellFactory((param) -> {return new FileOrDirectoryNameCell();});
        c2.setCellFactory((param) -> {return new FileOrDirectoryTypeCell();});
        c3.setCellFactory((param) -> {return new FileOrDirectoryDateCell();});
        c4.setCellFactory((param) -> {return new FileOrDirectorySizeCell();});
        v.getColumns().setAll(c1, c2, c3, c4);
    }
    
    private TreeItem<FileOrDirectory> itemBuild(FileOrDirectory fod)
    {
        TreeItem<FileOrDirectory> tree = new TreeItem<>(fod);
        
        tree.setExpanded(true);
        if (fod.isDirectory()) {
            fod.getChild().forEach(child -> {
                tree.getChildren().add(itemBuild(child));
            });
        }
        return (tree);
    }
}
