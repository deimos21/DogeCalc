package model;

import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
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
                if(value.getClass().equals(Type.String)){
                    String address = (String) value;
                    if(address.length()!=34){
                        errorMsg = "Podany adres konta ma niepoprawną długość";
                    }
                }else{
                    errorMsg = "Podana adres konta nie jest wartością tekstową";
                }
            break;
            case "power":
                double power = (double) value;
                if(power<0){
                    errorMsg = "Podana wartośc mocy jest niepoprawna";
                }
            break;
            case "powerCost":
                double powerCost = (double) value;
                if(powerCost<0){
                    errorMsg = "Podana wartośc taryfy jest niepoprawna";
                }
            break; 
            case "dateStart":
                if(value.getClass().equals(Date.class)){
                    Date dateStart = (Date) value;
                    Date obecna = new Date();
                    if(obecna.getTime()-dateStart.getTime()>0){
                        errorMsg = "Podana data jest datą z przyszłości";
                    }
                }else{
                    errorMsg = "Podana data początkowa jest niepoprawna";
                }
            break;
            case "constCost":
                double constCost = (double) value;
                if(constCost<0){
                    errorMsg = "Podana wartośc stałych kosztów pradu jest niepoprawna";
                }
            break; 
            case "updateTime":
                double updateTime = (int) value;
                if(updateTime<0){
                    errorMsg = "Podana wartość odświeżania jest niepoprawna";
                }
            break; 
            default:
                return errorMsg;
        }
        return errorMsg;
    }
    
}
