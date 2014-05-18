package gui;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import model.DogeCalc;
import model.ErrorsHandler;

/**
 *
 * @author Marcin Gordel
 */
public class MainWindow extends Application {
    
    private DogeCalc dc;
    
    private Label ballanceL;
    private Label profitMainL;
    private Label profit2L;
    private Label profit3L;
    private Label electricityCostL;
    private Label btcusdL;
    private Label dogebtcL;
    private Label statusL;
    private Label refreshL;
    private Label btcStockName;
    private Label dogeStockName;
    private Circle infoDotC;
    
    private void preload(Stage primaryStage) throws IOException{
        
        Parent preloader = FXMLLoader.load(getClass().getResource("preloader.fxml"));
        Scene scenePreloader = new Scene(preloader);
        primaryStage.setScene(scenePreloader);
        
        Font.loadFont(MainWindow.class.getResource("/fonts/ERASDEMI.TTF").toExternalForm(), 10);
        Font.loadFont(MainWindow.class.getResource("/fonts/segoepr.ttf").toExternalForm(),10);
        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
        
        final Scene sceneRoot = new Scene(root);
        
        final ProgressBar pb = (ProgressBar) preloader.lookup("#progressBar");
        final Label info = (Label) preloader.lookup("#info");
        
        IntegerProperty progressLoad = new SimpleIntegerProperty(0);
        
        primaryStage.show();
        
        progressLoad.addListener(new ChangeListener(){
            @Override public void changed(ObservableValue o,Object oldVal, 
                     Object newVal){
                 switch((int)newVal){
                     case 5:
                         info.setText("Ładowanie pliku ustawień");
                         System.out.print("Ładowanie pliku ustawień ");
                     break;
                     case 15:
                         info.setText("Ładowanie ustawień");
                         System.out.print("Ładowanie ustawień ");
                     break;
                     case 20:
                         info.setText("Budowanie głównego interfejsu");
                         System.out.print("Budowanie głównego interfejsu ");
                     break;
                     case 30:
                         info.setText("Pobieranie aktualnych kursów");
                         System.out.print("Pobieranie aktualnych kursów ");
                     break;
                     case 70:
                         info.setText("Uruchamianie aplikacji");
                         System.out.print("Uruchamianie aplikacji ");
                     break;
                     case 100:
                         Platform.runLater(() -> {
                             primaryStage.setScene(sceneRoot);
                            });
                         
                     break;
                 }
                 
                 pb.setProgress((double)((int)newVal)/100);
                 System.out.println(pb.getProgress()*100+"%");
            }
        });
        
        
        
        dc = new DogeCalc();
                
        dc.progressLoad.addListener(new ChangeListener(){
            @Override public void changed(ObservableValue o,Object oldVal, 
                     Object newVal){
                 progressLoad.set((int) newVal);
            }
        });
        
        dc.init();
                
        //PROGRESSS 
        progressLoad.set(25);
        
        primaryStage.setResizable(false);
        primaryStage.setTitle("DogeCalc");
        primaryStage.getIcons().add(new Image("/gui/img/dogecalc.png"));
                
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
        btcStockName = (Label) root.lookup("#btcStockName");
        dogeStockName = (Label) root.lookup("#dogeStockName");
        
        Stage aboutDialog = new Stage();
        aboutDialog.initStyle(StageStyle.UTILITY);
        aboutDialog.setResizable(false);
        aboutDialog.setTitle("O programie");
        aboutDialog.initOwner(primaryStage);
        Parent aboutRoot = FXMLLoader.load(getClass().getResource("about.fxml"));
        Scene sceneAbout = new Scene(aboutRoot);
        aboutDialog.setScene(sceneAbout);
        
        Hyperlink gordelHl = (Hyperlink) aboutRoot.lookup("#gordel");
        Hyperlink dogeAccountHl = (Hyperlink) aboutRoot.lookup("#dogeAccount");
        Hyperlink dogeHl = (Hyperlink) aboutRoot.lookup("#doge");
        Hyperlink btcHl = (Hyperlink) aboutRoot.lookup("#btc");
        Hyperlink currencyHl = (Hyperlink) aboutRoot.lookup("#currency");
        Button aboutOk = (Button) aboutRoot.lookup("#okButton");
        aboutOk.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent event) {
                System.out.println("OK");
                aboutDialog.close();
            }
        });
        
        setUrlOnAction(gordelHl);
        setUrlOnAction(dogeAccountHl);
        setUrlOnAction(dogeHl);
        setUrlOnAction(btcHl);
        setUrlOnAction(currencyHl);
        
        Button helpButton = (Button) root.lookup("#helpBt");
        if (helpButton!=null) helpButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                aboutDialog.show();
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
                                blinkControl();
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
        
        btcStockName.setText("BTC/USD ("+dc.getBtcStock()+")");
        dogeStockName.setText("DOGE/BTC ("+dc.getDogeStock()+")");
        
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
        
        
    }
    
    @Override
    public void start(Stage primaryStage) throws IOException {
        preload(primaryStage);
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
        btcStockName.setText("BTC/USD ("+dc.getBtcStock()+")");
        dogeStockName.setText("DOGE/BTC ("+dc.getDogeStock()+")");
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
    
    private void blinkControl(){
        Color colorBlink = Color.web("#16507c");
        Color colorDefault = Color.BLACK;
        
        ballanceL.setTextFill(colorBlink);
        profitMainL.setTextFill(colorBlink);
        electricityCostL.setTextFill(colorBlink);
        btcusdL.setTextFill(colorBlink);
        dogebtcL.setTextFill(colorBlink);
        
        
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                try {
                    Thread.sleep(400);
                } catch (InterruptedException ex) {
                    Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
                }
                ballanceL.setTextFill(colorDefault);
                profitMainL.setTextFill(colorDefault);
                electricityCostL.setTextFill(colorDefault);
                btcusdL.setTextFill(colorDefault);
                dogebtcL.setTextFill(colorDefault);
            }
        });
          
    }
    
    private void setUrlOnAction(Hyperlink hp){
        hp.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent e) {
                try {
                    Desktop.getDesktop().browse(new URI(hp.getText()));
                } catch (IOException ex) {
                    Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
                } catch (URISyntaxException ex) {
                    Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
