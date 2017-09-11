/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

/**
 *
 * @author Roberto Santin
 */
public class Parametros {
    
    public static int STRING = 1;
    public static int INT = 2;
    public static int BOOLEAN = 3;
    public static int FLOAT = 4;
    public static int BYTE = 5;
    
    public static String STRING_TOSTRING = "String";
    public static String INT_TOSTRING = "int";
    public static String BOOLEAN_TOSTRING = "boolean";
    public static String FLOAT_TOSTRING = "float";
    public static String BYTE_TOSTRING = "byte";
    
    private int idParametro = 0;
    private int idMetodo = 0;
    private String nomeParametro = "";
    private int tipo;

    public Parametros() {
    }
    
    public Parametros(String parametro, int tipo) {
        this.nomeParametro = parametro;
        this.tipo = tipo;
    }

    public String getNomeParametro() {
        return nomeParametro;
    }

    public int getTipo() {
        return tipo;
    }

    public int getIdParametro() {
        return idParametro;
    }

    public void setIdParametro(int idParametro) {
        this.idParametro = idParametro;
    }

    public void setNomeParametro(String nomeParametro) {
        this.nomeParametro = nomeParametro;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public int getIdMetodo() {
        return idMetodo;
    }

    public void setIdMetodo(int idMetodo) {
        this.idMetodo = idMetodo;
    }
    
    @Override
    public String toString() {
        
        String ret = "";
        
        if ( tipo == STRING )
        {
            ret = STRING_TOSTRING;
        }
        
        else if ( tipo == INT )
        {
            ret = INT_TOSTRING;
        }
        
        else if ( tipo == BOOLEAN )
        {
            ret = BOOLEAN_TOSTRING;
        }
        
        else if ( tipo == FLOAT )
        {
            ret = FLOAT_TOSTRING;
        }
       
        else if ( tipo == BYTE )
        {
            ret = BYTE_TOSTRING;
        }
        
        return ret + " " + nomeParametro;
    }
}
