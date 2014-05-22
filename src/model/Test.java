package model;

import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author Marcin
 */

public class Test {
    
    public static void main(String[] args) {
        final Test test = new Test();
        test.testDataDownloader();
        //test.testDogeCount();
        //test.testSettingStorage();
//        TimerTask task = new TimerTask() {
//
//            @Override
//            public void run() {
//                test.testDogeCount();
//            }
//        };
//        Timer timer = new Timer();
//        timer.schedule(task, 0, 5000);
        
        
    }
    
    private void testSettingStorage(){
        SettingsStorage ss = new SettingsStorage();
        Settings sets = new Settings();
        
        sets.setAddressAccount("fgdgdfgdfgdfgdjhyt");
        sets.setCurrency("EUR");
        sets.setBtcStock("LALALA");
        
        ss.serializeSettings(sets);
        
        Settings newSets = ss.deserialzeSettings();
        
        System.out.println("Account Account: "+newSets.getAddressAccount());
        System.out.println("-------------------------------");
        
        System.out.println("Currency: "+newSets.getCurrency());
        System.out.println("-------------------------------");
        
        System.out.println("BtcStock: "+newSets.getBtcStock());
        System.out.println("-------------------------------");
        
        System.out.println("Power: "+newSets.getPower());
        System.out.println("-------------------------------");
        
        newSets.setPower(23.44);
        
        ss.serializeSettings(newSets);
        
        Settings newSets2 = ss.deserialzeSettings();
        
        System.out.println("Power2: "+newSets2.getPower());
        System.out.println("-------------------------------");
        
        System.out.println("Account Account: "+newSets2.getAddressAccount());
        System.out.println("-------------------------------");
    }
    
    private void testDataDownloader(){
        DataDownloader dd = new DataDownloader();
        System.out.println("Account balance: "+dd.downloadAccountBalance("D7qiHYRU4Ti3h5CPipY5gpz94ZD1jPB3TH"));
        System.out.println("-------------------------------");
        System.out.println("Doge price: "+dd.downloadPricesDoge());
        
        System.out.println("-------------------------------");
        System.out.println("BTC: "+dd.downloadPricesBTC());
        
        System.out.println("-------------------------------");
        System.out.println("Currencies: "+dd.downloadPricesCurrencies());
    }
    
    private void testDogeCount(){
        DogeCalc dc = new DogeCalc();
        
        dc.update();
        
        System.out.println("Account balance: "+dc.getBalance());
        System.out.println("--------------------------------------");
        
        System.out.println("Doge price: "+dc.getPriceDOGEBTC());
        System.out.println("--------------------------------------");
        
        System.out.println("BTC price: "+dc.getPriceBTCUSD());
        System.out.println("--------------------------------------");
        
        System.out.println("BTC PLNUSD: "+dc.getPricePLNUSD());
        System.out.println("--------------------------------------");
        
        System.out.println("Electricity cost: "+dc.getElectricityCost());
        System.out.println("--------------------------------------\n");
        
        System.out.println("PROFIT: "+dc.getProfit()+"\n\n");
        
    }
}
