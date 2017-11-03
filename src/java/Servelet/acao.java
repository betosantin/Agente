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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
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

/**
 *
 * @author roberto.santin
 */
public class acao extends HttpServlet {

    HttpServletRequest requisicao;
    HttpServletResponse resposta;

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
            redirect(Functions.getUrlCentralizadora(false));
        }
        
        else if ( request.getParameter("apagarled") != null )
        {
            apagarLed();
            redirect(Functions.getUrlCentralizadora(false));
        }
        
        else if ( request.getParameter("obtertemperatura") != null )
        {
            obtertemperatura();
            redirect(Functions.getUrlCentralizadora(false));
        }
        else if ( request.getParameter("obterpressao") != null )
        {
            obterpressao();
            redirect(Functions.getUrlCentralizadora(false));
        }
        else if ( request.getParameter("obterumidade") != null )
        {
            obterumidade();
            redirect(Functions.getUrlCentralizadora(false));
        }
        else if ( request.getParameter("obterldr") != null )
        {
            obterldr();
            redirect(Functions.getUrlCentralizadora(false));
        }
        else if ( request.getParameter("realizarMonitoramento") != null )
        {
            realizarMonitoramento();
            redirect(Functions.getUrlCentralizadora(false));
        }
        else if ( request.getParameter("pararMonitoramento") != null )
        {
            pararMonitoramento();
            redirect(Functions.getUrlCentralizadora(false));
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
        
        Functions f = new Functions();
        
        f.acenderLed(red, green, blue, blink, time);
    }
    
    private void apagarLed() {
        Functions f = new Functions();
        f.apagarLed();
    }
    
    private void obtertemperatura() {
        Functions f = new Functions();
        f.obtertemperatura();
    }
    
    private void obterpressao()
    {
        Functions f = new Functions();
        f.obterpressao();
    }
    
    private void obterumidade()
    {
        Functions f = new Functions();
        f.obterumidade();
    }
    
    private void obterldr() {
        Functions f = new Functions();
        f.obterldr();
    }
    
    private void realizarMonitoramento() {
        Functions f = new Functions();
        f.realizarMonitoramento();
    }
    
    private void pararMonitoramento() {
        Functions f = new Functions();
        f.pararMonitoramento();
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
            
            Properties props = Functions.getProp();
            
            String httpType = requisicao.getParameter("httptype");
            String ip = requisicao.getRemoteAddr();
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
        Metodos startmonitoramento = new Metodos( "realizarMonitoramento", Parametros.BOOLEAN, null );
        Metodos stopmonitoramento = new Metodos( "pararMonitoramento", Parametros.BOOLEAN, null );
        
        List<Metodos> metodos = new ArrayList<>();
        metodos.add(luzOn);
        metodos.add(luzOff);
        metodos.add(obtTemp);
        metodos.add(obtPress);
        metodos.add(obtHumi);
        metodos.add(obtLuminosidade);
        metodos.add(startmonitoramento);
        metodos.add(stopmonitoramento);
        
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
    
    private void redirect( String pagina )
    {
        try
        {
            resposta.sendRedirect(pagina+"conHistorico.jsp");
        }
        
        catch (Exception e)
        {
            System.out.println("erro ao encaminhar página");
        }
    }
}
