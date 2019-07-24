package com.sohrab.obd.reader;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sohrab.obd.reader.Objetos.Sensor;
import com.sohrab.obd.reader.Objetos.Usuario;

public class navigation extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseAuth mAuth;

    public final static int MSMPERMISSIONCODE = 225;
    private boolean mMSMPermissionsGranted = false;

    DatabaseReference referenceUser = FirebaseDatabase.getInstance().getReference(Usuario.PATH_USER);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();

        final FirebaseUser user = mAuth.getCurrentUser();

        if(user == null){
            finish();
        }

/*
        referenceUser.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    final Usuario objUser = dataSnapshot.getValue(Usuario.class);
                    Log.i("navigation",objUser.toString());

                    DatabaseReference referenceSensores = FirebaseDatabase.getInstance().
                            getReference(objUser.getMacArduino()).child(Sensor.PATH_SENSORES);

                    referenceSensores.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                Sensor objSensor = dataSnapshot.getValue(Sensor.class);
                                Log.i("navigation",objSensor.toString());

                                if(objSensor.getImpact() == 1){
                                    Log.i("navigation","Mensaje Enviado");
                                    enviarMensaje(objUser.getPhoneNumber(),objUser.getName());
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        checkSMSStatePermission();

       */

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.DATOS_PERSONALES) {
            startActivity(new Intent(navigation.this,mostrar_datos.class));
        } else if (id == R.id.DIAGNOSTICO) {
            startActivity(new Intent(navigation.this, SampleActivity.class));

        } else if (id == R.id.CONFIGURACION) {
            startActivity(new Intent(navigation.this, configuracion.class));

        } else if (id == R.id.AVISO) {
            startActivity(new Intent(navigation.this, aviso_de_privacidad.class));

        } else if (id == R.id.nav_cerrar) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(navigation.this, login.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }






    /////////////////////////////////////////////////////Permisos Mensajes//////////////////////////////////////
    private void enviarMensaje(String number,String name) {
        //verificamos los permisos de telefono
        try{
            if(mMSMPermissionsGranted) {
                String mensajeCompleto = "Precaucion!!! se activo el sensor de impacto del usuario " + name ;
                SmsManager sms = SmsManager.getDefault();
                sms.sendTextMessage(number, null, mensajeCompleto, null, null);
                Toast.makeText(getApplicationContext(), "Mensaje Enviado.", Toast.LENGTH_LONG).show();
                Log.i("MENSAJE","Numero enviado a => " +  number + " Mensaje: => " + mensajeCompleto);
            }else {
                checkSMSStatePermission();
            }
        }

        catch (Exception e){
            Toast.makeText(getApplicationContext(), "Mensaje no enviado, datos incorrectos." + e.getMessage().toString(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void checkSMSStatePermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(
                this, android.Manifest.permission.SEND_SMS);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            Log.i("Mensaje", "No se tiene permiso para enviar SMS.");
            mMSMPermissionsGranted = false;
            ActivityCompat.requestPermissions(navigation.this, new String[]{android.Manifest.permission.SEND_SMS}, MSMPERMISSIONCODE);
        } else {
            mMSMPermissionsGranted = true;
            Log.i("Mensaje", "Se tiene permiso para enviar SMS!");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mMSMPermissionsGranted = false;

        switch (requestCode) {
            case MSMPERMISSIONCODE:
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        mMSMPermissionsGranted = false;
                        return;
                    }
                }
                Log.i("Mensajes", "permisos aceptados se cambia a true");
                mMSMPermissionsGranted = true;
                break;
        }
    }
}
