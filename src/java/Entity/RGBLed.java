package Entity;

import Entity.PinLayout;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.wiringpi.Gpio;
import com.pi4j.wiringpi.SoftPwm;
import java.awt.Color;

public class RGBLed {
    
    private PinLayout pinLayout;
    private Color color = Color.BLACK;
    
    private RGBLed(PinLayout pinLayout) {
        this.pinLayout = pinLayout;
        
        init();
    }
    
    private static RGBLed controller = null;
    
    public static RGBLed getInstance(PinLayout pinLayout) {

        if (controller == null) {
            controller = new RGBLed( pinLayout );
        }

        return controller;
    }
    
    // Metodo para converter de RGB ( 0 a 255 ) para a faixa do pwm ( 0 a 100 )
    
    private int convert(int x, int in_min, int in_max, int out_min, int out_max) {
        return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }
    
    public void ledColorSet(int color) {
        int r_val, g_val, b_val;

        r_val = (color & 0xFF0000) >> 16; // get red value
        g_val = (color & 0x00FF00) >> 8; // get green value
        b_val = (color & 0x0000FF) >> 0; // get blue value

        r_val = convert(r_val, 0, 255, 0, 100); // alterando o numero entre 0 até 255 para 0 até 100
        g_val = convert(g_val, 0, 255, 0, 100);
        b_val = convert(b_val, 0, 255, 0, 100);

        SoftPwm.softPwmWrite(pinLayout.getRedPin().getAddress(), 100 - r_val);
        SoftPwm.softPwmWrite(pinLayout.getGreenPin().getAddress(), 100 - g_val);
        SoftPwm.softPwmWrite(pinLayout.getBluePin().getAddress(), 100 - b_val);
    }

    private void init() {
        Gpio.wiringPiSetup();

        SoftPwm.softPwmCreate(pinLayout.getRedPin().getAddress(), 0, 100);
        SoftPwm.softPwmCreate(pinLayout.getGreenPin().getAddress(), 0, 100);
        SoftPwm.softPwmCreate(pinLayout.getBluePin().getAddress(), 0, 100);
    }
    
    public final void off() 
    {
        Color c = new Color(0,0,0);
        ledColorSet(c.getRGB());
    }

    public Color getDisplayedColor() {
        return color;
    }

}