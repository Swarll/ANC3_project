package view;

import ctrl.Ctrl;
import java.util.Arrays;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Model;

public class View extends VBox implements Observer{
    private final ListView<String> toDoList = new ListView<>();
    private final ListView<String> doneList = new ListView<>();
    private final Label toDoLabel = new Label("À faire");
    private final Label doneLabel = new Label("C'est fait");
    private final Label addLabel = new Label("À ajouter : ");
    private final Button setDone = new Button(">>");
    private final Button setToDo = new Button("<<");
    private final Button addButton = new Button(">>");
    private final TextField addText = new TextField();
    private final VBox lBox = new VBox();
    private final VBox cBox = new VBox();
    private final VBox rBox = new VBox();
    private final VBox addBox = new VBox();
    
    private final Ctrl ctrl;
    
    public View(Stage primaryStage, Ctrl ctrl)
    {
        this.ctrl = ctrl;
        configComponents();
        configListeners();       
        Parent root = setRoot();
        
        Scene scene = new Scene(root, 800, 400);
        primaryStage.setTitle("MiniProject V0");
        primaryStage.setScene(scene);
    }
    
    private void configComponents() {
        configLBOX();
        configCBOX();
        configRBOX();
        configAddBOX();
        configButton();
    }
    
    private void configLBOX()
    {
        lBox.getChildren().addAll(toDoLabel, toDoList);
        lBox.setAlignment(Pos.CENTER);
        lBox.setSpacing(5);
        lBox.setPrefWidth(250);
    }
    
    private void configCBOX()
    {
        cBox.getChildren().addAll(setDone, setToDo);
        cBox.setSpacing(20);
        cBox.setAlignment(Pos.CENTER);
    }
    
    private void configRBOX()
    {
        rBox.getChildren().addAll(doneLabel, doneList);
        rBox.setAlignment(Pos.CENTER);
        rBox.setSpacing(5);
        rBox.setPrefWidth(250);
    }
    
    private void configAddBOX()
    {
        addBox.getChildren().addAll(addLabel, addText, addButton);
        addBox.setAlignment(Pos.CENTER);
        addBox.setSpacing(20);
        addBox.setPrefWidth(250);
    }
    
    private void configButton()
    {
        setDone.setPrefWidth(60);
        setToDo.setPrefWidth(60);
        addButton.setPrefWidth(60);
        setToDo.setDisable(true);
        setDone.setDisable(true);
        addButton.setDisable(true);
    }
    
    private Parent setRoot() {
        HBox root = new HBox();
        root.setPadding(new Insets(10));
        root.setSpacing(10);
        root.getChildren().addAll(addBox, lBox, cBox, rBox);
        return root;
    }
    
    public void initData()
    {
        toDoList.getItems().clear();
        doneList.getItems().clear();
        toDoList.getItems().addAll(ctrl.getToDoList());
        doneList.getItems().addAll(ctrl.getDoneList());
    }

    private void configListeners() {
        configButton2();
        configList();
        configAddText();
    }
    
    private void configButton2()
    {
        setDone.setOnAction(e -> {
            ctrl.transfer(toDoList, doneList, true);
        });

        setToDo.setOnAction(e -> {
            ctrl.transfer(doneList, toDoList, false);
        });

        addButton.setOnAction(e -> {
            if ((ctrl.addToDo(addText.getText())) == true)
                addText.setText("");
        });
    }
    
    private void configList()
    {
        configToDoList();
        configDoneList();
    }
    
    private void configToDoList()
    {
        toDoList.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                ctrl.transfer(toDoList, doneList, true);
            }
        });

        toDoList.getSelectionModel().selectedIndexProperty().addListener((obs, old, act) -> {
            setDone.setDisable((int) act == -1);
        });
    }
    
    private void configDoneList()
    {
        doneList.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                ctrl.transfer(doneList, toDoList, false);
            }
        });
        
        doneList.getSelectionModel().selectedIndexProperty().addListener((obs, old, act) -> {
            setToDo.setDisable((int) act == -1);
        });
    }
    
    private void configAddText()
    {
        addText.setOnKeyPressed(e -> {
            if (e.getCode().equals(KeyCode.ENTER)) {
                if ((ctrl.addToDo(addText.getText())) == true)
                {
                    addText.setText("");
                }
            }
        });
        
        addText.textProperty().addListener((obs, old, act) -> {
            addButton.setDisable(act.length() <= 2);
        });
        
        addText.focusedProperty().addListener((obs, old, act) -> {
            if(!act)
                addText.requestFocus();
        });
    }

    @Override
    public void update(Observable o, Object o1) {
        initData();
    }
}
