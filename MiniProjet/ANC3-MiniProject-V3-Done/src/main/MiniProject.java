package main;

import mvvm.ViewModel;
import javafx.application.Application;
import javafx.stage.Stage;
import model.Model;
import view.View;

public class MiniProject extends Application {
    
    public void start(Stage primaryStage) {
        Model model = new Model();
        ViewModel vm = new ViewModel(model);
        View view = new View(primaryStage, vm);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
