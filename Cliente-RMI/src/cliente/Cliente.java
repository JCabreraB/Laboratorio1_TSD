package cliente;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import rmi_interface.Tablero;
import rmi_interface.Usuario;

import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.ImageIcon;
import javax.swing.JTextField;
import rmi.ConexionCliente;


/**
 *
 * @author Daniel Wladdimiro Cottet
 * @title Taller de sistemas distribuidos - Clase 1
 */

public class Cliente extends JFrame implements ActionListener{

    public static int Puerto = 2014;                                 //Número del puerto que está alojado el servidor
    public static String IPServer = "localhost";                      //Dirección IP del servidor, la cual podría utilizarse por defecto el localhost
    public static String usuarioRefRemoto = "UsuarioRemoto";    // Nombre del objeto subido
    public static String tableroRefRemota = "TableroRemoto";    // Nombre del objeto subido
    public static int Movimiento = 6;
    public static boolean Confirmar, Ready = false;   
    public static JButton botonIzquierdo, botonDerecho, botonArriba, botonAbajo, botonTamTablero, botonReady, confirmarNombre;
    public static int MatrizUsuario [][] = new int [10][10];
    public static JButton MatrizVisible [][] = new JButton [10][10];
    public static JTextField Nombre;
    public static String esteUsuario;
    
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
        if( evt.getSource()==Cliente.confirmarNombre )
            Cliente.esteUsuario = Cliente.Nombre.getText();
             ///////Esto es lo nuevo/////
    }
    
    public static void mostrar (int[][] matriz){
        
        ImageIcon raton = new ImageIcon ("raton.jpeg");
        ImageIcon trampa = new ImageIcon ("calavera.jpeg");
        ImageIcon meta = new ImageIcon ("meta.jpeg");
        int i,j;
        for(i=0;i<10;i++){
            for(j=0;j<10;j++){
                
                if(matriz[i][j]==1)
                    MatrizVisible[j][i].setIcon(raton);
                else if (matriz[i][j]==2)
                    MatrizVisible[j][i].setIcon(trampa);
                else if (matriz[i][j]==3)
                    MatrizVisible[j][i].setIcon(meta);
                else
                    MatrizVisible[j][i].setIcon(null);
                
                System.out.print(matriz[j][i]);
            }
            System.out.println();
        }
    }
    

    public static void main(String[] args) {
        
        Cliente panel = new Cliente();
        panel.setBounds(650, 250, 620, 740);
        panel.setVisible(true);
        

        
        
        Usuario usuarioRemoto; //Se crea un nuevo objeto llamado objetoRemoto
        Tablero tableroRemoto;  //Se crea un nuevo objeto llamado tableroRemoto
        

        //Se instancia el objeto que conecta con el servidor
        ConexionCliente conexion = new ConexionCliente();
        try {
            //Se conecta con el servidor

            if (conexion.iniciarRegistro(IPServer, Puerto, usuarioRefRemoto) && conexion.iniciarRegistro(IPServer, Puerto, tableroRefRemota)) {

                //Se obtiene la referencia al objeto remoto
                usuarioRemoto = conexion.getUsuarioServidor();
                tableroRemoto = conexion.getTableroServidor();
                int opcion=0;
                
                tableroRemoto.PonerTrampas();

                
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
                        
                    Cliente.mostrar(tableroRemoto.getMatriz());
                        
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
                        
                       // System.out.println("Cantidad de Usuarios: "+ CantidadUsuarios +" Cantidad de Listos: "+CantidadListos );
                        
                        if(Ready==true && Cuenta!=1){
                        usuarioRemoto.registrarListos(Ready);
                        Cuenta=1;
                        }
                        
                    }
                    
                 
                    while(Cliente.Movimiento!=5){
                       
                       
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
                       
                       if(tableroRemoto.getCondicion()){
                         Cliente.mostrar(tableroRemoto.getMatriz());                         
                         tableroRemoto.cambiarCondicion();
                       
                         if(!tableroRemoto.verEstado()){
                             
                            int MensajeDelServidor = tableroRemoto.MensajeJuego();
                            int PuntajeGlobal = tableroRemoto.Puntaje();
                            
                            if(MensajeDelServidor == -1)
                                System.out.println("Han caido en una trampa se restarán 100 puntos. Su puntacion es: "+PuntajeGlobal);
                            else if(MensajeDelServidor == 1)
                                System.out.println("Felicitaciones Terminaron el juego. La Puntuacion Final es: "+ PuntajeGlobal);
                            else
                                System.out.println("Su puntacion es: "+PuntajeGlobal);
                                    
                        }
                        else
                            opcion = 5;
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
}
