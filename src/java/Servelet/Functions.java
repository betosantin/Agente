/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servelet;

import Entity.Resultado;
import com.google.gson.Gson;
import com.pi4j.component.temperature.TemperatureSensor;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.w1.W1Master;
import com.pi4j.temperature.TemperatureScale;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import teste.bme280;

/**
 *
 * @author Roberto Santin
 */
public class Functions {
    
    LedSwitch ledThread;
    
    final NumberFormat NF = new DecimalFormat("##00.00");
    
    public void acenderLed(int red, int green, int blue, boolean blink, int time ) {
        
        Resultado res = new Resultado();
        res.setData(Calendar.getInstance().getTimeInMillis());
        res.setDirecao(Resultado.DIRECTION_RECEIVE);
        res.setDispositivo("");
        res.setNomeEvento("acenderled");
        res.setUsuario("Agente");
        res.setValue("Cor: R:" + red + " G:" + green + " B:" + blue + " Piscar:" + blink + " Tempo(s):" + time );

        sendValuesToUrl(res);
        
        ledThread = new LedSwitch(new Color(red, green, blue), time, blink);
        ledThread.run();
        
    }
    
    public void obterumidade()
    {
        try {
            bme280 sensor = new bme280();
            
            float hum = sensor.readHumidity();
            
            Resultado res = new Resultado();
            res.setData(Calendar.getInstance().getTimeInMillis());
            res.setDirecao(Resultado.DIRECTION_RECEIVE);
            res.setDispositivo("");
            res.setNomeEvento("obterumidade");
            res.setUsuario("Agente");
            res.setValue(NF.format(hum) + " %25");
            
            sendValuesToUrl(res);
            
            System.out.println("obterpressao: " + NF.format(hum) + " %");

        } catch (Exception ex) {
            Logger.getLogger(acao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void obterldr()
    {
        try {
            int count = 0;

            GpioController gpio = GpioFactory.getInstance();

            final GpioPinDigitalOutput pinOut = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_21, PinState.LOW);

            pinOut.setShutdownOptions(true, PinState.HIGH);

            Thread.sleep(1);

            pinOut.setMode(PinMode.DIGITAL_INPUT);

            while (pinOut.isLow()) {
                count += 1;
            }

            gpio.shutdown();
            gpio.unprovisionPin(pinOut);

            Resultado res = new Resultado();
            res.setData(Calendar.getInstance().getTimeInMillis());
            res.setDirecao(Resultado.DIRECTION_RECEIVE);
            res.setDispositivo("");
            res.setNomeEvento("obterldr");
            res.setUsuario("Agente");
            res.setValue(count + "");
            
            sendValuesToUrl(res);
            
            System.out.println("obterldr: " + count );
        } catch (Exception ex) {

        }
    }
    
    public void obterpressao()
    {
        try {
            bme280 sensor = new bme280();
            
            sensor.readTemperature();
            float press = sensor.readPressure();
            
            Resultado res = new Resultado();
            res.setData(Calendar.getInstance().getTimeInMillis());
            res.setDirecao(Resultado.DIRECTION_RECEIVE);
            res.setDispositivo("");
            res.setNomeEvento("obterpressao");
            res.setUsuario("Agente");
            res.setValue(NF.format( press / 100 ) + " hPa");

            sendValuesToUrl(res);
            
            System.out.println("obterpressao: " + NF.format( press / 100 ) + " hPa");
        } catch (Exception ex) {
            Logger.getLogger(acao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void obtertemperatura() 
    {
        W1Master w1Master = new W1Master();

        if ( !w1Master.getDevices(TemperatureSensor.class).isEmpty() )
        {
            for (TemperatureSensor device : w1Master.getDevices(TemperatureSensor.class)) {
                if (device.getName().contains("28-0316a32b78ff")) {
                        Resultado res = new Resultado();
                        res.setData(Calendar.getInstance().getTimeInMillis());
                        res.setDirecao(Resultado.DIRECTION_RECEIVE);
                        res.setDispositivo("");
                        res.setNomeEvento("obtertemperatura");
                        res.setUsuario("Agente");
                        res.setValue(device.getTemperature(TemperatureScale.CELSIUS) + " ºc");

                        sendValuesToUrl(res);
                        
                        System.out.println("obtertemperatura: " + device.getTemperature(TemperatureScale.CELSIUS) + " ºc");
                }
            }
        }
        
        else
        {
            try {
                bme280 sensor = new bme280();

                Resultado res = new Resultado();
                res.setData(Calendar.getInstance().getTimeInMillis());
                res.setDirecao(Resultado.DIRECTION_RECEIVE);
                res.setDispositivo("");
                res.setNomeEvento("obtertemperatura");
                res.setUsuario("Agente");
                res.setValue(NF.format(sensor.readTemperature()) + " ºc");

                sendValuesToUrl(res);
                
                System.out.println("obtertemperatura: " + NF.format(sensor.readTemperature()) + " ºc");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void apagarLed() {

        if (ledThread != null) {
            ledThread.stop();

            Resultado res = new Resultado();
            res.setData(Calendar.getInstance().getTimeInMillis());
            res.setDirecao(Resultado.DIRECTION_RECEIVE);
            res.setDispositivo("");
            res.setNomeEvento("apagarled");
            res.setUsuario("Agente");
            res.setValue("Desligado");

            sendValuesToUrl(res);
        }
    }
    
    public void realizarMonitoramento() {
    }
    
    public void pararMonitoramento() {
    }
    
    public String getUrlCentralizadora()
    {
        return getUrlCentralizadora(true);
    }
    
    public static String getUrlCentralizadora(boolean withReturn)
    {

        String ret = "";
        
        try {
            Properties props = getProp();

            if (props != null) {
                String schema = props.getProperty("httptype", "");
                String ip = props.getProperty("ip", "");
                String port = props.getProperty("porta", "");
                String servelet = props.getProperty("servelet", "");
                String retorno = props.getProperty("retorno", "");
                String pass = props.getProperty("pass", "");

                ret = schema + "://" + ip + ":" + port + "/" + servelet + "/";
                
                if ( withReturn )
                {
                    ret += retorno + "?pass=" + pass;
                }
                    
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return ret;
    }
    
    public static Properties getProp() throws IOException {
        Properties props = new Properties();
        File f = new File(System.getProperty("user.dir") + "/dados.properties");
      
        if (f.exists()) {
            FileInputStream file = new FileInputStream(f);
            props.load(file);
        }
        
        return props;
    } 
    
    
    public void sendValuesToUrl(Resultado res)
    {
        try {
            URL url = new URL(getUrlCentralizadora());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setReadTimeout(10000);
            conn.setDoOutput(true);
            conn.setDoInput(true);
//            conn.setRequestProperty("Content-Type","application/json"); 
            OutputStreamWriter out = new OutputStreamWriter(
                    conn.getOutputStream(), "UTF-8");
            out.write("resultado=" + new Gson().toJson(res));
            out.flush();
            out.close();
            
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            conn.getInputStream()));
            String decodedString;
            while ((decodedString = in.readLine()) != null) {
                System.out.println(decodedString);
            }
            in.close();
            conn.disconnect(); 
            
        } catch (IOException ex) {
            System.out.println("TIME OUT......");
            ex.printStackTrace();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    static {
        SslVerification();
    }
    
    public static void SslVerification() {
    try
    {
            // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }
            };

            // Install the all-trusting trust manager
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            // Create all-trusting host name verifier
            HostnameVerifier allHostsValid = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };

            // Install the all-trusting host verifier
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
    }
    
}
