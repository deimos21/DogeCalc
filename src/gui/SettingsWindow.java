package gui;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
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
    TextField dateStartTF;
    CheckBox isConstElectricityCostCB;
    TextField constElectricityCostTF;
    TextField refreshTimeTF;
    
    public SettingsWindow() throws IOException{
        dialog = new Stage();
        dialog.initStyle(StageStyle.UTILITY);
        dialog.setResizable(false);
        dialog.setTitle("Ustawienia");
        Parent root = FXMLLoader.load(getClass().getResource("edit.fxml"));
        Scene sceneEdit = new Scene(root, 419, 525);
        dialog.setScene(sceneEdit);
        
        mb = new MessageBox();
        
        ss = new SettingsStorage();
        
        addressAccountTF = (TextField) root.lookup("#addressAccount");
        currencyCB = (ChoiceBox) root.lookup("#currency");
        dogeStockCB = (ChoiceBox) root.lookup("#dogeStock");
        btcStockCB = (ChoiceBox) root.lookup("#btcStock");
        powerTF = (TextField) root.lookup("#power");
        powerCostTF = (TextField) root.lookup("#powerCost");
        dateStartTF = (TextField) root.lookup("#dateStart");
        isConstElectricityCostCB = (CheckBox) root.lookup("#isConstElectricityCost");
        constElectricityCostTF = (TextField) root.lookup("#constElectricityCost");
        refreshTimeTF = (TextField) root.lookup("#refreshTime");
        
        currencyCB.setItems(FXCollections.observableArrayList(DataDownloader.getAvaliableCurrencies()));
        currencyCB.setValue(DataDownloader.getAvaliableCurrencies().get(0));
        dogeStockCB.setItems(FXCollections.observableArrayList(DataDownloader.getAvaliableDogeStocks()));
        dogeStockCB.setValue(DataDownloader.getAvaliableDogeStocks().get(0));
        btcStockCB.setItems(FXCollections.observableArrayList(DataDownloader.getAvaliableBtcStocks()));
        btcStockCB.setValue(DataDownloader.getAvaliableBtcStocks().get(0));
        
        loadSettings();
        
        Button saveButton = (Button) root.lookup("#saveButton");
        if (saveButton!=null) saveButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                if(saveSettings()){
                    dialog.close();
                }
            }
        });
    }

    public Stage getDialog() {
        return dialog;
    }
        
    private boolean saveSettings(){
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
        sets.setPower(Double.parseDouble(powerCostTF.getText()));
        sets.setPowerCost(Double.parseDouble(powerCostTF.getText()));
        
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
	String dateInString = dateStartTF.getText();
        try {
            sets.setDateStart(formatter.parse(dateInString));
        } catch (ParseException ex) {
            Logger.getLogger(SettingsWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        sets.setIsConstElectricityCost(isConstElectricityCostCB.isSelected());
        sets.setConstElectricityCost(Double.parseDouble(constElectricityCostTF.getText()));
        sets.setRefreshTime(Integer.parseInt(refreshTimeTF.getText()));
        
        ss.serializeSettings(sets);
        
        return true;
    }
    
    private void loadSettings(){
        Settings sets = ss.deserialzeSettings();
        
        if(sets.getAddressAccount()!=null)addressAccountTF.setText(sets.getAddressAccount());
        if(sets.getCurrency()!=null)currencyCB.setValue(sets.getCurrency());
        if(sets.getBtcStock()!=null)btcStockCB.setValue(sets.getBtcStock());
        if(sets.getDogeStock()!=null)dogeStockCB.setValue(sets.getDogeStock());
        powerTF.setText(Double.toString(sets.getPower()));
        powerCostTF.setText(Double.toString(sets.getPowerCost()));
        //dateStartTF.setText(sets.getDateStart().toString());
        isConstElectricityCostCB.setSelected(sets.isIsConstElectricityCost());
        constElectricityCostTF.setText(Double.toString(sets.getConstElectricityCost()));
        refreshTimeTF.setText(Integer.toString(sets.getRefreshTime()));
    }
}
