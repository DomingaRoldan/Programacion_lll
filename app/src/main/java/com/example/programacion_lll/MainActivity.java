package com.example.programacion_lll;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ProgressDialog progreso;
    JSONArray datosJSON;
    JSONObject jsonObject;
    Bundle parametros = new Bundle();
    int posicion = 0;

    InputStreamReader isReader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        obtenerDatos myAsync = new obtenerDatos();
        myAsync.execute();

        FloatingActionButton btn = (FloatingActionButton)findViewById(R.id.btnAgregar);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parametros.putString("accion" , "nuevo");
                agregarProd();
            }
        });

    }

    public void agregarProd() {
        Intent agregar_agenda = new Intent(MainActivity.this, nuevo_producto.class);
        agregar_agenda.putExtras(parametros);
        startActivity(agregar_agenda);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
        try {
            datosJSON.getJSONObject(info.position);
            menu.setHeaderTitle(datosJSON.getJSONObject(info.position).getJSONObject("value").getString("marca").toString());
            posicion = info.position;
        }catch (Exception ex) {
            Toast.makeText(MainActivity.this, "Error! ", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.mnxAgregar:
                parametros.putString("accion" , "nuevo");
                agregarProd();
                return true;
            case R.id.mnxModificar:
                parametros.putString("accion" , "modificar");
                try {
                    parametros.putString("valores", datosJSON.getJSONObject(posicion).getJSONObject("value").toString());
                    agregarProd();
                }catch (Exception ex){
                    Toast.makeText(MainActivity.this, "Error! ", Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.mnxEliminar:
                JSONObject miData = new JSONObject();
                try {
                    miData.put("_id", datosJSON.getJSONObject(posicion).getJSONObject("value").getString("_rev"));
                }catch (Exception ex){
                    Toast.makeText(MainActivity.this, "Error al intentar eliminar  ", Toast.LENGTH_SHORT).show();
                }
                eliminarDatos objEliminar = new eliminarDatos();
                objEliminar.execute(miData.toString());
                return true;
        }
        return super.onContextItemSelected(item);
    }

    private class obtenerDatos extends AsyncTask<Void, Void, String> {
        HttpURLConnection URLConnection;
        @Override
        protected String doInBackground(Void... params) {
            StringBuilder result = new StringBuilder();
            try{
                //la conexion al servidor
                URL url = new URL("http://10.0.2.2:5984/db_tienda/_design/tiendaProd/_view/mi-tiendaProd");
                //   URLConnection.setRequestMethod("GET");
                URLConnection = (HttpURLConnection) url.openConnection();

                InputStream in = new BufferedInputStream(URLConnection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String line;
                while ((line = reader.readLine()) != null){
                    result.append(line);
                }
            }catch (Exception ex){
                Log.e("Error!", "Error!", ex);
                ex.printStackTrace();
            }finally {
                URLConnection.disconnect();
            }
            return result.toString();
        }
        protected void onPostExecute(String s){
            super.onPostExecute(s);

            try {
                jsonObject = new JSONObject(s);
                datosJSON= jsonObject.getJSONArray("rows");
                ListView ltsProd = (ListView)findViewById(R.id.ltsProducto);

                final ArrayList<String> alProd = new ArrayList<>();
                final ArrayAdapter<String> aProd = new ArrayAdapter<>(MainActivity.this,
                        android.R.layout.simple_list_item_1, alProd);
                ltsProd.setAdapter(aProd);

                for (int i = 0; i < datosJSON.length(); i++){
                    alProd.add(datosJSON.getJSONObject(i).getJSONObject("value").getString("marca").toString());
                }
                aProd.notifyDataSetChanged();
                registerForContextMenu(ltsProd);
            }catch (Exception ex){
                Toast.makeText(MainActivity.this, "Error!: " + ex.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }

    }

    private class eliminarDatos  extends AsyncTask<String, String, String> {
        HttpURLConnection urlConnection;

        @Override
        protected String doInBackground(String... params) {
            StringBuilder result = new StringBuilder();

            String JsonResponce = null;
            String JsonData = params[0];
            BufferedReader reader = null;

            try {
                //conexion al servidor
                String uri = "http://10.0.2.2:5984/db_tienda/"+
                        datosJSON.getJSONObject(posicion).getJSONObject("value").getString("_id")+"?rev="+
                        datosJSON.getJSONObject(posicion).getJSONObject("value").getString("_rev");
                URL url = new URL(uri);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("DELETE");

                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                reader = new BufferedReader(new InputStreamReader(in));
                String line;
                while ((line = reader.readLine()) != null){
                    result.append(line);
                }
            }catch (Exception ex){
                Log.e("Error!", "Error! al intentar eliminar", ex);
                ex.printStackTrace();
            }finally {
                urlConnection.disconnect();
            }
            return result.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getBoolean("ok")){
                    Toast.makeText(MainActivity.this, "El registro se elimino con exito", Toast.LENGTH_SHORT).show();
                    Intent regresar = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(regresar);
                }else {
                    Toast.makeText(MainActivity.this, "Error al intentar eliminar el registro ", Toast.LENGTH_SHORT).show();
                }
            }catch (Exception ex){
                Toast.makeText(MainActivity.this, "Error al intentar enviar a la red"+ ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

    }


}

