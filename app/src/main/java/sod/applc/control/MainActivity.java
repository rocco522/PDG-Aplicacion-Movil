package sod.applc.control;


import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ToggleButton;

import sod.applc.R;
import sod.applc.modelo.Cliente;
import sod.applc.modelo.HiloCliente;
import sod.applc.modelo.ListenerMensaje;
import sod.applc.modelo.Logs;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import static android.R.attr.delay;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, Serializable{


    private ToggleButton btnApagarPLC;
    private ToggleButton btnApagarCA;
    private Button btnPosicionar;
    private EditText txtConsola;

    private dbLogs sql;
    private SQLiteDatabase bd;

    private Cliente cliente;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ;
        btnApagarCA = (ToggleButton) findViewById(R.id.btnCA);
        btnApagarCA.setOnCheckedChangeListener(this);
        btnApagarPLC = (ToggleButton) findViewById(R.id.btnPLC);
        btnApagarPLC.setOnCheckedChangeListener(this);
        btnPosicionar = (Button) findViewById(R.id.btnPosicionar);
        btnPosicionar.setOnClickListener(this);
        txtConsola = (EditText) findViewById(R.id.txtConsola);

        actualizarConsola("Cargando...");
        inhabibilitarBotones();


        cliente = new Cliente(new ListenerMensaje() {
            @Override
            public void recibirMensaje(String mensaje) {

            }
        });{

        }

        sql = new dbLogs(this);
        bd= sql.getWritableDatabase();

        conectarServidor();

        new CountDownTimer(10000, 1000) {
            public void onFinish() {

                actualizarConsola("Conexión al Servidor: "+cliente.darEstadoServidor());
                if(!cliente.darEstadoServidor()){

                    inhabibilitarBotones();


                }else{
                    habibilitarBotones();
                }

            }

            public void onTick(long millisUntilFinished) {
                // millisUntilFinished    The amount of time until finished.
            }
        }.start();

      }


   public ArrayList<String> darLogs(){

        ArrayList<String> valor = new ArrayList<String>();


        Cursor c = sql.getReadableDatabase().query(Logs.Log.TABLE_NAME,null,null,null,null,null,null);

        while (c.moveToNext()){

            String accion =  c.getString(c.getColumnIndex(Logs.Log.ACCION));
            String resul =  c.getString(c.getColumnIndex(Logs.Log.RESUL));
            String fecha =  c.getString(c.getColumnIndex(Logs.Log.FECHA));

            String accionCompleta = accion+" - "+resul+" - "+fecha;

            valor.add(accionCompleta);
        }

        return valor;
    }

      public void inhabibilitarBotones(){

          btnApagarCA.setEnabled(false);
          btnApagarPLC.setEnabled(false);
          btnPosicionar.setEnabled(false);
      }

    public void habibilitarBotones(){

        btnApagarCA.setEnabled(true);
        btnApagarPLC.setEnabled(true);
        btnPosicionar.setEnabled(true);
    }

      public void abrirLogs(){

          Intent abrLogs = new Intent(getApplicationContext(), LogsActivity.class);

          ArrayList<String> valores = darLogs();
          abrLogs.putStringArrayListExtra("Logs",valores);


          startActivity(abrLogs);
      }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.logs:
                abrirLogs();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

      public void insertarLog(String ac, String fecha, String resul){

          ContentValues registro = new ContentValues();

          registro.put("accion",ac);
          registro.put("resultado",resul);
          registro.put("fecha",fecha);

          bd.insert(Logs.Log.TABLE_NAME,null,registro);


      }

      public Date obtenerFecha(){

          Calendar c = Calendar.getInstance();


          return c.getTime();

      }



    @Override
    public void onClick(View v) {


         if (v.getId() == R.id.btnPosicionar) {

            String mensajeAEnviar = "Robot";
            cliente.enviarMensaje(mensajeAEnviar);
            actualizarConsola(mensajeAEnviar);
            insertarLog("Posicionar",obtenerFecha().toString(),"OK");


        }

    }


    public void conectarServidor() {

        HiloCliente hilo = new HiloCliente(cliente);



        hilo.execute("");

        cliente=hilo.darCliente();

            System.out.println("esto vale cliente "+cliente);


    }

    public void actualizarConsola(String mensaje) {

        txtConsola.setText(mensaje);

    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if(buttonView.getId()==R.id.btnCA){

            if(isChecked){

                String mensajeAEnviar = "PrendioCA";
                cliente.enviarMensaje(mensajeAEnviar);
                actualizarConsola(mensajeAEnviar);
                insertarLog("PrendioCA",obtenerFecha().toString(),"OK");

            }else{

                String mensajeAEnviar = "ApagoCA";
                cliente.enviarMensaje(mensajeAEnviar);
                actualizarConsola(mensajeAEnviar);
                insertarLog("ApagoCA",obtenerFecha().toString(),"OK");

            }

        }else if(buttonView.getId()==R.id.btnPLC){

            if(isChecked){

                String mensajeAEnviar = "PrendioPLC";
                cliente.enviarMensaje(mensajeAEnviar);
                actualizarConsola(mensajeAEnviar);
                insertarLog("PrendioPLC",obtenerFecha().toString(),"OK");

            }else{

                String mensajeAEnviar = "ApagoPLC";
                cliente.enviarMensaje(mensajeAEnviar);
                actualizarConsola(mensajeAEnviar);
                insertarLog("ApagoPLC",obtenerFecha().toString(),"OK");

            }

        }

    }
}
