package model;

import java.util.logging.LogRecord;
import java.util.logging.StreamHandler;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 *
 * @author Marcin Gordel <marcin at gordel.pl>
 * @date 2014-05-14
 */
public class ErrorsHandler extends StreamHandler{
    Label infoLabel;
    Circle infoDot;

    public ErrorsHandler(Label infoLabel, Circle infoDot) {
        this.infoLabel = infoLabel;
        this.infoDot = infoDot;
    }
    
    @Override
    public void publish(LogRecord record) {
        if(infoLabel!=null)infoLabel.setText(record.getMessage());
        if(infoDot!=null)infoDot.setFill(Color.RED);
        super.publish(record);
    }
 
 
    @Override
    public void flush() {
        super.flush();
    }
 
 
    @Override
    public void close() throws SecurityException {
        super.close();
    }
}
