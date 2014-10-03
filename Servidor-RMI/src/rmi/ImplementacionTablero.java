package rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import rmi_interface.Tablero;

/**
 *
 * @author Daniel Wladdimiro Cottet
 * @title Taller de sistemas distribuidos - Clase 1
 */

public class ImplementacionTablero extends UnicastRemoteObject implements Tablero {
    
    static int tamanoTablero;
    static int MatrizTablero[][] = new int [10][10];
    static int PosX;
    static int PosY;
    static boolean Semovio, Termino = false;
    static int Puntaje;
    static int Mensaje;
    
    
    
    static Logger logger;

    public ImplementacionTablero() throws RemoteException {
        logger = Logger.getLogger(getClass().getName());
        logger.log(Level.INFO, "Se ha instanciado la clase de Implementacion del Servidor");
        tamanoTablero = 100;
        PosX = 0;
        PosY = 0;
        MatrizTablero[0][0] = 1;
        MatrizTablero[9][9] = 3;
        Puntaje=1000;
        Mensaje=0;
        
    }

    /*
     * Debo escribir todos los métodos que se encuentran en la interface
     */
    // Por cada metodo se escribe Override que se utiliza para que utilize este metodo en vez del metodo del padre
    @Override
    public void editarTablero(int tamanoTablero) throws RemoteException {
        //Se cambia el tamano del tablero,
        //el cual se debe tratar con "ImplementacionTablero"
        //y no con "this". Esto se debe a que es una variable
        //estática, de tal manera que al inicializar la clase
        //ImplementacionTablero siempre se utilice este valor.
        ImplementacionTablero.tamanoTablero = tamanoTablero;
        logger.log(Level.INFO, "Se ha actualizado el tablero a la cantidad de: {0}", ImplementacionTablero.tamanoTablero);
    }

    @Override
    public String actTablero() throws RemoteException {
        return Integer.toString(tamanoTablero);
    }
    
    @Override
    public void MoverPieza(int Lado) throws RemoteException{
        
     //System.out.println("Entro");
        
        int i=0;
        int j=0;
        boolean encontrado = false;
        
        for(i=0; i<10; i++){
            for(j=0; j<10; j++){
                if(ImplementacionTablero.MatrizTablero[i][j]==1){
                    ImplementacionTablero.MatrizTablero[i][j] = 0;
                    encontrado = true;
                    break;
                }                 
            }
            if(encontrado==true) break;
        }
          
        //System.out.println("Entro "+ Lado + i);
        
        if(Lado==0 && j!=0 ) j = j-1;
        else if(Lado==1 && j!=9) j= j+1;
        else if(Lado==2 && i!=0) i = i-1;
        else if(Lado==3 && i!=9) i = i+1; 
        else System.out.println("Movimiento imposible");
        
        verificarPocision(i,j);
        
        ImplementacionTablero.MatrizTablero[i][j] =1 ;
        ImplementacionTablero.Semovio=true;
        
        
        
        System.out.println("La pieza se encuentra en la posicion ["+ i + "][" + j + "]");
    
    }
    
    @Override
    public void verificarPocision(int x, int y) throws RemoteException{
    
        if(ImplementacionTablero.MatrizTablero[x][y]==2){
            ImplementacionTablero.Puntaje = ImplementacionTablero.Puntaje - 100;
            ImplementacionTablero.Mensaje=-1;
            }
        
        else if(ImplementacionTablero.MatrizTablero[x][y]==3){
            ImplementacionTablero.Termino = true;
            ImplementacionTablero.Mensaje=1;}
        else 
            ImplementacionTablero.Puntaje = ImplementacionTablero.Puntaje - 5;
        
        
           
    }
    
    @Override
    public int MensajeJuego() throws RemoteException{
    return ImplementacionTablero.Mensaje;
    }
    
    @Override
    public boolean verEstado() throws RemoteException{
    return ImplementacionTablero.Termino;
    }
    
    @Override
    public int Puntaje() throws RemoteException{
    return ImplementacionTablero.Puntaje;
    }
        
    @Override
    public void PonerTrampas (){
    
        int Trampas = 10;
        Random rnd = new Random();
        
        while(Trampas!=0){
            for(int i=0; i<10; i++){
                for(int j=0; j<10; j++){
                    
                    if(ImplementacionTablero.MatrizTablero[i][j]!=1 && ImplementacionTablero.MatrizTablero[i][j]!=3){
                    int randomNum = rnd.nextInt((4 - 0) + 1) + 0;
                    
                        if(randomNum == 2){
                        ImplementacionTablero.MatrizTablero[i][j] = 2;
                        Trampas = Trampas -1;
                        System.out.println("La trampa se encuentra en la posicion ["+ i + "][" + j + "]");
                        
                        } 
                        
                    }
                    if(Trampas==0)break;
                }
                if(Trampas==0)break;
            }
        }
    }

    
    @Override
    public int[][] getMatriz() throws RemoteException{
    return ImplementacionTablero.MatrizTablero;}
    
    @Override
    public boolean getCondicion() throws RemoteException{
    return ImplementacionTablero.Semovio;
    }
    
    @Override
    public void cambiarCondicion () throws RemoteException{    
    ImplementacionTablero.Semovio=false;
    }
}
