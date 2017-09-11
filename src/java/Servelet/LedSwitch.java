/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servelet;

import java.awt.Color;
import Entity.PinLayout;
import Entity.RGBLed;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Roberto Santin
 */
public class LedSwitch implements Runnable {

    Color c;
    RGBLed rgbLed;
    boolean stop = false;
    int time = 0;
    boolean pisca = false;
    
    public LedSwitch( Color c, int time, boolean pisca ) {
        this.c = c;
        this.time = time;
        this.pisca = pisca;
    }
    
    public boolean offLed()
    {
        stop = true;
        
        return stop;
    }

    @Override
    public void run() {
        try {
        
            System.out.println("Run Thread");
            
            rgbLed = RGBLed.getInstance(PinLayout.PIBORG_LEDBORG);
            
            if ( time == 0 )
            {
                time = 10000;
            }
            
            for (int i = time ; i > 0 && !stop; i--) {
                
                rgbLed.ledColorSet(c.getRGB());
                
                Thread.sleep(500); // 0,5 segundo
                
                if ( pisca )
                {
                    rgbLed.ledColorSet(Color.BLACK.getRGB());
                    
                    Thread.sleep(500); // 0,5 segundo
                }
            }

            stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void stop()
    {
        offLed();
        
        if ( rgbLed != null )
        {
            rgbLed.off();
        }
        
        
        try {
            this.finalize();
        } catch (Throwable ex) {
        }
    }
}
