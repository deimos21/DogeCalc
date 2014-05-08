package gui;

import java.io.IOException;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.DogeCount;
import model.Settings;
import model.SettingsStorage;

/**
 *
 * @author Marcin Gordel
 */
public class MainWindow extends Application {
    
    private DogeCount dc;
    
    
    @Override
    public void start(Stage primaryStage) throws IOException {
           
        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));

        Scene scene = new Scene(root, 405, 392);

        primaryStage.setResizable(false);
        primaryStage.setTitle("DogeCount");
        primaryStage.setScene(scene);
        primaryStage.show();
        
        final SettingsWindow sw = new SettingsWindow();
        
        Button editButton = (Button) root.lookup("#settingsBt");
        if (editButton!=null) editButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                sw.getDialog().show();
            }
        });
        
    }
    
    
    public static void main(String[] args) {
        launch(args);
    }
}
