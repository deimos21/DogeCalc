package model;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Marcin Gordel
 */
public class Settings implements Serializable{
    private String addressAccount;
    private String currency;
    private String dogeStock;
    private String btcStock;
    private double power;
    private double powerCost;
    private Date dateStart;
    private boolean isConstElectricityCost;
    private double constElectricityCost;
    private int refreshTime;

    public String getAddressAccount() {
        return addressAccount;
    }

    public void setAddressAccount(String addressAccount) {
        this.addressAccount = addressAccount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
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

    public double getPower() {
        return power;
    }

    public void setPower(double power) {
        this.power = power;
    }

    public double getPowerCost() {
        return powerCost;
    }

    public void setPowerCost(double powerCost) {
        this.powerCost = powerCost;
    }

    public Date getDateStart() {
        return dateStart;
    }

    public void setDateStart(Date dateStart) {
        this.dateStart = dateStart;
    }

    public boolean isIsConstElectricityCost() {
        return isConstElectricityCost;
    }

    public void setIsConstElectricityCost(boolean isConstElectricityCost) {
        this.isConstElectricityCost = isConstElectricityCost;
    }

    public double getConstElectricityCost() {
        return constElectricityCost;
    }

    public void setConstElectricityCost(double constElectricityCost) {
        this.constElectricityCost = constElectricityCost;
    }

    public int getRefreshTime() {
        return refreshTime;
    }

    public void setRefreshTime(int refreshTime) {
        this.refreshTime = refreshTime;
    }
    
    
    
}
