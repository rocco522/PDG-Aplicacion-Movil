package sod.applc.control;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import sod.applc.R;
import sod.applc.modelo.Logs.Log;

public class LogsActivity extends AppCompatActivity {


    private ListView lista;
    private ArrayAdapter<String> array;

    private MainActivity main;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logs);

        lista = (ListView) findViewById(R.id.lista);
        ArrayList<String> valores = getIntent().getStringArrayListExtra("Logs");

        array = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,valores);
        lista.setAdapter(array);
    }


}
