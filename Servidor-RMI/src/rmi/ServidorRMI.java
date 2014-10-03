package rmi;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;
import static rmi.ImplementacionUsuario.logger;
import rmi_interface.Tablero;
import rmi_interface.Usuario;

/**
 *
 * @author Daniel Wladdimiro Cottet
 * @title Taller de sistemas distribuidos - Clase 1
 */

public class ServidorRMI {

    private Registry registro;
    private boolean conectado;

    static Logger logger;

    public ServidorRMI() {
        this.conectado = false;
        this.registro = null;
        logger = Logger.getLogger(getClass().getName());
        logger.log(Level.INFO, "Se ha instanciado la clase Servidor RMI");
    }
   
    private void iniciarRegistro(int Puerto) throws RemoteException {
        try {
            registro = LocateRegistry.getRegistry(Puerto); //Obtiene el objeto remoto
            registro.list(); //Por lo que posteriormente, entrega una registro con la lista de objetos remotos

            conectado = true;
        } catch (RemoteException e) {
            registro = LocateRegistry.createRegistry(Puerto); //Se creará el objeto a nivel de localhost
            registro.list();  //Por lo que posteriormente, entrega una registro con la lista de objetos remotos

            conectado = true;
        }
    }

    //Ingresa el objeto referenciado al registro del servidor, de tal manera
    //que pueda ser utilizado posteriormente de forma remota. Este objeto va a ser
    //genérico, debido que pueden ser de muchos tipos, en este caso Usuario o
    //Tablero, de tal manera que después se castee el tipo de la clase.
    public boolean iniciarConexion(Object objeto, String nombre, int Puerto) {      
        try {
            this.registro = getRegistro(Puerto);

            //Para poder realizar el objeto remoto, deberá estar en el Registry
            //del servidor, por lo que con el método rebind quedará registrado
            //con el nombre de referencia del objeto y el objeto inicializado
            //que entró por parámetro
            
            //Es importante analizar que se casteará que tipo de implementación es que la está entrando
            if (objeto.getClass().equals(ImplementacionUsuario.class)){
                logger.log(Level.INFO, "Se agregara ".concat(nombre).concat(" con el objecto Usuario"));
                //De tal manera que aquí se castee la interface que le corresponde
                registro.rebind(nombre, (Usuario) objeto);
            }
            else if (objeto.getClass().equals(ImplementacionTablero.class)){
                logger.log(Level.INFO, "Se agregara ".concat(nombre).concat(" con el objecto Tablero"));
                registro.rebind(nombre, (Tablero) objeto);
            }
            
            //Nota: Esto también se podría realizar de otra manera, debido que 
            //se podrían implementar dos interfaces a la misma implementación.
            //De ser así, esta forma no serviría, debido que aquí estamos
            //aplicando distintas implementaciones a distintas interfaces.
               
        } catch (RemoteException re) {
            //En caso de haber un error, es mostrado por un mensaje
            logger.log(Level.SEVERE, re.getMessage());
            return false;
        }
        return true;
    }

    //Quita del registro del servidor la referencia al objeto remoto
    public void detenerConexion(String nombreUsado) throws RemoteException {
        try {
            //Se saca de RMI Registry el objeto "Ejemplo-RMI"
            //El cual ya no estará disponible para ser llamado remotamente
            registro.unbind(nombreUsado);
            conectado = false;
        } catch (NotBoundException | AccessException ex) {
            //En caso de haber un error, es mostrado por un mensaje
            logger.log(Level.SEVERE, null, ex);
            conectado = true;
        }
    }

    public Registry getRegistro(int Puerto) throws RemoteException {
        if (this.registro == null) {
            iniciarRegistro(Puerto);
        }
        return registro;
    }

    public void setRegistro(Registry registro) {
        this.registro = registro;
    }

    public boolean getConectado() {
        return conectado;
    }

    public void setConectado(boolean conectado) {
        this.conectado = conectado;
    }

}
