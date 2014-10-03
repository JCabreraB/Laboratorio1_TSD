package cliente;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import rmi_interface.Tablero;
import rmi_interface.Usuario;
import rmi_interface.Chat;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextField;
import rmi.ConexionCliente;


/**
 *
 * @author Daniel Wladdimiro Cottet
 * @title Taller de sistemas distribuidos - Clase 1
 */

public class Cliente extends JFrame implements ActionListener, Runnable{

    public static int Puerto = 2014;                                 //Número del puerto que está alojado el servidor
    public static String IPServer = "localhost";                      //Dirección IP del servidor, la cual podría utilizarse por defecto el localhost
    public static String usuarioRefRemoto = "UsuarioRemoto";    // Nombre del objeto subido
    public static String tableroRefRemota = "TableroRemoto";    // Nombre del objeto subido
    public static String chatRefRemota = "ChatRemoto";    // Nombre del objeto subido
    public static int Movimiento = 6;
    public static boolean Confirmar, Ready, confChat = false;
    public static JButton botonIzquierdo, botonDerecho, botonArriba, botonAbajo, botonTamTablero, botonReady, confirmarNombre;
    public static int MatrizUsuario [][] = new int [10][10];
    public static JTextField Nombre;
    public static String esteUsuario;
    public static Thread thread; 
    
    public Cliente() {
        
        setLayout(null);
        botonIzquierdo = new JButton("Izquierda");
        botonIzquierdo.setBounds(10, 40, 110, 30);
        add(botonIzquierdo);
        botonIzquierdo.addActionListener(this);
        botonDerecho = new JButton("Derecha");
        botonDerecho.setBounds(230, 40, 110, 30);
        add(botonDerecho);
        botonDerecho.addActionListener(this);
        botonArriba = new JButton ("Arriba");
        botonArriba.setBounds(120, 10, 110, 30);
        add(botonArriba);
        botonArriba.addActionListener(this);
        botonAbajo =  new JButton ("Abajo");
        botonAbajo.setBounds(120, 70, 110, 30);
        add(botonAbajo);
        botonAbajo.addActionListener(this);
        botonTamTablero = new JButton ("Confirmar");
        botonTamTablero.setBounds(120, 40, 110, 30);
        add(botonTamTablero);
        botonTamTablero.addActionListener(this);
        botonReady = new JButton ("Listo");
        botonReady.setBounds(260, 100, 110, 30);
        add(botonReady);
        botonReady.addActionListener(this);
        
        Nombre = new JTextField(20);
        Nombre.addActionListener(this);        
        Nombre.setBounds(20, 10, 110, 30);
        add(Nombre);
        
        confirmarNombre =new JButton("Aceptar");
        confirmarNombre.addActionListener(this);
        confirmarNombre.setBounds(220, 10, 90, 30);
        add(confirmarNombre);
        
        
        
        this.setTitle("Servidor BD1");
        Thread t= new Thread(this);
        t.start();
    }
    
    @Override
    public void actionPerformed(ActionEvent evt) {
        
    
      
        if(evt.getSource()==Cliente.botonIzquierdo) 
            Cliente.Movimiento=0;
        if( evt.getSource()==Cliente.botonDerecho ) 
            Cliente.Movimiento=1;
        if( evt.getSource()==Cliente.botonArriba ) 
            Cliente.Movimiento=2;
        if( evt.getSource()==Cliente.botonAbajo )
            Cliente.Movimiento=3;
        if( evt.getSource()==Cliente.botonTamTablero )
            Cliente.Confirmar=true;
        if( evt.getSource()==Cliente.botonReady )
            Cliente.Ready=true;
        if( evt.getSource()==Cliente.confirmarNombre )
        {     
            Cliente.esteUsuario = Cliente.Nombre.getText();
            Cliente.confChat = true;
        }
    }
    
    public static void mostrar (int[][] matriz){
        int i,j;
        for(i=0;i<10;i++){
            for(j=0;j<10;j++){
                System.out.print(matriz[i][j]+" ");
            }
            System.out.println();
        }
    }
    
   

    public static void main(String[] args) {
        Cliente panel = new Cliente();
        panel.setBounds(650, 350, 220, 100);
        panel.setVisible(true);
        
        
        
        Usuario usuarioRemoto; //Se crea un nuevo objeto llamado objetoRemoto
        Tablero tableroRemoto;  //Se crea un nuevo objeto llamado tableroRemoto
        Chat chatRemoto;  //Se crea un nuevo objeto llamado chatRemoto

        //Se instancia el objeto que conecta con el servidor
        ConexionCliente conexion = new ConexionCliente();
        try {
            //Se conecta con el servidor

            if (conexion.iniciarRegistro(IPServer, Puerto, usuarioRefRemoto) && conexion.iniciarRegistro(IPServer, Puerto, tableroRefRemota) && conexion.iniciarRegistro(IPServer, Puerto, chatRefRemota)) {

                //Se obtiene la referencia al objeto remoto
                usuarioRemoto = conexion.getUsuarioServidor();
                tableroRemoto = conexion.getTableroServidor();
                chatRemoto = conexion.getChatServidor();
                int opcion=0;
                
                tableroRemoto.PonerTrampas();

                
                while (opcion != 5) {                    

                        System.out.println("Ingrese el nombre del usuario: ");
                        Scanner sc = new Scanner(System.in);
                        String usuario = sc.next();

                        //Llama a un método del objeto remoto, y se le ingresa un parámetro a éste método

                        boolean ingreso = usuarioRemoto.ingresarUsuario(usuario);

                        if (ingreso) {
                            System.out.println("¡Felicitaciones, ha sido agregado el usuario!");
                        } else {
                            System.out.println("Lamentablemente no ha sido ingresado el usuario, pruebe con otro nombre...");
                        }

                    
                        
                    int CantidadUsuarios = usuarioRemoto.verUsuarios().size();
                    int myTurno = usuarioRemoto.verUsuarios().size()-1;
                    int TurnoActual = usuarioRemoto.getTurno();
                    int CantidadListos=usuarioRemoto.getListos().size();
                    panel.setTitle("Usuario: "+myTurno);
                    
                    //int CeldasUsuario = 100/CantidadUsuarios;
                    int Cuenta=0;
                    int Inicio=0;
                    //int Compatible = 100%CantidadUsuarios;
                    ArrayList <String> Usuarios = usuarioRemoto.verUsuarios();
                    int MatrizServidor[][] = tableroRemoto.getMatriz();
                    
                    
                    while(CantidadUsuarios!=CantidadListos){
                        
                        CantidadUsuarios=usuarioRemoto.verUsuarios().size();
                        CantidadListos=usuarioRemoto.getListos().size();
                        
                        //System.out.println("Cantidad de Usuarios: "+ CantidadUsuarios +" Cantidad de Listos: "+CantidadListos );
                        System.out.print("");
                        
                        if(Ready==true && Cuenta!=1){
                        usuarioRemoto.registrarListos(Ready);
                        Cuenta=1;
                        }
                        
                    }
                    
                 
                    while(Cliente.Movimiento!=5){
                        
                        
                       if(Cliente.confChat == true)
                       {
                           chatRemoto.enviarMensaje(Cliente.esteUsuario);
                           Cliente.confChat = false;
                       }
    
                       if(Cliente.Confirmar==true && TurnoActual == myTurno){
                           
                            //System.out.println("Aló");
                            System.out.println("-------------------------------------------------------------------- ");

                         if(Cliente.Movimiento==0) {
                             tableroRemoto.MoverPieza(0);
                             Cliente.Confirmar=false;}
                         else if(Cliente.Movimiento==1){ 
                                 tableroRemoto.MoverPieza(1); 
                                 Cliente.Confirmar=false;}
                         else if(Cliente.Movimiento==2) {
                                 tableroRemoto.MoverPieza(2);
                                 Cliente.Confirmar=false;}
                         else if(Cliente.Movimiento==3) {
                                 tableroRemoto.MoverPieza(3);
                                 Cliente.Confirmar=false;}
                         else System.out.println("Aló");
                         
                         usuarioRemoto.entregarTurno();
                         Cliente.mostrar(tableroRemoto.getMatriz());
                         
                       
                       }
                       else Cliente.Confirmar=false;
                       
                       
                       if(Cliente.confChat == true)
                       {
                           chatRemoto.enviarMensaje(Cliente.esteUsuario);
                           Cliente.confChat = false;
                       }
                       
                        TurnoActual=usuarioRemoto.getTurno();
                        //System.out.println("mi turno es: " + myTurno + " el turno Actual es: "+TurnoActual);
                       System.out.print("");
                       
                       
                       
                        //Cliente.Confirmar=false;
                    //System.out.println("Aló "+ Cliente.Confirmar + " "+ Cliente.Movimiento);
                       System.out.print("");
                    //Cliente.Movimiento = 6;
                    }
                   
                    

                    
                    if (opcion != 5) {

                    } else if (opcion != 3) {
                        System.out.println("Ingrese un número válido por favor...");
                    }
                
            }

        } 

        System.exit(0);}
    
catch (Exception e) {

            System.out.println("Ha ocurrido un error... " + e);


        }

}
    

    @Override
    public void run() {
        String mensaje = new String(); //String del mensaje entregado por el servidor
        Chat chatRemotoHebra; //Se crea un nuevo objeto llamado objetoRemoto

        //Se instancia el objeto que conecta con el servidor
        ConexionCliente conexion = new ConexionCliente();
        try {
            //Se conecta con el servidor
            if (conexion.iniciarRegistro(IPServer, Puerto, chatRefRemota)) {

                //Se obtiene la referencia al objeto remoto
                chatRemotoHebra = conexion.getChatServidor();

                while (true) {
                    //Obtenemos el valor entregado por el servidor
                    String mensajeActual = chatRemotoHebra.recibirMensaje();
                    //De ser distinto al mensaje anterior, cambiamos el valor
                    //del mensaje nuevo y lo imprimimos
                    if(!mensajeActual.equals(mensaje)){
                        mensaje = mensajeActual;                                
                        System.out.println(mensaje);
                    }
                    //La hebra se deja dormida 1 segundo, por lo tanto,
                    //cada 1 segundo estará consulta al servidor si existe
                    //un nuevo mensaje que entregar
                    Thread.sleep(1000);
                }
            }
        } catch (RemoteException | InterruptedException e) {
            System.out.println("Ha ocurrido un error..." + e);
        } catch (NotBoundException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

   


}
