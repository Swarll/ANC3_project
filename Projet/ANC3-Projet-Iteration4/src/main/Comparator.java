package main;

import utils.FileCopier;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.stage.Stage;
import model.FileOrDirectory;
import model.Model;
import view.View;
import viewModel.ViewModel;

public class Comparator extends Application{
    
    //****************************************************************************************************************************************
    //****************************************************************************************************************************************
    // Lance le programme
    //****************************************************************************************************************************************
    //****************************************************************************************************************************************
    @Override
    public void start(Stage primaryStage) 
    {
        try {
            File f1, f2;
            f1 = new File("TestBC/Rootbc_left");
            f2 = new File("TestBC/Rootbc_right");
            FileCopier fc = new FileCopier();
            FileOrDirectory fod1 = fc.copy(f1, "./");
            FileOrDirectory fod2 = fc.copy(f2, "./");
            Model model = new Model(fod1, fod2);
            ViewModel vm = new ViewModel(model);
            View v = new View(primaryStage, vm);
            primaryStage.show();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Comparator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}