package miniproject;

import java.util.Arrays;
import java.util.List;
import javafx.application.Application;
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

public class MiniProject extends Application {

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

    private static final List<String> INIT_DATA = Arrays.asList(
            "Jouer à SuperMario",
            "Traîner sur FaceBook",
            "Revoir Pro2",
            "Twitter",
            "Travailler au projet Anc3",
            "Regarder du foot",
            "Ecouter de la musique"
    );
    
    private final Model model = new Model(INIT_DATA);

    @Override
    public void start(Stage primaryStage) {
        initData();
        configComponents();
        configListeners();

        Parent root = setRoot();
        Scene scene = new Scene(root, 800, 400);

        primaryStage.setTitle("MiniProject V0");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void initData() {
        toDoList.getItems().clear();
        doneList.getItems().clear();
        toDoList.getItems().addAll(model.getToDoList());
        doneList.getItems().addAll(model.getDoneList());
    }

    private void configComponents() {
        lBox.getChildren().addAll(toDoLabel, toDoList);
        lBox.setAlignment(Pos.CENTER);
        lBox.setSpacing(5);
        lBox.setPrefWidth(250);
        cBox.getChildren().addAll(setDone, setToDo);
        cBox.setSpacing(20);
        cBox.setAlignment(Pos.CENTER);
        rBox.getChildren().addAll(doneLabel, doneList);
        rBox.setAlignment(Pos.CENTER);
        rBox.setSpacing(5);
        rBox.setPrefWidth(250);
        addBox.getChildren().addAll(addLabel, addText, addButton);
        addBox.setAlignment(Pos.CENTER);
        addBox.setSpacing(20);
        addBox.setPrefWidth(250);
        setDone.setPrefWidth(60);
        setToDo.setPrefWidth(60);
        addButton.setPrefWidth(60);
        setToDo.setDisable(true);
        setDone.setDisable(true);
        addButton.setDisable(true);
    }

    private void configListeners() {
        setDone.setOnAction(e -> {
            transfer(toDoList, doneList, true);
        });

        setToDo.setOnAction(e -> {
            transfer(doneList, toDoList, false);
        });

        toDoList.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                transfer(toDoList, doneList, true);
            }
        });

        doneList.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                transfer(doneList, toDoList, false);
            }
        });

        toDoList.getSelectionModel().selectedIndexProperty().addListener((obs, old, act) -> {
            setDone.setDisable((int) act == -1);
        });

        doneList.getSelectionModel().selectedIndexProperty().addListener((obs, old, act) -> {
            setToDo.setDisable((int) act == -1);
        });

        addButton.setOnAction(e -> {
            model.addToDo(addText.getText());
            initData();
            addText.setText("");
        });

        addText.setOnKeyPressed(e -> {
            if (e.getCode().equals(KeyCode.ENTER)) {
                if ((model.addToDo(addText.getText())) == true)
                {
                    initData();
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

    private Parent setRoot() {
        HBox root = new HBox();
        root.setPadding(new Insets(10));
        root.setSpacing(10);
        root.getChildren().addAll(addBox, lBox, cBox, rBox);
        return root;
    }

    private void transfer(ListView<String> fromList, ListView<String> toList, boolean ok) {
        final int index = fromList.getSelectionModel().getSelectedIndex();
        if (index != -1) {
            if (ok == true)
                model.setDone(index);
            else
                model.setToDo(index);
            initData();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}
