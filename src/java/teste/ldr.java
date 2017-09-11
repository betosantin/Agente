/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package teste;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author root
 */
public class ldr {
    
    public static void main(String[] args) {
        while ( true )
        {
            try {
                System.out.println( time() );
            } catch (InterruptedException ex) {
                Logger.getLogger(ldr.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        
    }
    
    private static int time() throws InterruptedException
    {
        int count = 0;
        
        GpioController gpio = GpioFactory.getInstance();
        
        final GpioPinDigitalOutput pinOut = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_21, PinState.LOW);
        
        pinOut.setShutdownOptions(true, PinState.HIGH);
        Thread.sleep(1);
        
        pinOut.setMode(PinMode.DIGITAL_INPUT);
        
        while ( pinOut.isLow() )
        {
            count +=1;
        }
        
        gpio.shutdown();
        gpio.unprovisionPin(pinOut);
        
        return count;        
    }
    
}
