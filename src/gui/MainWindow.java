package gui;

import model.DogeCount;

import java.io.IOException;
import javafx.application.Application;
import static javafx.application.ConditionalFeature.FXML;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Marcin Gordel
 */
public class MainWindow extends Application {
    
    private Stage dialog;
    
    @Override
    public void start(Stage primaryStage) throws IOException {
        
        DogeCount dc = new DogeCount();
                
        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));

        Scene scene = new Scene(root, 390, 254);

        primaryStage.setResizable(false);
        primaryStage.setTitle("DogeCount");
        primaryStage.setScene(scene);
        primaryStage.show();
        
        dialog = new Stage();
        dialog.initStyle(StageStyle.UTILITY);
        dialog.setResizable(false);
        dialog.setTitle("Ustawienia");
        Parent editRoot = FXMLLoader.load(getClass().getResource("edit.fxml"));
        Scene sceneEdit = new Scene(editRoot, 419, 435);
        dialog.setScene(sceneEdit);
        
        Button editButton = (Button) root.lookup("#editButton");
        if (editButton!=null) editButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                dialog.show();
            }
        });
        
        Label balanceLabel = (Label) root.lookup("#balanceLabel");
        if (balanceLabel!=null) balanceLabel.setText(dc.getBalanceString()+" DOGE");
    }
    
    
    public static void main(String[] args) {
        launch(args);
    }
}
