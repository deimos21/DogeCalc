package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Marcin Gordel
 */
public class DogeCount {
    
    private String accountAddress;
    private float priceBTCUSD;
    private float priceDOGEBTC;
    private byte dogeStock;
    private byte btcStock;
    private float powerCost;
    private float power;
    private Date dateStart;
    private float electricityCost;
    private float profit;
    private float balance;
    private boolean[] errorsCode;

    public DogeCount() throws IOException{
        accountAddress = "D7qiHYRU4Ti3h5CPipY5gpz94ZD1jPB3TH";
        downloadBalance();
    }
    
    
    public static void main(String[] args) throws IOException {
        DogeCount dc = new DogeCount();
        try {
            dc.downloadBalance();
        } catch (IOException ex) {
            Logger.getLogger(DogeCount.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
   
    private void countProfit(){
        
    }
    
    private void downloadBalance() throws MalformedURLException, IOException{
        if(accountAddress!=null){
            String json = getJSON("http://dogechain.info/chain/CHAIN/q/addressbalance/"+accountAddress,8000);
            balance = Float.parseFloat(json);
            
        }else{
            errorsCode[0]=true;
        }
    }
    
    private String getJSON(String url, int timeout) {
        try {
            URL u = new URL(url);
            HttpURLConnection c = (HttpURLConnection) u.openConnection();
            c.setRequestMethod("GET");
            c.setRequestProperty("Content-length", "0");
            c.setUseCaches(false);
            c.setAllowUserInteraction(false);
            c.setConnectTimeout(timeout);
            c.setReadTimeout(timeout);
            c.connect();
            int status = c.getResponseCode();

            switch (status) {
                case 200:
                case 201:
                    BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line+"\n");
                    }
                    br.close();
                    return sb.toString();
            }

        } catch (MalformedURLException ex) {
            Logger.getLogger(DogeCount.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DogeCount.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    private void downloadPriceBTC(){
        
    }
    
    private void downloadPriceDOGE(){
        
    }
    
    private List getAvaliableBTCStocks(){
        return null;
    }
    
    private List getAvaliableDOGEStocks(){
        return null;
    }
    
    //_________________________________________________________________
    //Setters and Getters
    
    public String getAccountAddress() {
        return accountAddress;
    }

    public void setAccountAddress(String accountAddress) {
        this.accountAddress = accountAddress;
    }

    public float getPriceBTCUSD() {
        return priceBTCUSD;
    }

    public void setPriceBTCUSD(float priceBTCUSD) {
        this.priceBTCUSD = priceBTCUSD;
    }

    public float getPriceDOGEBTC() {
        return priceDOGEBTC;
    }

    public void setPriceDOGEBTC(float priceDOGEBTC) {
        this.priceDOGEBTC = priceDOGEBTC;
    }

    public byte getDogeStock() {
        return dogeStock;
    }

    public void setDogeStock(byte dogeStock) {
        this.dogeStock = dogeStock;
    }

    public byte getBtcStock() {
        return btcStock;
    }

    public void setBtcStock(byte btcStock) {
        this.btcStock = btcStock;
    }

    public float getPowerCost() {
        return powerCost;
    }

    public void setPowerCost(float powerCost) {
        this.powerCost = powerCost;
    }

    public float getPower() {
        return power;
    }

    public void setPower(float power) {
        this.power = power;
    }

    public Date getDateStart() {
        return dateStart;
    }

    public void setDateStart(Date dateStart) {
        this.dateStart = dateStart;
    }

    public float getElectricityCost() {
        return electricityCost;
    }

    public void setElectricityCost(float electricityCost) {
        this.electricityCost = electricityCost;
    }

    public float getProfit() {
        return profit;
    }

    public void setProfit(float profit) {
        this.profit = profit;
    }

    public float getBalance() {
        return balance;
    }
    
    public String getBalanceString() {
        return Float.toString(balance);
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }
    
    
    
}
