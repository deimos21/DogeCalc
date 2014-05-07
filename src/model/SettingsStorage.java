package model;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 *
 * @author Marcin Gordel
 */
public class SettingsStorage {
    
    public void serializeSettings(Settings settings){
  
	   try{
 
		FileOutputStream fout = new FileOutputStream("settings.set");
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
 
		   FileInputStream fin = new FileInputStream("settings.set");
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
