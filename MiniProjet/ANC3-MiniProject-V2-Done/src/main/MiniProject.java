package main;

import ctrl.Ctrl;
import java.util.Arrays;
import java.util.List;
import javafx.application.Application;
import javafx.stage.Stage;
import model.Model;
import view.View;

public class MiniProject extends Application {
    
    public void start(Stage primaryStage) {
        Model model = new Model();
        Ctrl ctrl = new Ctrl(model);
        View view = new View(primaryStage, ctrl);
        model.addObserver(view);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
