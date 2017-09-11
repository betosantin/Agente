package teste;

import com.pi4j.io.i2c.I2CFactory;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;

public class pressao {

    public static void main(String[] args) throws I2CFactory.UnsupportedBusNumberException, InterruptedException {
        final NumberFormat NF = new DecimalFormat("##00.00");
        String data = "dd/MM/yyyy";
        String hora = "HH:mm:ss";
        
        
        while (true) {

            bme280 sensor = new bme280();
            float press = 0;
            float temp = 0;
            float hum = 0;

            try {
                temp = sensor.readTemperature();
            } catch (Exception ex) {
                System.err.println(ex.getMessage());
                ex.printStackTrace();
            }
            try {
                press = sensor.readPressure();
            } catch (Exception ex) {
                System.err.println(ex.getMessage());
                ex.printStackTrace();
            }

            try {
                hum = sensor.readHumidity();
            } catch (Exception ex) {
                System.err.println(ex.getMessage());
                ex.printStackTrace();
            }

            java.util.Date agora = new java.util.Date();
            SimpleDateFormat formata = new SimpleDateFormat(data);
            String data1 = formata.format(agora);
            
            formata = new SimpleDateFormat(hora);
            String hora1 = formata.format(agora);

            System.out.println("Dia: " + data1 + " Hora: " + hora1 );
            System.out.println("Temperatura: " + NF.format(temp) + " ºC");
            System.out.println("Pressão   : " + NF.format(press / 100) + " hPa");
            System.out.println("Humidade   : " + NF.format(hum) + " %");
            System.out.println("");
            
            Thread.sleep(300000);
        }
    }
    
}
