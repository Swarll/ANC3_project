package view;

import java.nio.file.Paths;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.*;
import utils.DirectoryComparator;
import viewModel.ViewModel;

public class View{
    private final int width = 1150;
    private final int height = 700;
    private final int ButtonSize = 100;
    private final String img = "./folder.png";
    private final Button chooser1 = new Button();
    private final Button chooser2 = new Button();
    private final ViewModel vm;
    private final TreeTableView<FileOrDirectory> v1 = new TreeTableView<>();
    private final TreeTableView<FileOrDirectory> v2 = new TreeTableView<>();
    private final Label orphan = new Label("ORPHAN");
    private final Label same = new Label("SAME");
    private final Label partial_same = new Label("PARTIAL_SAME");
    private final Label newer = new Label("NEWER");
    private final Label older = new Label("OLDER");
    private final BorderPane boxe = new BorderPane();
    private final HBox folders = new HBox();
    private static final String CSSPATH = "view/cssView.css";
    private final ToggleButton allButton = new ToggleButton("All");
    private final ToggleButton newerButton = new ToggleButton("Newer-left");
    private final ToggleButton olderButton = new ToggleButton("Older-Left");
    private final ToggleButton sameButton = new ToggleButton("Same");
    private final ToggleButton orphanButton = new ToggleButton("Orphan");
    private final ToggleButton folderButton = new ToggleButton("Folder-Only");
    private final DirectoryComparator fc = new DirectoryComparator();
    
    public View (Stage primaryStage, ViewModel vm)
    {
        this.vm = vm;
        this.fc.rebootCorresp(this.vm.getD1());
        this.fc.rebootCorresp(this.vm.getD2());
        this.fc.compare((Directory)this.vm.getD1(), (Directory)this.vm.getD2());

        configureInterface();
        
        Scene scene = new Scene(boxe, width, height);
        primaryStage.setTitle("Beyond Compare");
        primaryStage.setScene(scene);
    }
    
    private void configureInterface()
    {
        configureCss();
        configureV(this.v1);
        configureV(this.v2);
        configureChooser1(this.chooser1);
        configureChooser2(this.chooser2);
        configureBinding();
        configureListButton();
        configureBoxe();
    }
    
    private void configureCss()
    {
        this.orphan.getStylesheets().add(CSSPATH);
        this.orphan.getStyleClass().set(0, "ORPHAN");
        this.same.getStylesheets().add(CSSPATH);
        this.same.getStyleClass().set(0, "SAME");
        this.partial_same.getStylesheets().add(CSSPATH);
        this.partial_same.getStyleClass().set(0, "PARTIAL_SAME");
        this.newer.getStylesheets().add(CSSPATH);
        this.newer.getStyleClass().set(0, "NEWER");
        this.older.getStylesheets().add(CSSPATH);
        this.older.getStyleClass().set(0, "OLDER");
    }
    
    private void configureBoxe()
    {
        this.folders.getChildren().addAll(v1, v2);
        
        HBox hb = new HBox();
        hb.getChildren().addAll(orphan, same, partial_same, newer, older);
        hb.setSpacing(10);
        hb.setAlignment(Pos.CENTER);
        
        BorderPane pathLeft = new BorderPane();
        pathLeft.setPrefWidth(this.width/2);
        pathLeft.setLeft(new Label(Paths.get(vm.getD1().getName()).toAbsolutePath().toString()));
        pathLeft.setRight(this.chooser1);
        
        BorderPane pathRight = new BorderPane();
        pathRight.setPrefWidth(this.width/2);
        pathRight.setLeft(new Label(Paths.get(vm.getD2().getName()).toAbsolutePath().toString()));
        pathRight.setRight(this.chooser2);
        
        FlowPane bl = new FlowPane();
        bl.getChildren().addAll(this.allButton, this.newerButton, this.olderButton, this.sameButton, this.orphanButton, this.folderButton);
        bl.setHgap(10);
        bl.setAlignment(Pos.CENTER);
        
        BorderPane top = new BorderPane();
        top.setLeft(pathLeft);
        top.setRight(pathRight);
        top.setBottom(bl);
        
        this.boxe.setBottom(hb);
        this.boxe.setCenter(folders);
        this.boxe.setTop(top);
    }
    
    private void configureV(TreeTableView<FileOrDirectory> v)
    {
        TreeTableColumn<FileOrDirectory, FileOrDirectory> c1 = new TreeTableColumn<>("Name");
        TreeTableColumn<FileOrDirectory, FileOrDirectory> c2 = new TreeTableColumn<>("Type");
        TreeTableColumn<FileOrDirectory, FileOrDirectory> c3 = new TreeTableColumn<>("Modification Date");
        TreeTableColumn<FileOrDirectory, FileOrDirectory> c4 = new TreeTableColumn<>("Size");
        c1.setPrefWidth(this.width / 5);
        c2.setPrefWidth(this.width / 10);
        c3.setPrefWidth(this.width / 10);
        c4.setPrefWidth(this.width / 10);
        c1.setCellValueFactory(r -> new SimpleObjectProperty<>(r.getValue().getValue()));
        c2.setCellValueFactory(r -> new SimpleObjectProperty<>(r.getValue().getValue()));
        c3.setCellValueFactory(r -> new SimpleObjectProperty<>(r.getValue().getValue()));
        c4.setCellValueFactory(r -> new SimpleObjectProperty<>(r.getValue().getValue()));
        c1.setCellFactory((param) -> {return new FileOrDirectoryNameCell();});
        c2.setCellFactory((param) -> {return new FileOrDirectoryTypeCell();});
        c3.setCellFactory((param) -> {return new FileOrDirectoryDateCell();});
        c4.setCellFactory((param) -> {return new FileOrDirectorySizeCell();});
        v.getColumns().setAll(c1, c2, c3, c4);
        configureTTV();
    }
    
    private void configureListButton()
    {
        this.allButton.setPrefWidth(ButtonSize);
        this.newerButton.setPrefWidth(ButtonSize);
        this.olderButton.setPrefWidth(ButtonSize);
        this.sameButton.setPrefWidth(ButtonSize);
        this.orphanButton.setPrefWidth(ButtonSize);
        this.folderButton.setPrefWidth(ButtonSize);
        configureButtonAll();
    }
    
    private void configureButtonAll()
    {
        this.allButton.setOnAction(e -> {
            this.allButton.selectedProperty().setValue(true);
        });
    }
    
    private void configureChooser1(Button chooser)
    {
        ImageView folder1 = new ImageView(new Image(getClass().getResourceAsStream(this.img)));
	chooser.setGraphic(folder1);
        chooser.setOnAction((ActionEvent event) -> {
            this.vm.chooseLeftDirectory();
        });
    }
    
    private void configureChooser2(Button chooser)
    {
        ImageView folder2 = new ImageView(new Image(getClass().getResourceAsStream(this.img)));
        chooser.setGraphic(folder2);
        chooser.setOnAction((ActionEvent event) -> {
            this.vm.chooseRightDirectory();
        });
    }
    
    private void configureTTV()
    {
        this.v1.setOnMousePressed(e -> {
            ttvAction(this.v1, e);
        });
        
        this.v2.setOnMousePressed(e -> {
            ttvAction(this.v2, e);
        });
    }
    
    private void ttvAction(TreeTableView<FileOrDirectory> ttv, MouseEvent e)
    {
        if (e.getClickCount() == 2)
            this.vm.editSimpleFile(ttv.getSelectionModel().getSelectedItems().get(0).getValue());
    }
    
    private void configureBinding()
    {
        this.allButton.selectedProperty().bindBidirectional(this.vm.allButtonProperty());
        this.newerButton.selectedProperty().bindBidirectional(this.vm.newerButtonProperty());
        this.olderButton.selectedProperty().bindBidirectional(this.vm.olderButtonProperty());
        this.sameButton.selectedProperty().bindBidirectional(this.vm.sameButtonProperty());
        this.orphanButton.selectedProperty().bindBidirectional(this.vm.orphanButtonProperty());
        this.folderButton.selectedProperty().bindBidirectional(this.vm.foldersOnlyButtonProperty());
        
        this.v1.rootProperty().bindBidirectional(this.vm.OP1Property());
        this.v2.rootProperty().bindBidirectional(this.vm.OP2Property());
    }
}