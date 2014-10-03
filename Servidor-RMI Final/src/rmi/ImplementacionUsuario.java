package rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import rmi_interface.Usuario;

/**
 *
 * @author Edgar Gatica
 * @title Taller de sistemas distribuidos - Laboratorio 1
 */
public class ImplementacionUsuario extends UnicastRemoteObject implements Usuario {
    
    static ArrayList<String> usuarios;
    static ArrayList<Boolean> usuariosListos;
    static int turnoActual; 
    
    static Logger logger;

    public ImplementacionUsuario() throws RemoteException {
        logger = Logger.getLogger(getClass().getName());
        logger.log(Level.INFO, "Se ha instanciado la clase de Implementacion del Servidor");
        usuarios = new ArrayList<>();
        usuariosListos = new ArrayList<>();
        turnoActual = 0;
    }

    /*
     * Debo escribir todos los métodos que se encuentran en la interface
     */
    // Por cada metodo se escribe Override que se utiliza para que utilize este metodo en vez del metodo del padre
    @Override
    public boolean ingresarUsuario(String usuario) throws RemoteException {
        logger.log(Level.INFO, "Se desea ingresar un usuario al servidor");
        if(!usuarios.contains(usuario)){
            usuarios.add(usuario);
            return true;
        } else {
            return false;
        }
    }
    
    @Override
    public void registrarListos (boolean Listo) throws RemoteException{
        
        if(Listo==true){
        usuariosListos.add(Listo);
        }        
    }
    
    @Override
    public ArrayList<Boolean> getListos() throws RemoteException{
     return usuariosListos;
    }

    @Override
    public ArrayList<String> verUsuarios() throws RemoteException {
        logger.log(Level.INFO, "Se desea ver los usuarios del servidor");
        return usuarios;
    }
    
    @Override
    public int entregarTurno() throws RemoteException {
    
        if(ImplementacionUsuario.turnoActual+1==usuarios.size()){
            ImplementacionUsuario.turnoActual=0;}
        else
            ImplementacionUsuario.turnoActual=ImplementacionUsuario.turnoActual+1;
        
        return ImplementacionUsuario.turnoActual;
        
        
    }
    
     @Override
    public int getTurno() throws RemoteException {
    
        
        return ImplementacionUsuario.turnoActual;
        
        
    }

}
