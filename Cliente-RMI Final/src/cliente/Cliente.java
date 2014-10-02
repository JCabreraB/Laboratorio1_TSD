package cliente;

import java.awt.Color;
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
import java.awt.Dimension;
import java.awt.Label;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import rmi.ConexionCliente;


/**
 *
 * @author Edgar Gatica
 * @title Taller de sistemas distribuidos - Laboratorio 1
 */

public class Cliente extends JFrame implements ActionListener, Runnable{

    public static int Puerto = 2014;                                 //Número del puerto que está alojado el servidor
    public static String IPServer = "localhost";                 //Dirección IP del servidor, la cual podría utilizarse por defecto el localhost
    public static String usuarioRefRemoto = "UsuarioRemoto";    // Nombre del objeto subido
    public static String tableroRefRemota = "TableroRemoto";    // Nombre del objeto subido
    public static String chatRefRemota = "ChatRemoto";          // Nombre del objeto subido
    public static int Movimiento = 6;
    public static boolean Confirmar, Ready, confChat = false;   
    public static JButton botonIzquierdo, botonDerecho, botonArriba, botonAbajo, botonTamTablero, botonReady, confirmarNombre, enviar;
    public static int MatrizUsuario [][] = new int [10][10];
    public static JButton MatrizVisible [][] = new JButton [10][10];
    public static JTextField Nombre, mens1;
    public static JTextArea textArea;
    public static String esteUsuario, texto, auxiliarUsuario;
    public static Thread thread; 
    public static int validadorUsuario=0,contadorGlobal=0;
    public static Label Estado, IngreseUsuario;
    
    public Cliente() {
        
        setLayout(null);
        
        botonIzquierdo = new JButton("Izquierda");
        botonIzquierdo.setBounds(170, 600, 110, 30);
        add(botonIzquierdo);
        botonIzquierdo.addActionListener(this);
        botonDerecho = new JButton("Derecha");
        botonDerecho.setBounds(390, 600, 110, 30);
        add(botonDerecho);
        botonDerecho.addActionListener(this);
        botonArriba = new JButton ("Arriba");
        botonArriba.setBounds(280, 570, 110, 30);
        add(botonArriba);
        botonArriba.addActionListener(this);
        botonAbajo =  new JButton ("Abajo");
        botonAbajo.setBounds(280, 630, 110, 30);
        add(botonAbajo);
        botonAbajo.addActionListener(this);
        botonTamTablero = new JButton ("Confirmar");
        botonTamTablero.setBounds(280, 600, 110, 30);
        add(botonTamTablero);
        botonTamTablero.addActionListener(this);
        botonReady = new JButton ("Listo");
        botonReady.setBounds(460, 660, 110, 30);
        add(botonReady);
        botonReady.addActionListener(this);
        for(int i=0; i<10; i++){
        for(int j=0; j<10; j++){
        MatrizVisible[i][j] = new JButton("");
        MatrizVisible[i][j].setBounds(50*i+70, 50*j+50, 50, 50);
        add(MatrizVisible[i][j]);
        }}
        
        Estado = new Label();
        Estado.setBounds(600, 590, 350, 20);
        add(Estado);
        
        IngreseUsuario = new Label("(Aqui Ingrese su nombre de usuario)");
        IngreseUsuario.setBounds(240, 10, 250, 20);
        add(IngreseUsuario);
        
        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setBounds(620, 40, 310, 460);
        
        JScrollPane areaScrollPane = new JScrollPane(textArea);
        areaScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        areaScrollPane.setPreferredSize(new Dimension(210, 30));
        add(textArea);
        add(areaScrollPane);
        
        mens1 = new JTextField(20);
        mens1.addActionListener(this);        
        mens1.setBounds(620, 550, 210, 30);
        add(mens1);
        
        enviar =new JButton("Enviar");
        enviar.addActionListener(this);
        enviar.setBounds(850, 550, 90, 30);
        add(enviar);
        
     ///////Esto es lo nuevo/////
        Nombre = new JTextField(20);
        Nombre.addActionListener(this);        
        Nombre.setBounds(20, 10, 110, 30);
        add(Nombre);
        
        confirmarNombre =new JButton("Aceptar");
        confirmarNombre.addActionListener(this);
        confirmarNombre.setBounds(140, 10, 90, 30);
        add(confirmarNombre);
         ///////Esto es lo nuevo/////
        
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
             ///////Esto es lo nuevo/////
        if( evt.getSource()==Cliente.confirmarNombre ){
            if(Cliente.esteUsuario==null){
                Cliente.esteUsuario = Cliente.Nombre.getText();
                Cliente.validadorUsuario=1;
            }
            else{
                Cliente.auxiliarUsuario = Cliente.esteUsuario;
                Cliente.esteUsuario = Cliente.Nombre.getText();
                Cliente.validadorUsuario=2;
            }
            
        }
             ///////Esto es lo nuevo/////
        if( evt.getSource()==Cliente.enviar )
        {     
            Cliente.texto =  Cliente.mens1.getText();
            Cliente.confChat = true;
        }
    }
    
    public static void mostrar (int[][] matriz, int myTurno){
        
        
        ImageIcon raton = new ImageIcon ("raton.jpeg");
        ImageIcon trampa = new ImageIcon ("calavera.jpeg");
        ImageIcon meta = new ImageIcon ("meta.jpeg");
        int contadorTampa=0;
        
        int i,j;
        for(i=0;i<10;i++){
            for(j=0;j<10;j++){
                
                if(matriz[i][j]==1){
                    
                    MatrizVisible[j][i].setIcon(raton);
                }
                else if (matriz[i][j]==2){
                    contadorTampa++;
                    if(contadorTampa>=myTurno*10 && contadorTampa<=myTurno*10+10){
                        MatrizVisible[j][i].setIcon(trampa);
                    }
                }
                else if (matriz[i][j]==3){
                    MatrizVisible[j][i].setIcon(meta);
                }
                else{
                    MatrizVisible[j][i].setIcon(null);}
                
                System.out.print(matriz[j][i]);
            }
            System.out.println();
        } 
        
    }
    

    public static void main(String[] args) {
        
        Cliente panel = new Cliente();
        panel.setBounds(650, 250, 1000, 740);
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
                
                
                

                
                while (opcion != 5) {
                    

          

                        while(Cliente.esteUsuario==null){
                            Thread.sleep(1000);
                    }
                           
                        
                        
                        //Llama a un método del objeto remoto, y se le ingresa un parámetro a éste método
                        
                        System.out.println(Cliente.esteUsuario);

                        boolean ingreso = usuarioRemoto.ingresarUsuario(Cliente.esteUsuario);

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
                    Cliente.mostrar(tableroRemoto.getMatriz(),myTurno);
                    int Cuenta=0;

                    
                    
                    while(CantidadUsuarios!=CantidadListos){
                        
                        
                        if(Cliente.validadorUsuario == 1)
                       {   
                           
                           chatRemoto.enviarMensaje("El Usuario "+Cliente.esteUsuario+" Se ha unido a la partida");
                           Cliente.mens1.setText("");
                           Cliente.validadorUsuario=0;
                           
                       }
                        
                        if(Cliente.validadorUsuario == 2)
                       {   
                           
                           chatRemoto.enviarMensaje("El Usuario "+Cliente.auxiliarUsuario+" Se ha cambiado el nombre a "+Cliente.esteUsuario);
                           Cliente.mens1.setText("");
                           Cliente.validadorUsuario=0;
                       }
                        
                        if(Cliente.confChat == true)
                       {   
                           
                           chatRemoto.enviarMensaje(Cliente.esteUsuario + ": " +Cliente.texto);
                           Cliente.mens1.setText("");
                           Cliente.confChat = false;
                       }
                        
                        
                        CantidadUsuarios=usuarioRemoto.verUsuarios().size();
                        CantidadListos=usuarioRemoto.getListos().size();
                        
                       // System.out.println("Cantidad de Usuarios: "+ CantidadUsuarios +" Cantidad de Listos: "+CantidadListos );
                        
                        if(Ready==true && Cuenta!=1){
                        usuarioRemoto.registrarListos(Ready);
                        Cuenta=1;
                        }
                        
                    }
                    
                    //tableroRemoto.cambiarJuego();
                    
                    if(tableroRemoto.EnJuego()){
                    Cliente.Estado.setText("La Sala ha sido Ocupada. Intente mas Tarde");
                    Thread.sleep(5000);
                    opcion=5;
                    Cliente.Movimiento=5;
                }
                    Thread.sleep(1000);
                    tableroRemoto.cambiarJuego();
                    
                    tableroRemoto.PonerTrampas(); 
                    Cliente.mostrar(tableroRemoto.getMatriz(),myTurno);
                    
                 
                    while(Cliente.Movimiento!=5){
                        
                       
                       if(Cliente.validadorUsuario == 1)
                       {   
                           
                           chatRemoto.enviarMensaje("El Usuario "+Cliente.esteUsuario+" Se ha unido a la partida");
                           Cliente.mens1.setText("");
                           Cliente.validadorUsuario=0;
                       }
                        
                        if(Cliente.validadorUsuario == 2)
                       {   
                           
                           chatRemoto.enviarMensaje("El Usuario "+Cliente.auxiliarUsuario+" Se ha cambiado el nombre a "+Cliente.esteUsuario);
                           Cliente.mens1.setText("");
                           Cliente.validadorUsuario=0;
                       }
                       if(Cliente.confChat == true)
                       {   
                           
                           chatRemoto.enviarMensaje(Cliente.esteUsuario + ": " +Cliente.texto);
                           Cliente.mens1.setText("");
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
                         
                         
                       
                       }
                       else Cliente.Confirmar=false;
                       
                       
                       if(Cliente.validadorUsuario == 1)
                       {   
                           
                           chatRemoto.enviarMensaje("El Usuario "+Cliente.esteUsuario+" Se ha unido a la partida");
                           Cliente.mens1.setText("");
                           Cliente.validadorUsuario=0;
                       }
                        
                        if(Cliente.validadorUsuario == 2)
                       {   
                           
                           chatRemoto.enviarMensaje("El Usuario "+Cliente.auxiliarUsuario+" Se ha cambiado el nombre a "+Cliente.esteUsuario);
                           Cliente.mens1.setText("");
                           Cliente.validadorUsuario=0;
                       }
                       if(Cliente.confChat == true)
                       {       
                           chatRemoto.enviarMensaje(Cliente.esteUsuario + ": " +Cliente.texto);
                           Cliente.mens1.setText("");
                           Cliente.confChat = false;
                       }
                       
                       if(tableroRemoto.getCondicion()){
                         Thread.sleep(200);
                         Cliente.mostrar(tableroRemoto.getMatriz(),myTurno);
                         tableroRemoto.cambiarCondicion();
                         int PuntajeGlobal = tableroRemoto.Puntaje();
                         
                         if(!tableroRemoto.verEstado()){
                             
                            int MensajeDelServidor = tableroRemoto.MensajeJuego();
                            
                            Cliente.Estado.setText("");
                            
                            if (PuntajeGlobal<=0){
                                
                                Cliente.Estado.setText("Perdiste");
                                Cliente.Estado.setForeground(Color.RED);
                                Thread.sleep(3000);
                                opcion = 5;
                                Cliente.Movimiento = 5;}
                            
                            if(MensajeDelServidor == -1){
                                Cliente.Estado.setText("Han caido en una trampa (-100 puntos). Su puntacion es: "+PuntajeGlobal);
                            
                            }
                             else{
                                Cliente.Estado.setText("Su puntacion es: "+PuntajeGlobal);
                               
                            }
                           
                                    
                        }
                          else{
                             Cliente.Estado.setText("Felicitaciones Terminaron el juego. La Puntuacion Final es: "+ PuntajeGlobal);
                                Cliente.Estado.setForeground(Color.BLUE);
                                Thread.sleep(3000);
                                opcion = 5;
                                Cliente.Movimiento = 5;
                            }
                       }
                       
                        TurnoActual=usuarioRemoto.getTurno();
                        //System.out.println("mi turno es: " + myTurno + " el turno Actual es: "+TurnoActual);
                       
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
                        
                        Cliente.contadorGlobal++;
                        if(Cliente.contadorGlobal>=26){
                            Cliente.textArea.setText("");
                            Cliente.contadorGlobal=0;
                        }
                        mensaje = mensajeActual;                                
                        System.out.println(mensaje);
                        Cliente.textArea.append(mensaje+"\n");
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
