package com.example.programacion_lll;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    DB miBD;
    Cursor misProdctos;
    productos producto;
    ArrayList<productos> stringArrayList = new ArrayList<productos>();
    ArrayList<productos> copyStringArrayList = new ArrayList<productos>();
    ListView ltsAmigos;
    Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton btnAgregarProducto = (FloatingActionButton)findViewById(R.id.btnAgregarProducto);
        btnAgregarProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               agregarAmigo("nuevo", new String[]{});
            }
        });
        obtenerDatosProductos();
        buscarAmigos();

       button = (Button)findViewById(R.id.btnchat);
       button.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(getApplicationContext(), registro_chat.class);
               startActivity(intent);
           }
       });

    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_productos, menu);

        AdapterView.AdapterContextMenuInfo adapterContextMenuInfo = (AdapterView.AdapterContextMenuInfo)menuInfo;
        misProdctos.moveToPosition(adapterContextMenuInfo.position);
        menu.setHeaderTitle(misProdctos.getString(1));
    }
    void buscarAmigos(){
        final TextView tempVal = (TextView)findViewById(R.id.txtBuscarAmigos);
        tempVal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    stringArrayList.clear();
                    if (tempVal.getText().toString().trim().length() < 1) {//no hay texto para buscar
                        stringArrayList.addAll(copyStringArrayList);
                    } else {//se realiza la busqueda
                        for (productos am : copyStringArrayList) {
                            String codigo = am.getCodigo();
                            if (codigo.toLowerCase().contains(tempVal.getText().toString().trim().toLowerCase())) {
                                stringArrayList.add(am);
                            }
                        }
                    }
                    adaptadorImagenes adaptadorImg = new adaptadorImagenes(getApplicationContext(), stringArrayList);
                    ltsAmigos.setAdapter(adaptadorImg);
                }catch (Exception ex){
                    Toast.makeText(getApplicationContext(), "Error: "+ ex.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.mnxAgregar:
                agregarAmigo("nuevo", new String[]{});
                return true;

            case R.id.mnxModificar:
                String[] dataProducto = {
                        misProdctos.getString(0),//idProducto
                        misProdctos.getString(1),//codigo
                        misProdctos.getString(2),//descripcion
                        misProdctos.getString(3),//marca
                        misProdctos.getString(4), //presentacion
                        misProdctos.getString(5),  //precio
                        misProdctos.getString(6)  //urlImg
                };
                agregarAmigo("modificar",dataProducto);
                return true;

            case R.id.mnxEliminar:
                AlertDialog eliminarFriend =  eliminarAmigo();
                eliminarFriend.show();
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }
    AlertDialog eliminarAmigo(){
        AlertDialog.Builder confirmacion = new AlertDialog.Builder(MainActivity.this);
        confirmacion.setTitle(misProdctos.getString(1));
        confirmacion.setMessage("Esta seguro que quiere eliminar el registro?");
        confirmacion.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                miBD.mantenimientoProducto("eliminar",new String[]{misProdctos.getString(0)});
                obtenerDatosProductos();
                Toast.makeText(getApplicationContext(), "El producto se elimino",Toast.LENGTH_SHORT).show();
                dialogInterface.dismiss();
            }
        });
        confirmacion.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getApplicationContext(), "La eliminacion ha sido cancelada",Toast.LENGTH_SHORT).show();
                dialogInterface.dismiss();
            }
        });
        return confirmacion.create();
    }
    void obtenerDatosProductos(){
        miBD = new DB(getApplicationContext(), "", null, 1);
        misProdctos = miBD.mantenimientoProducto("consultar", null);
        if( misProdctos.moveToFirst() ){ //hay registro en la base de datos para mostrar
            mostrarDatosProductos();
        } else{ //No hay registro en la base de datos que mostrar.
            Toast.makeText(getApplicationContext(), "No hay registros de productos que mostrar",Toast.LENGTH_LONG).show();
            agregarAmigo("nuevo", new String[]{});
        }
    }
    void agregarAmigo(String accion, String[] dataAmigo){
        Bundle enviarParametros = new Bundle();
        enviarParametros.putString("accion",accion);
        enviarParametros.putStringArray("dataAmigo",dataAmigo);
        Intent agregarAmigos = new Intent(MainActivity.this, agregarProductos.class);
        agregarAmigos.putExtras(enviarParametros);
        startActivity(agregarAmigos);
    }
    void mostrarDatosProductos(){
        stringArrayList.clear();
        ltsAmigos = (ListView)findViewById(R.id.ltsAmigos);
        do {
            producto = new productos(misProdctos.getString(0),misProdctos.getString(1), misProdctos.getString(2), misProdctos.getString(3), misProdctos.getString(4), misProdctos.getString(5), misProdctos.getString(6));
            stringArrayList.add(producto);
        }while(misProdctos.moveToNext());
        adaptadorImagenes adaptadorImg = new adaptadorImagenes(getApplicationContext(), stringArrayList);
        ltsAmigos.setAdapter(adaptadorImg);

        copyStringArrayList.clear();//limpia la lista de productos
        copyStringArrayList.addAll(stringArrayList);//crear la copia de la lista de productos
        registerForContextMenu(ltsAmigos);
    }
}
class productos{
    String id;
    String codigo;
    String descripcion;
    String marca;
    String presentacion;
    String precio;

    String urlImg;

    public productos( String id, String codigo, String descripcion, String marca, String presentacion, String precio, String urlImg) {
        this.id = id;
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.marca = marca;
        this.presentacion = presentacion;
        this.precio = precio;
        this.urlImg = urlImg;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getPresentacion() {
        return presentacion;
    }

    public void setPresentacion(String presentacion) {
        this.presentacion = presentacion;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getUrlImg() {
        return urlImg;
    }

    public void setUrlImg(String urlImg) {
        this.urlImg = urlImg;
    }


}

