package rmi_interface;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Edgar Gatica
 * @title Taller de sistemas distribuidos - Laboratorio 1
 */

//Esta inteface indicará los métodos que están a dispoción del cliente y servidor
//para que puedan interactuar remotamente.
//Todos estos métodos deben poseer como mínimo la excepción RemoteException

public interface Tablero extends Remote {
    public void editarTablero(int tamanoTablero) throws RemoteException;
    public String actTablero() throws RemoteException;
    public void MoverPieza(int Lado) throws RemoteException;
    public void PonerTrampas() throws RemoteException;
    public int[][] getMatriz() throws RemoteException;
    public void cambiarCondicion () throws RemoteException;
    public boolean getCondicion() throws RemoteException;
    public void verificarPocision(int x, int y) throws RemoteException;
    public boolean verEstado() throws RemoteException;
    public int Puntaje() throws RemoteException;
    public int MensajeJuego() throws RemoteException;
    public void cambiarJuego () throws RemoteException;
    public boolean EnJuego () throws RemoteException;
}