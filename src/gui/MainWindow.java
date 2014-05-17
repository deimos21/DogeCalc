package gui;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
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
    
    private void preload(Stage primaryStage) throws IOException{
        
        Parent preloader = FXMLLoader.load(getClass().getResource("preloader.fxml"));
        Scene scenePreloader = new Scene(preloader);
        primaryStage.setScene(scenePreloader);
        
        final ProgressBar pb = (ProgressBar) preloader.lookup("#progressBar");
        final Label info = (Label) preloader.lookup("#info");
        
        IntegerProperty progressLoad = new SimpleIntegerProperty(0);
        
        progressLoad.addListener(new ChangeListener(){
            @Override public void changed(ObservableValue o,Object oldVal, 
                     Object newVal){
                 switch((int)newVal){
                     case 5:
                         info.setText("Ładowanie pliku ustawień");
                     break;
                     case 15:
                         info.setText("Ładowanie ustawień");
                     break;
                     case 20:
                         info.setText("Budowanie głównego interfejsu");
                     break;
                     case 30:
                         info.setText("Pobieranie aktualnych kursów");
                     break;
                     case 70:
                         info.setText("Uruchamianie aplikacji");
                     break;
                 }
                 pb.setProgress((double)((int)newVal)/100);
                 System.out.println((double)((int)newVal)+"%");
            }
        });
        
        primaryStage.show();
        
        dc = new DogeCount();
                
        dc.progressLoad.addListener(new ChangeListener(){
            @Override public void changed(ObservableValue o,Object oldVal, 
                     Object newVal){
                 progressLoad.set((int) newVal);
            }
        });
        
        dc.init();
        
        Font.loadFont(MainWindow.class.getResource("/fonts/ERASDEMI.TTF").toExternalForm(), 10);
        Font.loadFont(MainWindow.class.getResource("/fonts/segoepr.ttf").toExternalForm(),10);
        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
        
        Scene sceneRoot = new Scene(root);
        
        //PROGRESSS 
        progressLoad.set(25);
        
        primaryStage.setResizable(false);
        primaryStage.setTitle("DogeCount");
                
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
        
        Button helpButton = (Button) root.lookup("#helpBt");
        if (helpButton!=null) helpButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                //TODO
            }
        });
        
        dc.logger.addHandler(new ErrorsHandler(statusL,infoDotC));
        
        //PROGRESSS 
        progressLoad.set(30);
        
        final SettingsWindow sw = new SettingsWindow(dc.getSs());
        sw.getDialog().initOwner(primaryStage);
        
        Button editButton = (Button) root.lookup("#settingsBt");
        if (editButton!=null) editButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                sw.getDialog().show();
            }
        });
        
        //PROGRESSS 
        progressLoad.set(70);
        
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
        
        update();
        
        //PROGRESSS 
        progressLoad.set(100);
        
        primaryStage.setScene(sceneRoot);
    }
    
    @Override
    public void start(Stage primaryStage) throws IOException {
        
        preload(primaryStage);
        
//        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
//
//        Scene scene = new Scene(root);

//        primaryStage.setResizable(false);
//        primaryStage.setTitle("DogeCount");
//        primaryStage.setScene(scene);
        //primaryStage.show();
        
        
//        Font.loadFont(MainWindow.class.getResource("/fonts/ERASDEMI.TTF").toExternalForm(), 10);
//        Font.loadFont(MainWindow.class.getResource("/fonts/segoepr.ttf").toExternalForm(),10);
        
//        final SettingsWindow sw = new SettingsWindow(dc.getSs());
//        sw.getDialog().initOwner(primaryStage);
//        
//        Button editButton = (Button) root.lookup("#settingsBt");
//        if (editButton!=null) editButton.setOnAction(new EventHandler<ActionEvent>() {
//
//            @Override
//            public void handle(ActionEvent event) {
//                sw.getDialog().show();
//            }
//        });
        
//        Button helpButton = (Button) root.lookup("#helpBt");
//        if (helpButton!=null) helpButton.setOnAction(new EventHandler<ActionEvent>() {
//
//            @Override
//            public void handle(ActionEvent event) {
//                //TODO
//            }
//        });
        
//        ballanceL = (Label) root.lookup("#ballanceL");
//        profitMainL = (Label) root.lookup("#profitMainL");
//        profit2L = (Label) root.lookup("#profit2L");
//        profit3L = (Label) root.lookup("#profit3L");
//        electricityCostL = (Label) root.lookup("#electricityCostL");
//        btcusdL = (Label) root.lookup("#btcusdL");
//        dogebtcL = (Label) root.lookup("#dogebtcL");
//        statusL = (Label) root.lookup("#statusL");
//        refreshL = (Label) root.lookup("#refreshL");
//        infoDotC = (Circle) root.lookup("#infoDot");
        
//        dc.logger.addHandler(new ErrorsHandler(statusL,infoDotC));
        
        
//        TimerTask task = new TimerTask() {
//            int timeDown=dc.getRefreshTime();
//            int refreshTime=timeDown;
//            @Override
//            public void run() {
//                Platform.runLater(new Runnable() {
//                    public void run() {
//                        if(!sw.updateLock)
//                            if(timeDown==0){
//                                infoDotC.setFill(Color.LIMEGREEN);
//                                refreshL.setText("AKTUALIZACJA");
//                                statusL.setText("Połaczenie aktywne");
//                                update();
//                                String timeStamp = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
//                                System.out.println("AKTUALIZACJA "+timeStamp);
//                                timeDown=dc.getRefreshTime();
//                                
//                            }else{
//                                refreshL.setText("Odświeżanie za: "+timeDown+"s");
//                                timeDown--;
//                            }
//                    }
//                });
//            }
//        };
//        Timer timer = new Timer();
//        timer.schedule(task, 0, 1*1000);
//        
//        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
//            @Override
//            public void handle(WindowEvent we) {
//                timer.cancel();
//                sw.getDialog().close();
//                System.out.println("Goodbye");
//            }
//        });  
//        
//        if (sw.saveButton!=null) sw.saveButton.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                if(sw.saveSettings()){
//                    sw.updateLock=false;
//                    sw.getDialog().close();
//                    updateSettings();
//                }
//            }
//        });
             
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
