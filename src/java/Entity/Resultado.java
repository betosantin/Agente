package Entity;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 *
 * @author Roberto Santin
 */
public class Resultado {
    
    public static int DIRECTION_SEND = 0;
    public static int DIRECTION_RECEIVE = 1;
    
    private int idhistorico;
    private String dispositivo;
    private String usuario;
    private String nomeEvento;
    private long data;
    private int direcao;
    private String value;

    public Resultado(String dispositivo, String usuario, String nomeEvento, long data, int direcao, String value) {
        this.dispositivo = dispositivo;
        this.usuario = usuario;
        this.nomeEvento = nomeEvento;
        this.data = data;
        this.direcao = direcao;
        this.value = value;
    }

    public Resultado() {
    }
    

    public int getIdhistorico() {
        return idhistorico;
    }

    public void setIdhistorico(int idhistorico) {
        this.idhistorico = idhistorico;
    }

    public String getDispositivo() {
        return dispositivo;
    }

    public void setDispositivo(String dispositivo) {
        this.dispositivo = dispositivo;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getNomeEvento() {
        return nomeEvento;
    }

    public void setNomeEvento(String nomeEvento) {
        this.nomeEvento = nomeEvento;
    }

    public long getData() {
        return data;
    }

    public void setData(long data) {
        this.data = data;
    }

    public int getDirecao() {
        return direcao;
    }

    public void setDirecao(int direcao) {
        this.direcao = direcao;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    
    public String getDataToString()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        
        return sdf.format(new Date(this.getData()));
    }

    public String getDirectionToString()
    {
        if ( this.direcao == DIRECTION_SEND )
        {
            return "Enviado";
        }
        else
        {
            return "Recebido";
        }
    }
    
    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        
        return idhistorico + " - " + dispositivo + " - " + sdf.format(data);
    }
    
}
