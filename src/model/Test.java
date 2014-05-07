package model;

/**
 *
 * @author Marcin
 */

public class Test {
    
    public static void main(String[] args) {
        DataDownloader dd = new DataDownloader();
        System.out.println("Account balance: "+dd.downloadAccountBalance("D7qiHYRU4Ti3h5CPipY5gpz94ZD1jPB3TH"));
        System.out.println("-------------------------------");
        System.out.println("Doge price: "+dd.downloadPricesDoge());
        
        System.out.println("-------------------------------");
        System.out.println("BTC: "+dd.downloadPricesBTC());
        
        System.out.println("-------------------------------");
        System.out.println("Currencies: "+dd.downloadPricesCurrencies());
    }
}
