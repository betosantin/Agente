/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servelet;

import Entity.Parametros;
import Entity.Metodos;
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
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
        else if ( request.getParameter("obterhumidade") != null )
        {
            obterhumidade();
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
        
        ledThread = new LedSwitch(new Color(red, green, blue), time, blink);
        ledThread.run();
        
        System.out.println("continuando...");
    }
    
    private void apagarLed() {
        
        if ( ledThread != null )
        {
            ledThread.stop();
        }
    }
    
    private void obtertemperatura() 
    {
        W1Master w1Master = new W1Master();

        for (TemperatureSensor device : w1Master.getDevices(TemperatureSensor.class)) {
            if (device.getName().contains("28-0316a32b78ff")) {
                try {
                    resposta.getWriter().write(device.getTemperature(TemperatureScale.CELSIUS) + " ºc");
                } catch (IOException ex) {
                    Logger.getLogger(acao.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    private void obterpressao()
    {
        try {
            bme280 sensor = new bme280();
            
            sensor.readTemperature();
            float press = sensor.readPressure();
            
            resposta.getWriter().write( NF.format( press / 100 ) + " hPa");

        } catch (Exception ex) {
            Logger.getLogger(acao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void obterhumidade()
    {
        try {
            bme280 sensor = new bme280();
            
            float hum = sensor.readHumidity();
            
            resposta.getWriter().write( NF.format(hum) + " %");

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

            resposta.getWriter().write(count + " lumens?????");
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
        Metodos obtHumi = new Metodos( "obterhumidade", Parametros.FLOAT, null );
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
//            resposta.getWriter().write(g.toJson(luzOn));
            
        } catch (IOException ex) {
            Logger.getLogger(acao.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        return "";
    }

//    private void encaminharPagina(String pagina) {
//        try {
//            RequestDispatcher rd = requisicao.getRequestDispatcher(pagina);
//            rd.forward(requisicao, resposta);
//        } catch (Exception e) {
//            System.out.println("erro ao encaminhar página");
//        }
//    }

}