package model;

import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 *
 * @author Marcin Gordel <marcin at gordel.pl>
 * @date 2014-05-14
 */
public class Validator {
    
    public String validate(String type, Object value){
        String errorMsg = null;
        switch (type){
            case "address":
                if(value!=null){
                    String address = (String) value;
                    if(address.length()!=34){
                        errorMsg = "Podany adres konta ma niepoprawną długość";
                    }
                }else{
                    errorMsg = "Adres konta nie może być pusty";
                }
            break;
            case "constBalance":
                try{
                    double power = Double.parseDouble((String) value);
                    if(power<0){
                        errorMsg = "Podana wartośc salda nie może być ujemnna";
                    }
                }catch(NumberFormatException e){
                  errorMsg = "Podana wartośc mocy jest niepoprawna";
                }
            break;
            case "power":
                try{
                    double power = Double.parseDouble((String) value);
                    if(power<0){
                        errorMsg = "Podana wartośc mocy nie może być ujemnna";
                    }
                }catch(NumberFormatException e){
                  errorMsg = "Podana wartośc mocy jest niepoprawna";
                }
                
            break;
            case "powerCost":
                try{
                    double powerCost = Double.parseDouble((String) value);
                    if(powerCost<0){
                        errorMsg = "Podana wartośc taryfy nie może być ujemnna";
                    }
                }catch(NumberFormatException e){
                    errorMsg = "Podana wartośc taryfy jest niepoprawna";
                }
            break; 
            case "dateStart":
                try{
                    LocalDate lDate = (LocalDate) value;
                    Instant instant = lDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
                    Date dateStart = Date.from(instant);
                    Date obecna = new Date();
                    if(obecna.getTime()-dateStart.getTime()<=0){
                        errorMsg = "Podana data jest datą z przyszłości";
                    }
                }catch(NumberFormatException e){
                    errorMsg = "Podana data startowa jest niepoprawna";
                }
            break;
            case "constCost":
                try{
                    double constCost = Double.parseDouble((String) value);
                    if(constCost<0){
                        errorMsg = "Podana wartośc stałych kosztów prądu nie może być ujemnna";
                    }
                }catch(NumberFormatException e){
                    errorMsg = "Podana wartośc stałych kosztów prądu jest niepoprawna";
                }
            break; 
            case "updateTime":
                try{
                    int updateTime = Integer.parseInt((String)value);
                    if(updateTime<2){
                        errorMsg = "Podany czas odświeżania musi być większy od 1";
                    }
                }catch(NumberFormatException e){
                    errorMsg = "Podany czas odświeżania jest niepoprawny";
                }
            break; 
            default:
                return errorMsg;
        }
        return errorMsg;
    }
    
}
