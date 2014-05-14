package model;

import java.util.logging.LogRecord;
import java.util.logging.StreamHandler;

/**
 *
 * @author Marcin Gordel <marcin at gordel.pl>
 * @date 2014-05-14
 */
public class MyHandler extends StreamHandler{
    @Override
    public void publish(LogRecord record) {
        System.out.println("LALALAL: ");
        
        super.publish(record);
    }
 
 
    @Override
    public void flush() {
        super.flush();
    }
 
 
    @Override
    public void close() throws SecurityException {
        super.close();
    }
}
