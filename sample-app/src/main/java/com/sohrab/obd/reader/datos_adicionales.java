package com.sohrab.obd.reader;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sohrab.obd.reader.Objetos.Car;
import com.sohrab.obd.reader.Objetos.Usuario;

public class datos_adicionales extends AppCompatActivity {

    private static final String TAG = "datos_adicionales" ;
    private EditText etNombre, etModelo, etTelefono, etPlaca, etFecha, etCodigoArduino;
    private String uid;
    private String nombre,fecha,modelo,placa,telefono,email,codigo;
    private ProgressDialog progressDialog;


    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference referenceUser = database.getReference(Usuario.PATH_USER);
    DatabaseReference referenceCar = database.getReference(Usuario.PATH_USER);

    private Usuario usuarioObjeto;
    private Car objetoCarro;


    @Override public void onBackPressed(){
        moveTaskToBack(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_adicionales);

        etNombre = (EditText)findViewById(R.id.etNombre);
        etFecha = (EditText)findViewById(R.id.etFecha);
        etModelo = (EditText)findViewById(R.id.etModelo);
        etPlaca = (EditText)findViewById(R.id.etPlaca);
        etTelefono = (EditText)findViewById(R.id.etTelefono);
        etCodigoArduino = (EditText)findViewById(R.id.etCodigoArduino);

        progressDialog = new ProgressDialog(this);

        uid = getIntent().getStringExtra("uid");
        email = getIntent().getStringExtra("email");

        Log.i(TAG,"UserFIREBASEId = " + uid);
        Log.i(TAG,"email = " + email);

    }


    public void registrar (View v){
        nombre = etNombre.getText().toString().trim();
        fecha = etFecha.getText().toString().trim();
        modelo = etModelo.getText().toString().trim();
        placa = etPlaca.getText().toString().trim();
        telefono = etTelefono.getText().toString().trim();
        codigo = etCodigoArduino.getText().toString().trim();

        if (TextUtils.isEmpty(nombre)){
            etNombre.setError(getText(R.string.campo_vacio));
            return;
        }

        if (TextUtils.isEmpty(fecha)){
            etFecha.setError(getText(R.string.campo_vacio));
            return;
        }

        if (TextUtils.isEmpty(modelo)){
            etModelo.setError(getText(R.string.campo_vacio));
            return;
        }
        if (TextUtils.isEmpty(placa)){
            etPlaca.setError(getText(R.string.campo_vacio));
            return;
        }
        if (TextUtils.isEmpty(telefono)){
            etTelefono.setError(getText(R.string.campo_vacio));
            return;
        }

        progressDialog.setMessage(getText(R.string.realizando_registro));
        progressDialog.show();

        usuarioObjeto = new Usuario();
        objetoCarro = new Car();

        usuarioObjeto.setName(nombre);
        usuarioObjeto.setEmail(email);
        usuarioObjeto.setPhoneNumber(telefono);
        usuarioObjeto.setUid(uid);
        usuarioObjeto.setMacArduino(codigo);

        objetoCarro.setDate(fecha);
        objetoCarro.setModel(modelo);
        objetoCarro.setPlaque(placa);

        referenceUser.child(uid).setValue(usuarioObjeto).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                referenceCar.child(uid).child(Car.PATH_CAR).setValue(objetoCarro).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        startActivity(new Intent(datos_adicionales.this, navigation.class));
                        finish();
                    }
                });
            }
        });






    }
}
