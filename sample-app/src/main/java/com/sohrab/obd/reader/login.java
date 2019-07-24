package com.sohrab.obd.reader;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class login extends AppCompatActivity {

    private static final String TAG = " ";
    EditText edCorreo, edContra;
    Button iniciar,registrar;

    private FirebaseAuth mAuth;

    @Override public void onBackPressed(){
        moveTaskToBack(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edCorreo = (EditText) findViewById(R.id.txtCorreo);
        edContra = (EditText) findViewById(R.id.txtContra);
        iniciar = (Button) findViewById(R.id.btnIniciar);
        registrar = (Button) findViewById(R.id.btnregistrar);

        mAuth = FirebaseAuth.getInstance();

        iniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email, password;
                email = edCorreo.getText().toString().trim();
                password = edContra.getText().toString().trim();

                if (TextUtils.isEmpty(email)){
                    edCorreo.setError(getText(R.string.campo_vacio));
                    return;
                }

                if (TextUtils.isEmpty(password)){
                    edContra.setError(getText(R.string.campo_vacio));
                    return;
                }


                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(login.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            startActivity(new Intent(login.this, navigation.class));
                            Toast.makeText(login.this, R.string.bienvenido, Toast.LENGTH_SHORT).show();

                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());

                            Toast.makeText(login.this, R.string.datos_no_validos,
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });

            }
        });
    }


    public void registrar(View v) {
        startActivity(new Intent(login.this, datos_personales.class));
    }

    protected void onStart() {
        login.super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);

    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            startActivity(new Intent(login.this, navigation.class));
            finish();
        }
    }
}
