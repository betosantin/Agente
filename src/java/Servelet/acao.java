/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servelet;

import Entity.Parametros;
import Entity.Metodos;
import Entity.Resultado;
import com.google.gson.Gson;
import com.pi4j.component.temperature.TemperatureSensor;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.i2c.I2CFactory;
import com.pi4j.io.w1.W1Master;
import com.pi4j.temperature.TemperatureScale;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import teste.bme280;

/**
 *
 * @author roberto.santin
 */
public class acao extends HttpServlet {

    HttpServletRequest requisicao;
    HttpServletResponse resposta;

    LedSwitch ledThread;
    
    final NumberFormat NF = new DecimalFormat("##00.00");
    
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet acao</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet acao at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        requisicao = request;
        resposta = response;
        
        doPost(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        requisicao = request;
        resposta = response;

        
        if (request.getParameter("acenderled") != null )
        {
            acenderLed();
        }
        
        else if ( request.getParameter("apagarled") != null )
        {
            apagarLed();
        }
        
        else if ( request.getParameter("obtertemperatura") != null )
        {
            obtertemperatura();
        }
        else if ( request.getParameter("obterpressao") != null )
        {
            obterpressao();
        }
        else if ( request.getParameter("obterumidade") != null )
        {
            obterumidade();
        }
        else if ( request.getParameter("obterldr") != null )
        {
            obterldr();
        }
        
        else if ( request.getParameter("sincronizar") != null )
        {
            getServletInfo();
        }
       
        else if ( request.getParameter("status") != null )
        {
            getStatus();
        }
    }

    private void acenderLed() {
        int red = 255;
        int green = 255;
        int blue = 255;   
        
        // Time = 0 Infinito ( em segundos )
        int time = 0;
        
        // Blink = Pisca
        boolean blink = false;
        
        if ( requisicao.getParameter("red") != null )
        {
            red = Integer.parseInt(requisicao.getParameter("red"));
        }
        
        if ( requisicao.getParameter("green") != null )
        {
            green = Integer.parseInt(requisicao.getParameter("green"));
        }
        
        if ( requisicao.getParameter("blue") != null )
        {
            blue = Integer.parseInt(requisicao.getParameter("blue"));
        }
        
        if ( requisicao.getParameter("time") != null )
        {
            time = Integer.parseInt(requisicao.getParameter("time"));
        }
        
        if ( requisicao.getParameter("blink") != null )
        {
            blink = Boolean.valueOf(requisicao.getParameter("blink"));
        }
        
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
    
    private void apagarLed() {
        
        if ( ledThread != null )
        {
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
    
    private void obtertemperatura() 
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
    
    private void obterpressao()
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
    
    private void obterumidade()
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
    
    private void obterldr()
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

    public void getStatus() {
        resposta.setContentType("application/json");
        
        try {
            resposta.getWriter().write("Online");
            
        } catch (IOException ex) {
            Logger.getLogger(acao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        resposta.setContentType("application/json");
        
        try {
            createProp();
            
            Properties props = getProp();
            
            String httpType = requisicao.getParameter("httptype");
            String ip = requisicao.getParameter("ip");
            String porta = requisicao.getParameter("porta");
            String servelet = requisicao.getParameter("servelet");
            String retorno = requisicao.getParameter("retorno");
            String pass = requisicao.getParameter("pass");
            
            if (httpType != null && ip != null && porta != null
                    && servelet != null && retorno != null && pass != null) {

                props.setProperty("httptype", httpType);
                props.setProperty("ip", ip);
                props.setProperty("porta", porta);
                props.setProperty("servelet", servelet);
                props.setProperty("retorno", retorno);
                props.setProperty("pass", pass);

                saveProp(props);
            }
            else
            {
                resposta.sendError(HttpURLConnection.HTTP_BAD_REQUEST);
            }
            
        } catch (IOException ex) {
            Logger.getLogger(acao.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Parametros p1 = new Parametros( "red", Parametros.INT );
        Parametros p2 = new Parametros( "green", Parametros.INT );
        Parametros p3 = new Parametros( "blue", Parametros.INT );
        Parametros p4 = new Parametros( "time", Parametros.INT );
        Parametros p5 = new Parametros( "blink", Parametros.BOOLEAN );
        
        List<Parametros> ps = new ArrayList();
        ps.add(p1);
        ps.add(p2);
        ps.add(p3);
        ps.add(p4);
        ps.add(p5);
        
        Metodos luzOn = new Metodos( "acenderled", ps );
        Metodos luzOff = new Metodos( "apagarled", Metodos.VOID, null );
        
        Metodos obtTemp = new Metodos( "obtertemperatura", Parametros.FLOAT, null );
        Metodos obtPress = new Metodos( "obterpressao", Parametros.FLOAT, null );
        Metodos obtHumi = new Metodos( "obterumidade", Parametros.FLOAT, null );
        Metodos obtLuminosidade = new Metodos( "obterldr", Parametros.INT, null );
        
        List<Metodos> metodos = new ArrayList<>();
        metodos.add(luzOn);
        metodos.add(luzOff);
        metodos.add(obtTemp);
        metodos.add(obtPress);
        metodos.add(obtHumi);
        metodos.add(obtLuminosidade);
        
        Gson g = new Gson();
        
        try {
            resposta.getWriter().write(g.toJson(metodos));
            
        } catch (IOException ex) {
            Logger.getLogger(acao.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        return "";
    }
    
    private void createProp() throws FileNotFoundException, IOException
    {
        Properties props = new Properties();
 
        props.setProperty("httptype", "");
        props.setProperty("ip", "");
        props.setProperty("porta", "");
        props.setProperty("servelet", "");
        props.setProperty("retorno", "");
        props.setProperty("pass", "" );

        saveProp(props);
    }
    
    private void saveProp(Properties props) throws FileNotFoundException, IOException
    {
        File f = new File(System.getProperty("user.dir") + "/dados.properties");
      
        FileOutputStream fos = new FileOutputStream(f);
        props.store(fos, "");
        fos.close();
    }
    
    private Properties getProp() throws IOException {
        Properties props = new Properties();
        File f = new File(System.getProperty("user.dir") + "/dados.properties");
      
        if (f.exists()) {
            FileInputStream file = new FileInputStream(f);
            props.load(file);
        }
        
        return props;
    } 
    
    public String getUrlCentralizadora()
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

                ret = schema + "://" + ip + ":" + port + "/" + servelet + "/" + retorno + "?pass=" + pass;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return ret;
    }
    
    private void sendValuesToUrl(Resultado res)
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
}
