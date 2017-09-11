/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import static Entity.Parametros.BOOLEAN;
import static Entity.Parametros.BOOLEAN_TOSTRING;
import static Entity.Parametros.BYTE;
import static Entity.Parametros.BYTE_TOSTRING;
import static Entity.Parametros.FLOAT;
import static Entity.Parametros.FLOAT_TOSTRING;
import static Entity.Parametros.INT;
import static Entity.Parametros.INT_TOSTRING;
import static Entity.Parametros.STRING;
import static Entity.Parametros.STRING_TOSTRING;
import java.util.List;

/**
 *
 * @author Roberto Santin
 */
public class Metodos {
    
    public static int VOID = 0;
    
    private int idMetodo = 0;
    private int idDispositivo = 0;
    private String nomeMetodo;
    private int tipoRetorno;
    private List<Parametros> parametros;

    public Metodos() {
    }
    
    public Metodos(String nomeMetodo, List<Parametros> parametros) {
        this(nomeMetodo, 0, parametros);
    }
    
    public Metodos(String nomeMetodo, int retorno ) {
        this(nomeMetodo, retorno, null );
    }
    
    public Metodos(String nomeMetodo, int retorno, List<Parametros> parametros) {
        this.nomeMetodo = nomeMetodo;
        this.tipoRetorno = retorno;
        this.parametros = parametros;
    }

    public int getTipoRetorno() {
        return tipoRetorno;
    }

    public void setTipoRetorno(int tipoRetorno) {
        this.tipoRetorno = tipoRetorno;
    }
    
    public List<Parametros> getParametros() {
        return parametros;
    }

    public int getIdMetodo() {
        return idMetodo;
    }

    public void setIdMetodo(int idMetodo) {
        this.idMetodo = idMetodo;
    }

    public String getNomeMetodo() {
        return nomeMetodo;
    }

    public void setNomeMetodo(String nomeMetodo) {
        this.nomeMetodo = nomeMetodo;
    }

    public int getIdDispositivo() {
        return idDispositivo;
    }

    public void setIdDispositivo(int idDispositivo) {
        this.idDispositivo = idDispositivo;
    }
    
    
    
    private String tipoToString()
    {
        String ret = "";
        
        if ( tipoRetorno == VOID )
        {
            ret = "void";
        }
        
        else if ( tipoRetorno == STRING )
        {
            ret = STRING_TOSTRING;
        }
        
        else if ( tipoRetorno == INT )
        {
            ret = INT_TOSTRING;
        }
        
        else if ( tipoRetorno == BOOLEAN )
        {
            ret = BOOLEAN_TOSTRING;
        }
        
        else if ( tipoRetorno == FLOAT )
        {
            ret = FLOAT_TOSTRING;
        }
       
        else if ( tipoRetorno == BYTE )
        {
            ret = BYTE_TOSTRING;
        }
            
        return ret;
    }

    @Override
    public String toString() {

        String ret = "";
        
        if ( tipoRetorno != 0 && parametros != null )
        {
            ret += tipoToString() + " " + nomeMetodo + " (" + parametros.toString() + ") ";
        }
        
        else if ( tipoRetorno == 0 && parametros != null )
        {
            ret += "void " + nomeMetodo + " " + parametros.toString() + " ";
        }
        
        else if ( tipoRetorno != 0 && parametros == null )
        {
            ret += tipoToString() + " " + nomeMetodo + " ()";
        }
        
        else
        {
            ret += "void " + nomeMetodo + " ()";
        }
        
        return ret;
    }
    
}