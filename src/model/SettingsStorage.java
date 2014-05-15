package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Marcin Gordel
 */
public class SettingsStorage {
    
    private String settsDirectory;
    private String nameOfFile;
    
    public SettingsStorage(){
        String separator = System.getProperty("file.separator");
        settsDirectory = System.getProperty("user.home")+separator+"DogeCount"+separator;
        nameOfFile = "settings.set";
        
        File dir = new File(settsDirectory);
        if(!dir.exists()){
            dir.mkdir();
        }
        File f = new File(settsDirectory+nameOfFile);
        if(!f.exists()) {
            try {
                f.createNewFile();
                Settings setts = new Settings(true);
                serializeSettings(setts);
            } catch (IOException ex) {
                Logger.getLogger(SettingsStorage.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    
    public void serializeSettings(Settings settings){
  
	   try{
               
		FileOutputStream fout = new FileOutputStream(settsDirectory+nameOfFile);
		ObjectOutputStream oos = new ObjectOutputStream(fout);   
		oos.writeObject(settings);
		oos.close();
		System.out.println("Settings have been saved");
 
	   }catch(Exception ex){
		   ex.printStackTrace();
	   }
    }
    
    public Settings deserialzeSettings(){
 
	   Settings settings;
 
	   try{
		   FileInputStream fin = new FileInputStream(settsDirectory+nameOfFile);
		   ObjectInputStream ois = new ObjectInputStream(fin);
		   settings = (Settings) ois.readObject();
		   ois.close();
 
		   return settings;
 
	   }catch(Exception ex){
		   ex.printStackTrace();
		   return null;
	   } 
    } 
}
