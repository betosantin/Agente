/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servelet;

import java.util.logging.Level;
import java.util.logging.Logger;
import teste.bme280;

/**
 *
 * @author Roberto Santin
 */
public class TemperatureTrigger implements Runnable {

    boolean stop = false;
    int time;
    
    public TemperatureTrigger( int time ) {
        this.time = time;
    }
    
    @Override
    public void run() {
        try {
        
            System.out.println("Run Thread - TemperatureTrigger");
            
            if ( time == 0 )
            {
                time = 10000;
            }
            
            for (int i = time ; i > 0 && !stop; i--) {
                
                Functions f = new Functions();
                f.obtertemperatura();
                
                bme280 sensor = new bme280();
                float temp = sensor.readTemperature();
                
                
                if ( temp >= 20 && temp <= 23 )
                {
                    f.acenderLed(0, 255, 0, false, 10);
                    //verde
                }
                else if ( temp > 23 && temp <= 25 )
                {
                    f.acenderLed(255, 255, 0, false, 10);
                    //amarelo
                }
                else
                {
                    f.acenderLed(255, 0, 0, false, 10);
                    //vermelho
                }
                
                Thread.sleep(60000); 
            }

            finalize();
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Throwable ex) {
            Logger.getLogger(TemperatureTrigger.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void finalize() throws Throwable {
        stop = true;
        
        super.finalize();
    }
}
