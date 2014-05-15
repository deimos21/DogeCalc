package gui;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import model.DataDownloader;
import model.Settings;
import model.SettingsStorage;
import model.Validator;

/**
 *
 * @author Marcin Gordel
 */
public class SettingsWindow {
    
    private final Stage dialog;
    private final SettingsStorage ss;
    private MessageBox mb;
    
    TextField addressAccountTF;
    ChoiceBox currencyCB;
    ChoiceBox dogeStockCB;
    ChoiceBox btcStockCB;
    TextField powerTF;
    TextField powerCostTF;
    DatePicker dateStartTF;
    CheckBox isConstElectricityCostCB;
    TextField constElectricityCostTF;
    TextField refreshTimeTF;
    protected Button saveButton;
    
    public boolean updateLock=false;
    
    
    public SettingsWindow(SettingsStorage ss) throws IOException{
        dialog = new Stage();
        dialog.initStyle(StageStyle.UTILITY);
        dialog.setResizable(false);
        dialog.setTitle("Ustawienia");
        Parent root = FXMLLoader.load(getClass().getResource("edit.fxml"));
        Scene sceneEdit = new Scene(root, 419, 525);
        dialog.setScene(sceneEdit);
        
        mb = new MessageBox();
        
        this.ss = ss;
        
        addressAccountTF = (TextField) root.lookup("#addressAccount");
        currencyCB = (ChoiceBox) root.lookup("#currency");
        dogeStockCB = (ChoiceBox) root.lookup("#dogeStock");
        btcStockCB = (ChoiceBox) root.lookup("#btcStock");
        powerTF = (TextField) root.lookup("#power");
        powerCostTF = (TextField) root.lookup("#powerCost");
        dateStartTF = (DatePicker) root.lookup("#dateStart");
        isConstElectricityCostCB = (CheckBox) root.lookup("#isConstElectricityCost");
        constElectricityCostTF = (TextField) root.lookup("#constElectricityCost");
        refreshTimeTF = (TextField) root.lookup("#refreshTime");
        
        currencyCB.setItems(FXCollections.observableArrayList(DataDownloader.getAvailableCurrencies()));
        currencyCB.setValue(DataDownloader.getAvailableCurrencies().get(0));
        dogeStockCB.setItems(FXCollections.observableArrayList(DataDownloader.getAvailableDogeStocks()));
        dogeStockCB.setValue(DataDownloader.getAvailableDogeStocks().get(0));
        btcStockCB.setItems(FXCollections.observableArrayList(DataDownloader.getAvailableBtcStocks()));
        btcStockCB.setValue(DataDownloader.getAvailableBtcStocks().get(0));
        
        loadSettings();
        
        saveButton = (Button) root.lookup("#saveButton");
        
        
        dialog.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent we) {
                updateLock=false;
            }
        });  
        
        dialog.setOnShowing(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent we) {
                updateLock=true;
            }
        });  
        
        isConstElectricityCostCB.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                if(t){
                    powerTF.setDisable(false);
                    powerCostTF.setDisable(false);
                    dateStartTF.setDisable(false);
                    
                    constElectricityCostTF.setDisable(true);
                }
                if(t1){
                    powerTF.setDisable(true);
                    powerCostTF.setDisable(true);
                    dateStartTF.setDisable(true);
                    
                    constElectricityCostTF.setDisable(false);
                    
                }
            }
        });
    }

    public Stage getDialog() {
        return dialog;
    }
        
    protected boolean saveSettings(){
        Settings sets = new Settings();
        
        //Validation
        Validator validator = new Validator();
        String errorsMsg="";
        String resVal = validator.validate("address", addressAccountTF.getText());
        if(resVal==null){
            sets.setAddressAccount(addressAccountTF.getText());
        }else{
            errorsMsg += resVal+"\n";
        }
        sets.setCurrency(currencyCB.getValue().toString());
        sets.setBtcStock(btcStockCB.getValue().toString());
        sets.setDogeStock(dogeStockCB.getValue().toString());
        
        resVal = validator.validate("power", powerTF.getText());
        if(resVal==null){
            sets.setPower(Double.parseDouble(powerTF.getText()));
        }else{
            errorsMsg += resVal+"\n";
        }
        
        resVal = validator.validate("powerCost", powerCostTF.getText());
        if(resVal==null){
            sets.setPowerCost(Double.parseDouble(powerCostTF.getText()));
        }else{
            errorsMsg += resVal+"\n";
        }
        
        resVal = validator.validate("dateStart", dateStartTF.getValue());
        if(resVal==null){
            LocalDate lDate = dateStartTF.getValue();
            Instant instant = lDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
            sets.setDateStart(Date.from(instant));
        }else{
            errorsMsg += resVal+"\n";
        }
        
        resVal = validator.validate("constCost", constElectricityCostTF.getText());
        if(resVal==null){
            sets.setConstElectricityCost(Double.parseDouble(constElectricityCostTF.getText()));
        }else{
            errorsMsg += resVal+"\n";
        }
        
        sets.setIsConstElectricityCost(isConstElectricityCostCB.isSelected());
       
        resVal = validator.validate("updateTime", refreshTimeTF.getText());
        if(resVal==null){
            sets.setRefreshTime(Integer.parseInt(refreshTimeTF.getText()));
        }else{
            errorsMsg += resVal+"\n";
        }
        
        ss.serializeSettings(sets);
        
        if("".equals(errorsMsg)){
            return true;
        }else{
            mb.show(errorsMsg);
            return false;
        }
    }
    
    private void loadSettings(){
        Settings sets = ss.deserialzeSettings();
        
        if(sets.getAddressAccount()!=null)addressAccountTF.setText(sets.getAddressAccount());
        if(sets.getCurrency()!=null)currencyCB.setValue(sets.getCurrency());
        if(sets.getBtcStock()!=null)btcStockCB.setValue(sets.getBtcStock());
        if(sets.getDogeStock()!=null)dogeStockCB.setValue(sets.getDogeStock());
        powerTF.setText(Double.toString(sets.getPower()));
        
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.FRANCE);
        otherSymbols.setDecimalSeparator('.');
        DecimalFormat df = new DecimalFormat("#0.00", otherSymbols);
        
        powerCostTF.setText(df.format(sets.getPowerCost()));
        if(sets.getDateStart()!=null){
            Instant instant = Instant.ofEpochMilli(sets.getDateStart().getTime());
            dateStartTF.setValue(LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalDate());
        }
        isConstElectricityCostCB.setSelected(sets.isIsConstElectricityCost());
        if(sets.isIsConstElectricityCost()){
            powerTF.setDisable(true);
            powerCostTF.setDisable(true);
            dateStartTF.setDisable(true);

            constElectricityCostTF.setDisable(false);
        }
        constElectricityCostTF.setText(df.format(sets.getConstElectricityCost()));
        refreshTimeTF.setText(Integer.toString(sets.getRefreshTime()));
    }
}
