package com.example.programacion_lll;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RegistroActivity extends AppCompatActivity {

    private EditText txtNombre;
    private EditText txtCorreo;
    private EditText txtContraseña;

    private Button registrar;
    private Button iniciaresion;

    private String nombre = "";
    private String correo = "";
    private String contraseña = "";

    FirebaseAuth mAuth;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        txtNombre = (EditText)findViewById(R.id.txtNombre);
        txtCorreo = (EditText)findViewById(R.id.txtCorreo);
        txtContraseña = (EditText)findViewById(R.id.txtContracea);

        registrar = (Button) findViewById(R.id.btnRegistro);
        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nombre = txtNombre.getText().toString();
                correo = txtCorreo.getText().toString();
                contraseña = txtContraseña.getText().toString();

                if (!nombre.isEmpty() && !correo.isEmpty() && !contraseña.isEmpty()){
                    if (contraseña.length() >= 6){
                        registrUsuario();
                    }
                    else {
                        Toast.makeText(RegistroActivity.this, " La contraseña no debe tener menos de 6 caracteres", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(RegistroActivity.this, "Debe completar los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });

        iniciaresion = (Button) findViewById(R.id.btnIniciarSesion);
        iniciaresion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegistroActivity.this, IniciarSesion.class));
            }
        });
    }

    private void registrUsuario() {
        mAuth.createUserWithEmailAndPassword(correo, contraseña).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                    Map<String, Object> map = new HashMap<>();
                    map.put("nombre", nombre);
                    map.put("correo", correo);
                    map.put("contraseña", contraseña);

                    String id = mAuth.getCurrentUser().getUid();

                    mDatabase.child("Users").child(id).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task2) {
                            if (task2.isSuccessful()){
                                startActivity(new Intent(RegistroActivity.this, MainActivity.class));
                                finish();
                            }
                            else {
                                Toast.makeText(RegistroActivity.this, "Los registros no se crearon correctamente", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else {
                    Toast.makeText(RegistroActivity.this, "El usuario no se pudo registrar", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}

