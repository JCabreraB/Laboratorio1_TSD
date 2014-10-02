/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;
import rmi_interface.Chat;


/**
 *
 * @author Edgar Gatica
 * @title Taller de sistemas distribuidos - Laboratorio 1
 */
public class ImplementacionChat extends UnicastRemoteObject implements Chat {
    
    static String mensaje;
    
    static Logger logger;

    public ImplementacionChat() throws RemoteException {
        logger = Logger.getLogger(getClass().getName());
        logger.log(Level.INFO, "Se ha instanciado la clase de Implementacion del Chat");
        mensaje = new String("");
    }

    /*
     * Debo escribir todos los métodos que se encuentran en la interface
     */
    // Por cada metodo se escribe Override que se utiliza para que utilize este metodo en vez del metodo del padre
    @Override
    public void enviarMensaje(String mens) throws RemoteException {
        logger.log(Level.INFO, "Se desea enviar mensaje a todos los usuarios");
        
        mensaje = mens;
        
        logger.log(Level.INFO, mensaje);
    }

    @Override
    public String recibirMensaje() throws RemoteException {
        logger.log(Level.INFO, "Se va a enviar el mensaje a todos los usuarios del servidor");
        return this.mensaje;
    }
    
}
