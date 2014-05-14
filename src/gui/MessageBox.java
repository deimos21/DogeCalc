package gui;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Marcin Gordel <marcin at gordel.pl>
 * @date 2014-05-14
 */
public class MessageBox {
    private final Stage dialog;
    
    public MessageBox() throws IOException{
        dialog = new Stage();
        dialog.initStyle(StageStyle.UTILITY);
        dialog.setResizable(false);
        dialog.setTitle("Informacja");
        Parent root = FXMLLoader.load(getClass().getResource("msgbox.fxml"));
        Scene sceneMsg = new Scene(root, 405, 213);
        dialog.setScene(sceneMsg);
        
        Button okButton = (Button) root.lookup("#okButton");
        if (okButton!=null) okButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                dialog.close();
            }
        });
    }
    
    public Stage getDialog() {
        return dialog;
    }
    
    private void setMsg(String msg){
        Label msgL = (Label) dialog.getScene().getRoot().lookup("#messageL");
        msgL.setText(msg);
    }
    
    public void show(String msg){
        setMsg(msg);
        dialog.show();
    }
        
}
