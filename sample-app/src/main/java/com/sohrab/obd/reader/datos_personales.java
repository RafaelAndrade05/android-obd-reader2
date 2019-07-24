package com.sohrab.obd.reader;

import android.app.ProgressDialog;
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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class datos_personales extends AppCompatActivity {

    EditText edCorreo, edContra;
    ImageButton registrar;
    private static final String TAG = "datos_personales";


    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_personales);

        edCorreo = (EditText) findViewById(R.id.txtCorreo);
        edContra = (EditText) findViewById(R.id.txtContra);
        registrar = (ImageButton) findViewById(R.id.imageRegistrar);


        mAuth = FirebaseAuth.getInstance();


        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email, password;
                email = edCorreo.getText().toString().trim();
                password = edContra.getText().toString().trim();

                Pattern patternEmail = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

                Matcher matherEmail = patternEmail.matcher(email);

                if (TextUtils.isEmpty(email)){
                    edCorreo.setError(getText(R.string.campo_vacio));
                    return;
                }

                if (!matherEmail.find()){
                    edCorreo.setError(getText(R.string.correo_no_valido));
                    return;
                }

                if (TextUtils.isEmpty(password)){
                    edContra.setError(getText(R.string.campo_vacio));
                    return;
                }

                if (password.length()<= 5 ){
                    edContra.setError(getText(R.string.contra_pequena));
                    return;
                }




                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(datos_personales.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Log.i(TAG,"id == "+ user.getUid());
                                    Log.i(TAG,"email == "+ user.getEmail());
                                    updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(datos_personales.this, R.string.error_reigstro,
                                            Toast.LENGTH_SHORT).show();
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(datos_personales.this, R.string.correo_no_valido,
                                            Toast.LENGTH_SHORT).show();
                                    updateUI(null);
                                }

                                // ...
                            }
                        });

            }
        });

    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            Intent intent = new Intent(getApplicationContext(),datos_adicionales.class);
            intent.putExtra("uid",user.getUid());
            intent.putExtra("email",user.getEmail());
            startActivity(intent);
            Toast.makeText(datos_personales.this, R.string.registrado,
                    Toast.LENGTH_SHORT).show();
            Log.d(TAG, "createUserWithEmail:success");
            finish();
        }
    }

    public void cancelar (View v){
        startActivity(new Intent(datos_personales.this, login.class));
    }

}
