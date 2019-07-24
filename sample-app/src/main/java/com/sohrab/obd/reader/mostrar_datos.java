package com.sohrab.obd.reader;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sohrab.obd.reader.Objetos.Car;
import com.sohrab.obd.reader.Objetos.Usuario;

public class mostrar_datos extends AppCompatActivity {

    private static final String TAG = "mostrar_datos" ;
    private TextView txtNombre,txtEmail,txtModelo,txtPlaca,txtFecha,txtTelefono;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference referenceUser = database.getReference(Usuario.PATH_USER);
    DatabaseReference referenceCar = database.getReference(Usuario.PATH_USER);

    private Usuario usuarioObjeto;
    private Car objetoCarro;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_datos);

        txtNombre = (TextView)findViewById(R.id.txtNombre);
        txtEmail = (TextView)findViewById(R.id.txtEmail);
        txtModelo = (TextView)findViewById(R.id.txtModelo);
        txtPlaca = (TextView)findViewById(R.id.txtPlaca);
        txtFecha = (TextView)findViewById(R.id.txtFecha);
        txtTelefono = (TextView)findViewById(R.id.txtTelefono);

        user = mAuth.getCurrentUser();

        if(user == null){
            finish();
        }


        mostrarDatosUsuario(user);


    }

    private void mostrarDatosUsuario(final FirebaseUser user) {
        referenceUser.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Log.i(TAG,dataSnapshot.toString());
                    usuarioObjeto = dataSnapshot.getValue(Usuario.class);

                    objetoCarro = dataSnapshot.child(Car.PATH_CAR).getValue(Car.class);

                    Log.i(TAG,"user ===" + usuarioObjeto.toString());
                    Log.i(TAG,"carro ===" + objetoCarro.toString());

                    txtNombre.setText(usuarioObjeto.getName());
                    txtEmail.setText(usuarioObjeto.getEmail());
                    txtTelefono.setText(usuarioObjeto.getPhoneNumber());

                    txtModelo.setText(objetoCarro.getModel());
                    txtPlaca.setText(objetoCarro.getPlaque());
                    txtFecha.setText(objetoCarro.getDate());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
