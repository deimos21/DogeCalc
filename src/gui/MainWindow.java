package gui;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.DogeCount;
import model.ErrorsHandler;

/**
 *
 * @author Marcin Gordel
 */
public class MainWindow extends Application {
    
    private DogeCount dc;
    
    private Label ballanceL;
    private Label profitMainL;
    private Label profit2L;
    private Label profit3L;
    private Label electricityCostL;
    private Label btcusdL;
    private Label dogebtcL;
    private Label statusL;
    private Label refreshL;
    private Circle infoDotC;
    
    @Override
    public void start(Stage primaryStage) throws IOException {
        
        dc = new DogeCount();
        
        
           
        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));

        Scene scene = new Scene(root, 405, 392);

        primaryStage.setResizable(false);
        primaryStage.setTitle("DogeCount");
        primaryStage.setScene(scene);
        primaryStage.show();
        
        
        Font.loadFont(
          MainWindow.class.getResource("/fonts/ERASDEMI.TTF").toExternalForm(), 
          10
        );
        Font.loadFont(
          MainWindow.class.getResource("/fonts/segoepr.ttf").toExternalForm(), 
          10
        );
        
        final SettingsWindow sw = new SettingsWindow(dc.getSs());
        sw.getDialog().initOwner(primaryStage);
        
        Button editButton = (Button) root.lookup("#settingsBt");
        if (editButton!=null) editButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                sw.getDialog().show();
            }
        });
        
        Button helpButton = (Button) root.lookup("#helpBt");
        if (helpButton!=null) helpButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                //TODO
            }
        });
        
        ballanceL = (Label) root.lookup("#ballanceL");
        profitMainL = (Label) root.lookup("#profitMainL");
        profit2L = (Label) root.lookup("#profit2L");
        profit3L = (Label) root.lookup("#profit3L");
        electricityCostL = (Label) root.lookup("#electricityCostL");
        btcusdL = (Label) root.lookup("#btcusdL");
        dogebtcL = (Label) root.lookup("#dogebtcL");
        statusL = (Label) root.lookup("#statusL");
        refreshL = (Label) root.lookup("#refreshL");
        infoDotC = (Circle) root.lookup("#infoDot");
        
        dc.logger.addHandler(new ErrorsHandler(statusL,infoDotC));
        
        
        TimerTask task = new TimerTask() {
            int timeDown=dc.getRefreshTime();
            int refreshTime=timeDown;
            @Override
            public void run() {
                Platform.runLater(new Runnable() {
                    public void run() {
                        if(!sw.updateLock)
                            if(timeDown==0){
                                infoDotC.setFill(Color.LIMEGREEN);
                                refreshL.setText("AKTUALIZACJA");
                                statusL.setText("Połaczenie aktywne");
                                update();
                                String timeStamp = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
                                System.out.println("AKTUALIZACJA "+timeStamp);
                                timeDown=dc.getRefreshTime();
                                
                            }else{
                                refreshL.setText("Odświeżanie za: "+timeDown+"s");
                                timeDown--;
                            }
                    }
                });
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 0, 1*1000);
        
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent we) {
                timer.cancel();
                sw.getDialog().close();
                System.out.println("Goodbye");
            }
        });  
        
        if (sw.saveButton!=null) sw.saveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(sw.saveSettings()){
                    sw.updateLock=false;
                    sw.getDialog().close();
                    updateSettings();
                }
            }
        });
             
    }
    
    private void update(){
        dc.update();
        ballanceL.setText(formatter(dc.getBalance(),"doge"));
        profitMainL.setText(formatter(dc.getProfit(),"zl"));
        profit2L.setText(formatter(dc.getProfit2(),"dolar"));
        profit3L.setText(formatter(dc.getProfit3(),"euro"));
        electricityCostL.setText(formatter(dc.getElectricityCost(),"zl"));
        btcusdL.setText(formatter(dc.getPriceBTCUSD(),"btc"));
        dogebtcL.setText(formatter(dc.getPriceDOGEBTC(),"satoshi"));
    }
    
    private void updateSettings(){
        dc.updateSettings();
        ballanceL.setText(formatter(dc.getBalance(),"doge"));
        profitMainL.setText(formatter(dc.getProfit(),"zl"));
        profit2L.setText(formatter(dc.getProfit2(),"dolar"));
        profit3L.setText(formatter(dc.getProfit3(),"euro"));
        electricityCostL.setText(formatter(dc.getElectricityCost(),"zl"));
        btcusdL.setText(formatter(dc.getPriceBTCUSD(),"btc"));
        dogebtcL.setText(formatter(dc.getPriceDOGEBTC(),"satoshi"));
    }
    
    private String formatter(double value, String type){
        String output="";
        DecimalFormat df;
        switch (type) {
            case "doge": 
                df = new DecimalFormat("#,###,###");
                output = df.format(value)+" DOGE";
                break;
            case "zl":  
                df = new DecimalFormat("#,###,###,##0.00");
                output = df.format(value)+" zł";
                break;
            case "dolar":  
                df = new DecimalFormat("#,###,###,##0.00");
                output = df.format(value)+" $";
                break;
            case "euro":  
                df = new DecimalFormat("#,###,###,##0.00");
                output = df.format(value)+" €";
                break;
            case "btc":  
                df = new DecimalFormat("#,###,###,##0.00");
                output = df.format(value);
                break;
            case "satoshi":  
                double sat = value*100000000;
                df = new DecimalFormat("#,###,###");
                output = df.format(sat);
                break;
            default:
                
        }
        return output;
    }
    
    
    public static void main(String[] args) {
        launch(args);
    }
}
