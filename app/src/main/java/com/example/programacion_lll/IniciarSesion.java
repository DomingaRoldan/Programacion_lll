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

public class IniciarSesion extends AppCompatActivity {

    private EditText txtcorreo;
    private EditText txtContraseña;

    private Button btnIniciar;

    private String correo = "";
    private String contaseña = "";

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_sesion);

        mAuth = FirebaseAuth.getInstance();

        txtcorreo = (EditText)findViewById(R.id.txtCorreo1);
        txtContraseña = (EditText)findViewById(R.id.txtcontrasena);

        btnIniciar = (Button) findViewById(R.id.btnIniciar);
        btnIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                correo = txtcorreo.getText().toString();
                contaseña = txtContraseña.getText().toString();
                if (!correo.isEmpty() && !contaseña.isEmpty()){
                    iniciarSesion();
                }
                else {
                    Toast.makeText(IniciarSesion.this, "Complete los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void iniciarSesion() {
        mAuth.signInWithEmailAndPassword(correo, contaseña).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    startActivity(new Intent(IniciarSesion.this, MainActivity.class));
                    finish();
                }
                else {
                    Toast.makeText(IniciarSesion.this, "No se pudo iniciar sesion! verifique la informacion de los campos ", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
