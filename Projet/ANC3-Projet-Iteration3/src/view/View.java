package view;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import static main.SortFile.copy1;
import model.*;
import viewModel.ViewModel;

public class View{
    private final int width = 1150  ;
    private final int height = 700;
    private final int ButtonSize = 100;
    private final Button chooser1 = new Button();
    private final Button chooser2 = new Button();
    private final DirectoryChooser directoryChooser = new DirectoryChooser();
    private final ViewModel vm;
    private TreeItem<FileOrDirectory> t1;
    private TreeItem<FileOrDirectory> t2;
    private TreeTableView<FileOrDirectory> v1;
    private TreeTableView<FileOrDirectory> v2;
    private final Label orphan = new Label("ORPHAN");
    private final Label same = new Label("SAME");
    private final Label partial_same = new Label("PARTIAL_SAME");
    private final Label newer = new Label("NEWER");
    private final Label older = new Label("OLDER");
    private final BorderPane boxe = new BorderPane();
    private final HBox folders = new HBox();
    private static final String CSSPATH = "view/cssView.css";
    private final ToggleButton listButton[] = new ToggleButton[6];
    private final List<Correspondance> listCorrespLeft;
    private final List<Correspondance> listCorrespRight;
    private int[] nb_left = new int[5];
    
    public View (Stage primaryStage, ViewModel vm)
    {
        this.nb_left[0] = 0;
        this.nb_left[1] = 0;
        this.nb_left[2] = 0;
        this.nb_left[3] = 0;
        this.nb_left[4] = 0;
        this.listCorrespLeft = new ArrayList<>();
        this.listCorrespRight = new ArrayList<>();
        this.vm = vm;
        vm.getModel().getD1().rebootState(vm.getModel().getD1());
        vm.getModel().getD2().rebootState(vm.getModel().getD2());
        ((Directory)vm.getModel().getD1()).compareWith((Directory)vm.getModel().getD2());
        this.t1 = itemBuild(vm.getModel().getD1());
        this.t2 = itemBuild(vm.getModel().getD2());
        this.v1 = new TreeTableView<>(t1);
        this.v2 = new TreeTableView<>(t2);
        
        configureInterface(primaryStage);
        
        Scene scene = new Scene(boxe, width, height);
        primaryStage.setTitle("Beyond Compare");
        primaryStage.setScene(scene);
    }
    
    private void configureInterface(Stage primaryStage)
    {
        configureCss();
        configureV(this.v1);
        configureV(this.v2);
        configureChooser1(this.chooser1, primaryStage);
        configureChooser2(this.chooser2, primaryStage);
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
        pathLeft.setLeft(new Label(Paths.get(vm.getModel().getD1().getName()).toAbsolutePath().toString()));
        pathLeft.setRight(this.chooser1);
        
        BorderPane pathRight = new BorderPane();
        pathRight.setPrefWidth(this.width/2);
        pathRight.setLeft(new Label(Paths.get(vm.getModel().getD2().getName()).toAbsolutePath().toString()));
        pathRight.setRight(this.chooser2);
        
        FlowPane bl = new FlowPane();
        bl.getChildren().addAll(this.listButton[0], this.listButton[1], this.listButton[2], this.listButton[3], this.listButton[4], this.listButton[5]);
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
    }
    
    private void configureListButton()
    {
        this.listButton[0] = new ToggleButton("All");
        this.listButton[1] = new ToggleButton("Newer-Left");
        this.listButton[2] = new ToggleButton("Newer-Right");
        this.listButton[3] = new ToggleButton("Orphans");
        this.listButton[4] = new ToggleButton("Same");
        this.listButton[5] = new ToggleButton("Folder Only");
        this.listButton[0].setPrefWidth(ButtonSize);
        this.listButton[1].setPrefWidth(ButtonSize);
        this.listButton[2].setPrefWidth(ButtonSize);
        this.listButton[3].setPrefWidth(ButtonSize);
        this.listButton[4].setPrefWidth(ButtonSize);
        this.listButton[5].setPrefWidth(ButtonSize);
        configureButtonActionAll(this.listButton[0]);
        configureButtonAction(this.listButton[1], Correspondance.NEWER, Correspondance.OLDER);
        configureButtonAction(this.listButton[2], Correspondance.OLDER, Correspondance.NEWER);
        configureButtonAction(this.listButton[3], Correspondance.ORPHAN, Correspondance.ORPHAN);
        configureButtonAction(this.listButton[4], Correspondance.SAME, Correspondance.SAME);
        configureButtonActionFolder(this.listButton[5]);
    }
    
    private void configureButtonActionAll(ToggleButton b)
    {
        b.setOnAction((ActionEvent event) -> {
            this.listCorrespLeft.clear();
            this.listCorrespRight.clear();
            this.nb_left[0] = 0;
            this.nb_left[1] = 0;
            this.nb_left[2] = 0;
            this.nb_left[3] = 0;
            this.nb_left[4] = 0;
            this.vm.getModel().getD1().selectAll(this.vm.getModel().getD1());
            this.vm.getModel().getD2().selectAll(this.vm.getModel().getD2());
            System.out.println("\n\n\n Directory left \n\n\n" + this.vm.getModel().getD1());
            System.out.println("\n\n\n Directory right \n\n\n" + this.vm.getModel().getD2());
            this.t1 = new TreeItem(this.vm.getModel().getD1());
            this.t2 = new TreeItem(this.vm.getModel().getD2());
            this.v1 = new TreeTableView<>(t1);
            this.v2 = new TreeTableView<>(t2);
            configureV(v1);
            configureV(v2);
            this.folders.getChildren().clear();
            this.folders.getChildren().addAll(v1, v2);
        });
    }
    
    private void configureButtonAction(ToggleButton b, Correspondance c1, Correspondance c2)
    {
        b.setOnAction((ActionEvent event) -> {
            if(c1 == Correspondance.SAME)
            {
                if (nb_left[0] == 0)
                {
                    this.listCorrespLeft.add(c1);
                    this.listCorrespRight.add(c2);
                    nb_left[0] = 1;
                }
                else
                {
                    this.listCorrespLeft.remove(c1);
                    this.listCorrespRight.remove(c2);
                    nb_left[0] = 0;
                }
            }
            
            if(c1 == Correspondance.NEWER)
            {
                if (nb_left[1] == 0)
                {
                    this.listCorrespLeft.add(c1);
                    this.listCorrespRight.add(c2);
                    nb_left[1] = 1;
                }
                else
                {
                    this.listCorrespLeft.remove(c1);
                    this.listCorrespRight.remove(c2);
                    nb_left[1] = 0;
                }
            }
            
            if(c1 == Correspondance.OLDER && nb_left[2] == 0)
            {
                if (nb_left[2] == 0)
                {
                    this.listCorrespLeft.add(c1);
                    this.listCorrespRight.add(c2);
                    nb_left[2] = 1;
                }
                else
                {
                    this.listCorrespLeft.remove(c1);
                    this.listCorrespRight.remove(c2);
                    nb_left[2] = 0;
                }
            }
            
            if(c1 == Correspondance.ORPHAN)
            {
                if (nb_left[3] == 0)
                {
                    this.listCorrespLeft.add(c1);
                    this.listCorrespRight.add(c2);
                    nb_left[3] = 1;
                }
                else
                {
                    this.listCorrespLeft.remove(c1);
                    this.listCorrespRight.remove(c2);
                    nb_left[3] = 0;
                }
            }
            this.vm.getModel().getD1().selectNan(this.vm.getModel().getD1());
            this.vm.getModel().getD2().selectNan(this.vm.getModel().getD2());
            System.out.println("\n\n\n Directory left unselected\n\n\n" + this.vm.getModel().getD1());
            System.out.println("\n\n\n Directory right unselected\n\n\n" + this.vm.getModel().getD2());
            for (int i = 0 ; i < this.listCorrespLeft.size() ; ++i)
            {
                this.vm.getModel().getD1().select(this.vm.getModel().getD1(), this.listCorrespLeft.get(i));
                this.vm.getModel().getD2().select(this.vm.getModel().getD2(), this.listCorrespRight.get(i));
            }
            if (nb_left[0] == 0 && nb_left[1] == 0 && nb_left[2] == 0 && nb_left[3] == 0)
            {
                this.vm.getModel().getD1().selectAll(this.vm.getModel().getD1());
                this.vm.getModel().getD2().selectAll(this.vm.getModel().getD2());
            }
            System.out.println("Same left : " + nb_left[0] + "\n"
                                + "Newer left : " + nb_left[1] + "\n"
                                + "Older left : " + nb_left[2] + "\n"
                                + "Orphan left : " + nb_left[3] + "\n");
            System.out.println("\n\n\n Directory left selected\n\n\n" + this.vm.getModel().getD1());
            System.out.println("\n\n\n Directory right selected\n\n\n" + this.vm.getModel().getD2());
            final TreeItem t11 = new TreeItem(this.vm.getModel().getD1());
            final TreeItem t22 = new TreeItem(this.vm.getModel().getD2());
            final TreeTableView v11 = new TreeTableView<>(t11);
            final TreeTableView v22 = new TreeTableView<>(t22);
            configureV(v11);
            configureV(v22);
            this.folders.getChildren().clear();
            this.folders.getChildren().addAll(v11, v22);
        });
    }
    
    private void configureButtonActionFolder(ToggleButton b)
    {
        b.setOnAction((ActionEvent event) -> {
            if (nb_left[4] == 0)
            {
                this.vm.getModel().getD1().selectFolder(this.vm.getModel().getD1());
                this.vm.getModel().getD2().selectFolder(this.vm.getModel().getD2());
            }
            else
            {
                for (int i = 0 ; i < this.listCorrespLeft.size() ; ++i)
                {
                    this.vm.getModel().getD1().select(this.vm.getModel().getD1(), this.listCorrespLeft.get(i));
                    this.vm.getModel().getD2().select(this.vm.getModel().getD2(), this.listCorrespRight.get(i));
                }
                if (nb_left[0] == 0 && nb_left[1] == 0 && nb_left[2] == 0 && nb_left[3] == 0)
                {
                    this.vm.getModel().getD1().selectAll(this.vm.getModel().getD1());
                    this.vm.getModel().getD2().selectAll(this.vm.getModel().getD2());
                }
            }
            System.out.println("\n\n\n" + this.vm.getModel().getD1());
            final TreeItem t11 = new TreeItem(this.vm.getModel().getD1());
            final TreeItem t22 = new TreeItem(this.vm.getModel().getD2());
            final TreeTableView v11 = new TreeTableView<>(t11);
            final TreeTableView v22 = new TreeTableView<>(t22);
            configureV(v11);
            configureV(v22);
            this.folders.getChildren().clear();
            this.folders.getChildren().addAll(v11, v22);
        });
    }
    
    private void configureChooser1(Button chooser, Stage primaryStage)
    {
        ImageView folder1 = new ImageView(new Image(getClass().getResourceAsStream("./folder.png")));
	chooser.setGraphic(folder1);
        chooser.setOnAction((ActionEvent event) -> {
            Stage chooseStage = new Stage();
            File f = this.directoryChooser.showDialog(chooseStage);
            if (f != null){
                final FileOrDirectory fod = copy1(f, "./");
                final Model model = new Model(fod, this.vm.getModel().getD2());
                final ViewModel view_model = new ViewModel(model);
                View v = new View(primaryStage, view_model);
            }
        });
    }
    
    private void configureChooser2(Button chooser, Stage primaryStage)
    {
        ImageView folder2 = new ImageView(new Image(getClass().getResourceAsStream("./folder.png")));
        chooser.setGraphic(folder2);
        chooser.setOnAction((ActionEvent event) -> {
            Stage chooseStage = new Stage();
            File f = this.directoryChooser.showDialog(chooseStage);
            if (f != null){
                final FileOrDirectory fod = copy1(f, "./");
                final Model model = new Model(this.vm.getModel().getD1(), fod);
                final ViewModel view_model = new ViewModel(model);
                View v = new View(primaryStage, view_model);
            }
        });
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