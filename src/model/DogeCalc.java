package model;

import java.util.Date;
import java.util.HashMap;
import java.util.logging.Logger;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 *
 * @author Marcin Gordel
 */
public final class DogeCalc {
    
    private String accountAddress;
    private double priceBTCUSD;
    private double priceDOGEBTC;
    private double pricePLNUSD;
    private double priceUSDEUR;
    private String dogeStock;
    private String btcStock;
    private String currency;
    private double powerCost;
    private double power;
    private Date dateStart;
    private double electricityCost;
    private boolean isConstElectricityCost;
    private double constElectricityCost;
    private boolean isConstBallance;
    private double constBallance;
    private int refreshTime;
    
    private double profit;
    private double profit2;
    private double profit3;
    private double balance;
    private boolean[] errorsCode;
    
    private int downloadCounter = 0;
    
    private DataDownloader dd;
    private SettingsStorage ss;
    
    public static final Logger logger = Logger.getLogger(DogeCalc.class.getName());
    
    public IntegerProperty progressLoad;

    public DogeCalc(){
        progressLoad = new SimpleIntegerProperty(0);
    }
    
    public void init(){
        dd = new DataDownloader();
        progressLoad.set(5);
        ss = new SettingsStorage();
        progressLoad.set(15);
        updateSettings();
        progressLoad.set(20);
    }
    
    public void updateSettings(){
        Settings sets = ss.deserialzeSettings();
        
        accountAddress = sets.getAddressAccount();
        isConstBallance = sets.isIsConstBallance();
        constBallance = sets.getConstBallance();
        currency = sets.getCurrency();
        dogeStock = sets.getDogeStock();
        btcStock = sets.getBtcStock();
        power = sets.getPower();
        powerCost = sets.getPowerCost();
        dateStart=sets.getDateStart();
        isConstElectricityCost = sets.isIsConstElectricityCost();
        constElectricityCost = sets.getConstElectricityCost();
        refreshTime = sets.getRefreshTime();
        
        if(isConstBallance){
            balance = constBallance;
        }else{
            balance = dd.downloadAccountBalance(accountAddress);
        }
        
        countElectricityCost();
        countProfit();
    }
    
    public void update(){
        if(isConstBallance){
            balance = constBallance;
        }else{
            balance = dd.downloadAccountBalance(accountAddress);
        }
        
        priceDOGEBTC = (double) dd.downloadPricesDoge().get(dogeStock);
        priceBTCUSD = (double) dd.downloadPricesBTC().get(btcStock);
        
        //PricesCurrencies download only 1/20 times
        if(downloadCounter==0){
            HashMap priceCurrencies = dd.downloadPricesCurrencies();
            pricePLNUSD = (double) priceCurrencies.get("PLN/USD");
            priceUSDEUR = (double) priceCurrencies.get("USD/EUR");
        }
        downloadCounter++;
        if(downloadCounter==20)downloadCounter=0;
        
        countElectricityCost();
        countProfit();
    }
    
    
      
    private void countProfit(){
        switch (currency) {
            case "USD":  
                profit = balance*priceDOGEBTC*priceBTCUSD-electricityCost;
                profit2 = profit*pricePLNUSD;
                profit3 = profit/priceUSDEUR;
                break;
            case "PLN":  
                profit = balance*priceDOGEBTC*priceBTCUSD*pricePLNUSD-electricityCost;
                profit2 = profit/pricePLNUSD;
                profit3 = profit2/priceUSDEUR;
                break;
            case "EUR":  
                profit = balance*priceDOGEBTC*priceBTCUSD/priceUSDEUR-electricityCost;
                profit2 = profit/priceUSDEUR;
                profit3 = profit2/priceUSDEUR;
                break;
            default:
                profit = balance*priceDOGEBTC*priceBTCUSD*pricePLNUSD-electricityCost;
                profit2 = profit/pricePLNUSD;
                profit3 = profit2/priceUSDEUR;
        }
    }
    
    
    
    
    private void countElectricityCost(){
        if(isConstElectricityCost){
            electricityCost = constElectricityCost;
        }else{
            Date now = new Date();
            double hours = ((now.getTime() - dateStart.getTime())/(1000*60*60));
            electricityCost = hours * powerCost * power/1000;
        }
    }
    

    //_________________________________________________________________
    //Setters and Getters
    public String getAccountAddress() {
        return accountAddress;
    }

    public void setAccountAddress(String accountAddress) {
        this.accountAddress = accountAddress;
    }

    public double getPriceBTCUSD() {
        return priceBTCUSD;
    }

    public void setPriceBTCUSD(double priceBTCUSD) {
        this.priceBTCUSD = priceBTCUSD;
    }

    public double getPriceDOGEBTC() {
        return priceDOGEBTC;
    }

    public void setPriceDOGEBTC(double priceDOGEBTC) {
        this.priceDOGEBTC = priceDOGEBTC;
    }

    public double getPricePLNUSD() {
        return pricePLNUSD;
    }

    public void setPricePLNUSD(double pricePLNUSD) {
        this.pricePLNUSD = pricePLNUSD;
    }

    public double getPriceUSDEUR() {
        return priceUSDEUR;
    }

    public void setPriceUSDEUR(double priceUSDEUR) {
        this.priceUSDEUR = priceUSDEUR;
    }

    public String getDogeStock() {
        return dogeStock;
    }

    public void setDogeStock(String dogeStock) {
        this.dogeStock = dogeStock;
    }

    public String getBtcStock() {
        return btcStock;
    }

    public void setBtcStock(String btcStock) {
        this.btcStock = btcStock;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public double getPowerCost() {
        return powerCost;
    }

    public void setPowerCost(double powerCost) {
        this.powerCost = powerCost;
    }

    public double getPower() {
        return power;
    }

    public void setPower(double power) {
        this.power = power;
    }

    public Date getDateStart() {
        return dateStart;
    }

    public void setDateStart(Date dateStart) {
        this.dateStart = dateStart;
    }

    public double getElectricityCost() {
        return electricityCost;
    }

    public void setElectricityCost(double electricityCost) {
        this.electricityCost = electricityCost;
    }

    public double getProfit() {
        return profit;
    }

    public void setProfit(double profit) {
        this.profit = profit;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public boolean[] getErrorsCode() {
        return errorsCode;
    }

    public void setErrorsCode(boolean[] errorsCode) {
        this.errorsCode = errorsCode;
    }

    public DataDownloader getDd() {
        return dd;
    }

    public void setDd(DataDownloader dd) {
        this.dd = dd;
    }

    public double getProfit2() {
        return profit2;
    }

    public void setProfit2(double profit2) {
        this.profit2 = profit2;
    }

    public double getProfit3() {
        return profit3;
    }

    public void setProfit3(double profit3) {
        this.profit3 = profit3;
    }

    public SettingsStorage getSs() {
        return ss;
    }

    public boolean isIsConstElectricityCost() {
        return isConstElectricityCost;
    }

    public double getConstElectricityCost() {
        return constElectricityCost;
    }

    public int getRefreshTime() {
        return refreshTime;
    }
    
    
    
    
}
