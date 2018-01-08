package sod.applc.modelo;
import android.os.AsyncTask;

/**
 * Created by sebastian on 4/12/17.
 */

public  class HiloCliente extends AsyncTask<String, String, Cliente>{


    private Cliente cliente;


    public HiloCliente (Cliente c){

        cliente=c;
    }


    @Override
    protected Cliente doInBackground(String... params) {




        cliente.iniciarCliente();
        return cliente;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Cliente cliente) {
        super.onPostExecute(cliente);

        this.cliente=cliente;
    }

    public Cliente darCliente(){

        return cliente;
    }
}
