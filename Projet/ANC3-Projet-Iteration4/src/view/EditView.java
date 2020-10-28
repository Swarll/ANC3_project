package view;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import viewModel.EditVM;

public class EditView {
    private final int width = 700;
    private final int height = 500;
    private final BorderPane box = new BorderPane();
    private final Label label = new Label();
    private final TextArea content_area = new TextArea();
    private final Button save = new Button("save");
    private final EditVM editVM;
    
    public EditView(Stage stage, EditVM editVM)
    {
        this.editVM = editVM;
        this.label.setText("size : " + editVM.getFileSize());
        
        configureBinding();      
        configureComponent();
        
        Scene scene = new Scene(box, width, height);
        stage.setTitle("FileEditor : " + editVM.getFileName());
        stage.setScene(scene);
    }
    
    private void configureComponent()
    {
        label.setMinHeight(40);
        
        configureTextArea(this.label);
        configureButton();
        configureBox();     
    }
    
    private void configureBox()
    {
        this.box.setCenter(this.content_area);
        this.box.setTop(label);
        this.box.setBottom(save);
    }
    
    private void configureTextArea(Label label)
    {
        this.content_area.textProperty().addListener((obs, old, nw) -> {
            label.setText("size : " + nw.length());
        });
    }
    
    private void configureButton()
    {
        this.save.setOnAction(event -> {
            this.editVM.save();
        });  
    }
    
    private void configureBinding()
    {
        this.content_area.textProperty().bindBidirectional(this.editVM.contentProperty());
    }
}
