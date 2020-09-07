package com.example.programacion_lll;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText txtMonto, txtCantidadC, txtRes;
    private Button btnConvertir;

    final String[] datos = new String[]{"PIE CUADRADO","VARA CUADRADA","YARDA CUADRADO","METRO CUADRADO","TAREAS","MANZANA","HECTAREA"};

    private Spinner AreaActual;
    private Spinner AreaCambio;
    private EditText editTvalorCambio;
    private TextView textVresultado;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        txtMonto = (EditText) findViewById(R.id.txtMonto);
        txtCantidadC = (EditText) findViewById(R.id.txtCantidadCaja);
        txtRes = (EditText) findViewById(R.id.txtResultado);

        btnConvertir =(Button)findViewById(R.id.btnConvertir);

        btnConvertir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int countRes = 0;
                int resp = 0;
                int tot = 0;


                int CountC = Integer.parseInt(txtCantidadC.getText().toString());

                if (!txtMonto.getText().toString().isEmpty() && !txtCantidadC.getText().toString().isEmpty()){

                    int CountM = Integer.parseInt(txtMonto.getText().toString());

                    for (int i = tot; i <= CountM; i++){
                        if(CountM >= CountC){
                            resp = CountM - CountC;
                            countRes += 1;
                            CountM = resp;
                            tot = resp;
                        }

                    }

                    txtRes.setText(countRes + "/" + resp);


                    countRes = 0;
                    resp = 0;


                }else if(txtRes.getText().toString().contains("/")) {

                    String[] separated = txtRes.getText().toString().split("/");
                    int postion0 = Integer.parseInt(separated[0]);
                    int position1 = Integer.parseInt(separated[1]);


                    int Resultado = CountC * postion0 + position1;

                    txtMonto.setText(String.valueOf(Resultado));
                }
            }
        });


        TabHost pestana = (TabHost)findViewById(R.id.parcial);
        pestana.setup();

        //pestana 1
        TabHost.TabSpec spec = pestana.newTabSpec ("Universal");
        spec.setContent(R.id.Universal);
        spec.setIndicator("Universal");
        pestana.addTab (spec);

        //pestana 2
        spec = pestana.newTabSpec("Area");

        spec.setContent(R.id.Area);
        spec.setIndicator("Area");
        pestana.addTab (spec);

        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,datos);
        AreaActual=(Spinner) findViewById(R.id.AreaActual);
        AreaActual.setAdapter(adaptador);

        AreaCambio=(Spinner) findViewById(R.id.AreaCambio);
        SharedPreferences preferences=getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        String tmpAreaActual= preferences.getString("AreaActual","");
        String tmpAreaCambio= preferences.getString("AreaCambio","");

        if (!tmpAreaActual.equals("")){
            int indice=adaptador.getPosition(tmpAreaActual);
            AreaActual.setSelection(indice);
        }
        if (!tmpAreaCambio.equals("")){
            int indice=adaptador.getPosition(tmpAreaCambio);
            AreaCambio.setSelection(indice);

        }
    }


    public void clickConvertir(View v){
        AreaActual=(Spinner) findViewById(R.id.AreaActual);
        AreaCambio=(Spinner) findViewById(R.id.AreaCambio);
        editTvalorCambio=(EditText)findViewById(R.id.editTvalorCambio);
        textVresultado=(TextView) findViewById(R.id.textVresultado);

        String actualmoneda = AreaActual.getSelectedItem().toString();
        String cambiomonda = AreaCambio.getSelectedItem().toString();

        double valorCambio = Double.parseDouble(editTvalorCambio.getText().toString());
        double resultado = procesarconversor(actualmoneda,cambiomonda,valorCambio);

        if(resultado>0){
            textVresultado.setText(String.format("Por %5.2f %s, usted recibira %5.2f %s",valorCambio,actualmoneda,resultado,cambiomonda));
            editTvalorCambio.setText("");

            SharedPreferences preferencias = getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor=preferencias.edit();

            editor.putString("actualmoneda",actualmoneda);
            editor.putString("cambiomonda",cambiomonda);
            editor.commit();
        }
        else {
            textVresultado.setText(String.format("Resultado de Cambio"));
            Toast.makeText(MainActivity.this,"No es posible la converción", Toast.LENGTH_SHORT).show();

        }
    }

    private double procesarconversor(String actualmoneda,String cambiomonda, double valorCambio) {

           double resultadoconver = 0;
           switch (actualmoneda) {
               case "PIE CUADRADO":

                   if (cambiomonda.equals("VARA CUADRADA")) {
                       resultadoconver = valorCambio * 0.13223088;
                   }
                   if (cambiomonda.equals("YARDA CUADRADA")) {
                       resultadoconver = valorCambio / 9;
                   }
                   if (cambiomonda.equals("METRO CUADRADO")) {
                       resultadoconver = valorCambio / 10.764;
                   }
                   if (cambiomonda.equals("TAREA")) {
                       resultadoconver = valorCambio * 0.00014775;
                   }
                   if (cambiomonda.equals("MANZANA")) {
                       resultadoconver = valorCambio * 0.13223088;
                   }
                   if (cambiomonda.equals("HECTAREA")) {
                       resultadoconver = valorCambio / 107639;
                   }

                   break;

               case "VARA CUADRADA":

                   if (cambiomonda.equals("PIE CUADRADO")) {
                       resultadoconver = valorCambio * 7.562530;
                   }
                   if (cambiomonda.equals("YARDA CUADRADA")) {
                       resultadoconver = valorCambio * 0.539271;
                   }
                   if (cambiomonda.equals("METRO CUADRADO")) {
                       resultadoconver = valorCambio * 0.70258205;
                   }
                   if (cambiomonda.equals("TAREA")) {
                       resultadoconver = valorCambio * 0.001117;
                   }
                   if (cambiomonda.equals("MANZANA")) {
                       resultadoconver = valorCambio * 0.998422;
                   }
                   if (cambiomonda.equals("HECTAREA")) {
                       resultadoconver = valorCambio * 10002.805;
                   }

                   break;

               case "YARDA CUADRADA":

                   if (cambiomonda.equals("PIE CUADRADO")) {
                       resultadoconver = valorCambio / 9;
                   }
                   if (cambiomonda.equals("VARA CUADRADA")) {
                       resultadoconver = valorCambio * 0.8357;
                   }
                   if (cambiomonda.equals("METRO CUADRADO")) {
                       resultadoconver = valorCambio / 1.196;
                   }
                   if (cambiomonda.equals("TAREA")) {
                       resultadoconver = valorCambio * 438.94428;
                   }
                   if (cambiomonda.equals("MANZANA")) {
                       resultadoconver = valorCambio * 4878.29403;
                   }
                   if (cambiomonda.equals("HECTAREA")) {
                       resultadoconver = valorCambio * 11960;
                   }

                   break;

               case "METRO CUADRADO":

                   if (cambiomonda.equals("PIE CUADRADO")) {
                       resultadoconver = valorCambio * 10.764;
                   }
                   if (cambiomonda.equals("VARA CUADRADA")) {
                       resultadoconver = valorCambio * 69.8896;
                   }
                   if (cambiomonda.equals("YARDA CUADRADA")) {
                       resultadoconver = valorCambio * 1.196;
                   }
                   if (cambiomonda.equals("TAREA")) {
                       resultadoconver = valorCambio / 0.1590179054;
                   }
                   if (cambiomonda.equals("MANZANA")) {
                       resultadoconver = valorCambio * 6988.96;
                   }
                   if (cambiomonda.equals("HECTAREA")) {
                       resultadoconver = valorCambio / 10000;
                   }

                   break;

               case "TAREA":

                   if (cambiomonda.equals("PIE CUADRADO")) {
                       resultadoconver = valorCambio * 6768.34687003;
                   }
                   if (cambiomonda.equals("VARA CUADRADA")) {
                       resultadoconver = valorCambio * 894.98443634;
                   }
                   if (cambiomonda.equals("YARDA CUADRADA")) {
                       resultadoconver = valorCambio * 752.03854111;
                   }
                   if (cambiomonda.equals("METRO CUADRADO")) {
                       resultadoconver = valorCambio * 628.8;
                   }
                   if (cambiomonda.equals("MANZANA")) {
                       resultadoconver = valorCambio * 0.08926746;
                   }
                   if (cambiomonda.equals("HECTAREA")) {
                       resultadoconver = valorCambio * 0.06288;
                   }

                   break;

               case "MANZANA":

                   if (cambiomonda.equals("PIE CUADRADO")) {
                       resultadoconver = valorCambio * 75820.9849753;
                   }
                   if (cambiomonda.equals("VARA CUADRADA")) {
                       resultadoconver = valorCambio * 10025.87526966;
                   }
                   if (cambiomonda.equals("YARDA CUADRADA")) {
                       resultadoconver = valorCambio * 8424.55388614;
                   }
                   if (cambiomonda.equals("METRO CUADRADO")) {
                       resultadoconver = valorCambio * 6.972;
                   }
                   if (cambiomonda.equals("TAREA")) {
                       resultadoconver = valorCambio * 15.90331;
                   }
                   if (cambiomonda.equals("HECTAREA")) {
                       resultadoconver = valorCambio * 0.698896;
                   }

                   break;

               case "HECTAREA":

                   if (cambiomonda.equals("PIE CUADRADO")) {
                       resultadoconver = valorCambio * 107639;
                   }
                   if (cambiomonda.equals("VARA CUADRADA")) {
                       resultadoconver = valorCambio / 0.70258205;
                   }
                   if (cambiomonda.equals("YARDA CUADRADA")) {
                       resultadoconver = valorCambio * 11960;
                   }
                   if (cambiomonda.equals("METRO CUADRADO")) {
                       resultadoconver = valorCambio * 10000;
                   }
                   if (cambiomonda.equals("TAREA")) {
                       resultadoconver = valorCambio * 0;
                   }
                   if (cambiomonda.equals("MANZANA")) {
                       resultadoconver = valorCambio / 1.4184;
                   }

                   break;

           }
           return resultadoconver;



       }
    }


