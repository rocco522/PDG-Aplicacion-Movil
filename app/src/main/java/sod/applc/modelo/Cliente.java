package sod.applc.modelo;

import android.util.Log;
import android.widget.Toast;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import sod.applc.control.MainActivity;
import sod.applc.modelo.ListenerMensaje;

/**
 * Created by Ricardo on 4/12/17.
 */

    public class Cliente  implements ListenerMensaje  {

    public final static String SERVIDOR_IP = "192.168.100.120";
    public final static int PUERTO = 4444;




    private PrintWriter escritor;
    private BufferedReader lector;

    private String mensajeDelServidor;
    private String estado;
    private boolean estadoServidor;
    private ListenerMensaje listenerMensaje;

    private boolean ejecutando;


    private Socket socket;

    public Cliente(ListenerMensaje listener) {

        listenerMensaje = listener;
        estadoServidor=false;

    }

    public void enviarMensaje(String mensaje) {

        if (escritor != null && !escritor.checkError()) {

            escritor.println(mensaje);
            escritor.flush();

        }


    }

    public void pararCliente() {

        ejecutando = false;

        if (escritor != null) {
            escritor.flush();
            escritor.close();

        }

        listenerMensaje = null;
        escritor = null;
        lector = null;
        mensajeDelServidor = null;

    }

    public void iniciarCliente() {


        ejecutando = true;

        try {


            socket = new Socket(SERVIDOR_IP, PUERTO);
            estadoServidor=true;

            System.out.println("HOlaaaa si sirvióoo");


            try {



                escritor = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                lector = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                while (ejecutando) {

                    mensajeDelServidor = lector.readLine();

                    if (mensajeDelServidor != null && listenerMensaje != null) {


                        recibirMensaje(mensajeDelServidor);
                        estado=mensajeDelServidor.toString();
                        System.out.println("Holaaaa recibí un mensaje "+mensajeDelServidor);


                    }
                    mensajeDelServidor=null;



                }

                Log.e("RESPONSE FROM SERVER", "S: Received Message: '" + mensajeDelServidor + "'");


            } catch (Exception e) {

                Log.e("TCP", "S: Error", e);


            } finally {

                mensajeDelServidor="no se encontró servidor";
                socket.close();

            }

        } catch(IOException e){
            Log.e("TCP", "C: Error", e);

        }


    }

    public String darEstado(){

        return estado;

    }

    @Override
    public void recibirMensaje(String mensaje) {

        mensajeDelServidor = mensaje;
    }

    public boolean darEstadoServidor(){

        return estadoServidor;
    }

}
