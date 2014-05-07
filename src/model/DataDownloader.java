package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author Marcin
 */
public class DataDownloader {
    
    String urlApiAccountBalance="http://dogechain.info/chain/CHAIN/q/addressbalance/";
    String urlApiPricesBTC="https://api.bitcoinaverage.com/exchanges/USD";
    String urlApiPricesDoge="http://www.cryptocoincharts.info/v2/api/tradingPairs";
    String urlApiPricesCurrencies="https://kantor.aliorbank.pl/forex/json/current";
    
    int timeout = 6000;
    
    protected double downloadAccountBalance(String accountAddress){
        String textFromApi = getHttpResponseText(urlApiAccountBalance+accountAddress, timeout);
        double balance = 0;
        if(textFromApi != null && !textFromApi.isEmpty()){
            balance = Double.parseDouble(textFromApi);
        }
        return balance;
    }
    
    protected HashMap downloadPricesBTC(){
        HashMap<String, Double> pricesBTC = new HashMap<>();
        
        String jsonFromApi = getHttpResponseText(urlApiPricesBTC, timeout);
        
        JSONParser parser=new JSONParser();
        
        try {
            Object obj = parser.parse(jsonFromApi);
          
            JSONObject obj2 = (JSONObject) obj;
            
            Iterator it = obj2.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pairs = (Map.Entry)it.next();
                
                if(!(pairs.getValue() instanceof String)){
                    JSONObject obj3 = (JSONObject) pairs.getValue();
                    JSONObject obj4 = (JSONObject) obj3.get("rates");

                    double briceBTC = Double.parseDouble(obj4.get("last").toString());

                    pricesBTC.put(pairs.getKey().toString(), briceBTC);
                }
                
                it.remove(); // avoids a ConcurrentModificationException
            }
            
                        
            
        } catch (ParseException ex) {
            Logger.getLogger(DataDownloader.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return pricesBTC;
    }
    
    
    
    protected HashMap downloadPricesDoge(){
        
        HashMap<String, Double> pricesDoge = new HashMap<>();
        
        double priceDoge = 0;
        
        String jsonFromApi = getHttpResponseText(urlApiPricesDoge, timeout);
        
        JSONParser parser=new JSONParser();
        
        try {
            Object obj = parser.parse(jsonFromApi);
            JSONArray array = (JSONArray)obj;
          
            JSONObject obj2;
            for(int i=0;i<array.size();i++){
                obj2 = (JSONObject)array.get(i);
                if("doge/btc".equals(obj2.get("id").toString())){
                    priceDoge = Double.parseDouble(obj2.get("price").toString());
                }
            }
            
            
        } catch (ParseException ex) {
            Logger.getLogger(DataDownloader.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        pricesDoge.put("Cryptsy",priceDoge);
        
        return pricesDoge;
    }
    
    protected HashMap downloadPricesCurrencies(){
        HashMap<String, Double> pricesCurrencies = new HashMap<>();
        
        String jsonFromApi = getHttpResponseText(urlApiPricesCurrencies, timeout);
        
        JSONParser parser=new JSONParser();
        
        try {
            Object obj = parser.parse(jsonFromApi);
          
            JSONObject obj2 = (JSONObject) obj;
            Object obj3 = parser.parse(obj2.get("currencies").toString());
            JSONArray array = (JSONArray)obj3;
            
            double plnusd = 0;
            double usdeur = 0;
            JSONObject currencies;
                        
            for(int i=0;i<array.size();i++){
                currencies = (JSONObject) array.get(i);
                if(("PLN".equals(currencies.get("currency1").toString())) && ("USD".equals(currencies.get("currency2").toString()))){
                    String valueWithDot = currencies.get("buy").toString().replaceAll(",",".");
                    plnusd = Double.parseDouble(valueWithDot);
                }
                if(("USD".equals(currencies.get("currency1").toString())) && ("EUR".equals(currencies.get("currency2").toString()))){
                    String valueWithDot = currencies.get("buy").toString().replaceAll(",",".");
                    usdeur = Double.parseDouble(valueWithDot);
                }
            }
            pricesCurrencies.put("PLN/USD",plnusd);
            pricesCurrencies.put("USD/EUR",usdeur);
            
        } catch (ParseException ex) {
            Logger.getLogger(DataDownloader.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return pricesCurrencies;
    }
    
    private String getHttpResponseText(String url, int timeout) {
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
    
    public static ArrayList getAvaliableCurrencies(){
        ArrayList<String> currencies = new ArrayList();
        currencies.add("PLN");
        currencies.add("USD");
        currencies.add("EUR");
        
        return currencies;
    }
    
    public static ArrayList getAvaliableBtcStocks(){
        ArrayList<String> stocks = new ArrayList();
        DataDownloader dd = new DataDownloader();
        
        HashMap hm = dd.downloadPricesBTC();
        
        Iterator it = hm.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry)it.next();
            stocks.add((String) pairs.getKey());
            it.remove(); // avoids a ConcurrentModificationException
        }
        
        return stocks;
    }
    
    public static ArrayList getAvaliableDogeStocks(){
        ArrayList<String> stocks = new ArrayList();
        DataDownloader dd = new DataDownloader();
        
        HashMap hm = dd.downloadPricesDoge();
        
        Iterator it = hm.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry)it.next();
            stocks.add((String) pairs.getKey());
            it.remove(); // avoids a ConcurrentModificationException
        }
        
        return stocks;
    }
}
