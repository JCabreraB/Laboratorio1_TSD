
package rmi_interface;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 *
 * @author Edgar Gatica
 * @title Taller de sistemas distribuidos - Laboratorio 1
 */


//Esta inteface indicará los métodos que están a dispoción del cliente y servidor
//para que puedan interactuar remotamente.
//Todos estos métodos deben poseer como mínimo la excepción RemoteException


public interface Usuario extends Remote {
    
    public boolean ingresarUsuario(String usuario) throws RemoteException;
    public ArrayList<String> verUsuarios() throws RemoteException;
    public void registrarListos (boolean Listo) throws RemoteException;
    public ArrayList<Boolean> getListos() throws RemoteException;
    public int entregarTurno() throws RemoteException;
    public int getTurno() throws RemoteException;
}