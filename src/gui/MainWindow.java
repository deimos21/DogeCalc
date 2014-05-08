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
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.DogeCount;

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
    
    @Override
    public void start(Stage primaryStage) throws IOException {
        
        dc = new DogeCount();
           
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
        
        final int refreshTime = 10;
        
        TimerTask task = new TimerTask() {
            int timeDown=refreshTime;
            @Override
            public void run() {
                Platform.runLater(new Runnable() {
                    public void run() {
                        if(timeDown==0){
                            refreshL.setText("AKTUALIZACJA");
                            update();
                            String timeStamp = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
                            System.out.println("AKTUALIZACJA "+timeStamp);
                            timeDown=refreshTime;
                        }else{
                            refreshL.setText("Odświeżanie za: "+timeDown+"s");
                            timeDown--;
                        }
                    }
                });
            }
        };
        final Timer timer = new Timer();
        timer.schedule(task, 0, 1*1000);
        
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent we) {
                timer.cancel();
                System.out.println("Goodbye");
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
