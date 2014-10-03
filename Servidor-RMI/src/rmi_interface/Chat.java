/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmi_interface;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author stempel
 */
public interface Chat extends Remote {
    
    public void enviarMensaje(String mens) throws RemoteException;
    public String recibirMensaje() throws RemoteException;
    
}
