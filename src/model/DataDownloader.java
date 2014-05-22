package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
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
 * @author Marcin Gordel
 */
public class DataDownloader {
    
    String urlApiAccountBalance="http://dogechain.info/chain/CHAIN/q/addressbalance/";
    String urlApiPricesBTC="https://api.bitcoinaverage.com/exchanges/USD";
    String urlApiPricesDoge="http://www.cryptocoincharts.info/v2/api/tradingPairs";
    String urlApiPricesCurrencies="https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20csv%20where%20url%3D%22http%3A%2F%2Ffinance.yahoo.com%2Fd%2Fquotes.csv%3Fe%3D.csv%26f%3Dc4l1%26s%3DEURUSD%3DX%2CUSDPLN%3DX%22%3B&format=json&diagnostics=true&callback=";
    
    int timeout = 6000;
    String method = "GET";
    
    protected double downloadAccountBalance(String accountAddress){
        double balance = 0;
        String textFromApi="Nieprawidłowy numer konta";
        try {
            textFromApi = getHttpResponseText(urlApiAccountBalance+accountAddress, timeout, method, null);
        
            if(textFromApi != null && !textFromApi.isEmpty()){
                balance = Double.parseDouble(textFromApi);
            }
        } catch (NumberFormatException ex) {
            Logger.getLogger(DogeCalc.class.getName()).log(Level.SEVERE, "Nieprawidłowy numer konta", ex);
        } catch (IOException ex) {
            Logger.getLogger(DogeCalc.class.getName()).log(Level.SEVERE, "Błąd pobierania salda z konta", ex);
        }
        return balance;
    }
    
    protected HashMap downloadPricesBTC(){
        HashMap<String, Double> pricesBTC = new HashMap<>();
        
        try {
            String jsonFromApi = getHttpResponseText(urlApiPricesBTC, timeout, method, null);

            JSONParser parser=new JSONParser();
        
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
            Logger.getLogger(DogeCalc.class.getName()).log(Level.SEVERE, "Błąd parsowania kursu BTC", ex);
        } catch (IOException ex) {
            Logger.getLogger(DogeCalc.class.getName()).log(Level.SEVERE, "Błąd pobierania kursu BTC: "+ex.getMessage(), ex);
        }
        
        return pricesBTC;
    }
    
    
    
    protected HashMap downloadPricesDoge(){
        
        HashMap<String, Double> pricesDoge = new HashMap<>();
        
        double priceDoge = 0;
        
        String[][] params={
            {"pairs","doge_btc"}
        };
        try {
            String jsonFromApi = getHttpResponseText(urlApiPricesDoge, timeout, "POST", params);
            
            JSONParser parser=new JSONParser();
            
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
            Logger.getLogger(DogeCalc.class.getName()).log(Level.SEVERE, "Błąd parsowania kursu DOGE", ex);
        } catch (IOException ex) {
            Logger.getLogger(DogeCalc.class.getName()).log(Level.SEVERE, "Błąd pobierania kursu DOGE", ex);
        }
        
        pricesDoge.put("Cryptsy",priceDoge);
        
        return pricesDoge;
    }
    
    protected HashMap downloadPricesCurrencies(){
        HashMap<String, Double> pricesCurrencies = new HashMap<>();
        
        try {
            String jsonFromApi = getHttpResponseText(urlApiPricesCurrencies, timeout, method, null);

            JSONParser parser=new JSONParser();
        
        
            Object obj = parser.parse(jsonFromApi);
          
            JSONObject obj2 = (JSONObject) obj;
            obj = parser.parse(obj2.get("query").toString());
            JSONObject obj3 = (JSONObject) obj;
            obj = parser.parse(obj3.get("results").toString());
            JSONObject obj4 = (JSONObject) obj;
            obj = parser.parse(obj4.get("row").toString());
            JSONArray array = (JSONArray) obj;
            
            JSONObject obj5 = (JSONObject) array.get(0);
            JSONObject obj6 = (JSONObject) array.get(1);
            
            double plnusd = 0;
            double usdeur = 0;
            if("EUR".equals(obj5.get("col0").toString())){
                usdeur=Double.parseDouble(obj5.get("col1").toString());
            }
            if("PLN".equals(obj6.get("col0").toString())){
                plnusd=Double.parseDouble(obj6.get("col1").toString());
            }
                                    

            pricesCurrencies.put("PLN/USD",plnusd);
            pricesCurrencies.put("USD/EUR",usdeur);
            
        } catch (ParseException ex) {
            Logger.getLogger(DogeCalc.class.getName()).log(Level.SEVERE, "Błąd parsowania kursów walut", ex);
        } catch (IOException ex) {
            Logger.getLogger(DogeCalc.class.getName()).log(Level.SEVERE, "Błąd pobierania kursów walut", ex);
        }
        
        return pricesCurrencies;
    }
    
    private String getHttpResponseText(String url, int timeout, String method, String[][]params) throws MalformedURLException, ProtocolException, IOException {
        
        URL u = new URL(url);
        HttpURLConnection c = (HttpURLConnection) u.openConnection();
        c.setRequestMethod(method);


        c.setUseCaches(false);
        c.setAllowUserInteraction(false);
        c.setConnectTimeout(timeout);
        c.setReadTimeout(timeout);

        if(params!=null)if(params.length>0){
            c.setDoInput(true);
            c.setDoOutput(true);
            c.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            OutputStreamWriter writer = new OutputStreamWriter(c.getOutputStream());
            for(int i=0;i<params.length;i++){
                String postParms = params[0][0]+"="+params[0][1]+"&";
                writer.write(postParms);
            }
            writer.flush ();
            writer.close ();
        }else{
            c.setRequestProperty("Content-length", "0");
            c.connect();
        }
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
        c.disconnect();

        return null;
    }
    
    public static ArrayList getAvailableCurrencies(){
        ArrayList<String> currencies = new ArrayList<String>();
        currencies.add("PLN");
        // TODO in future
        //currencies.add("USD");
        //currencies.add("EUR");
        
        return currencies;
    }
    
    public static ArrayList getAvailableBtcStocks(){
        ArrayList<String> stocks = new ArrayList<String>();
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
    
    public static ArrayList getAvailableDogeStocks(){
        ArrayList<String> stocks = new ArrayList<String>();
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
