/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package teste;

import com.pi4j.component.temperature.TemperatureSensor;
import com.pi4j.io.w1.W1Master;
import com.pi4j.temperature.TemperatureScale;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author root
 */
public class WaterTemperatureSensor {
    
    public static double getWaterTemperature() {

        W1Master w1Master = new W1Master();

//        System.out.println(w1Master);

        for (TemperatureSensor device : w1Master.getDevices(TemperatureSensor.class)) {
//            System.out.printf("%-20s %3.1f°C %3.1f°F\n", device.getName(), device.getTemperature(),
//                    device.getTemperature(TemperatureScale.CELSIUS));
            if (device.getName().contains("28-0316a32b78ff")) {
                return device.getTemperature(TemperatureScale.CELSIUS);
            }
            //return 32;
        }
        return 0;
    }
    
    public static void main(String[] args) throws InterruptedException {
        
        double maiorTemp = -273;
        double menorTemp = 273;
        String maiorTempHora = "";
        String menorTempHora = "";
        
        String data = "dd/MM/yyyy";
        String hora = "HH:mm:ss";
        String data1, hora1;
        
        SimpleDateFormat formata = null;
        
        
        while (true) {
            
            double atualTemp = getWaterTemperature();
            
            System.out.print( atualTemp + "ºc");
            java.util.Date agora = new java.util.Date();
            formata = new SimpleDateFormat(data);
            data1 = formata.format(agora);
            formata = new SimpleDateFormat(hora);
            hora1 = formata.format(agora);
            
                        
            System.out.print(" - " + data1 + " " + hora1 );

            if ( atualTemp > maiorTemp )
            {
                maiorTemp = atualTemp;
                maiorTempHora = data1 + " " + hora1;
            }
            
            if ( atualTemp < menorTemp )
            {
                menorTemp = atualTemp;
                menorTempHora = data1 + " " + hora1 ;
            }

            System.out.print(" Maior Temp: " + maiorTemp + "ºc às " + maiorTempHora );
            System.out.println(" Menor Temp: " + menorTemp + "ºc às " + menorTempHora );
            
            Thread.sleep(300000);
        }
    }
    
    
}
