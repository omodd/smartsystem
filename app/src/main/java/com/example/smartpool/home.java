package com.example.smartpool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class home extends AppCompatActivity {
    private static final String TAG = "TAG";

    FirebaseDatabase fbDb;
    String sPool, sPju, sHidro, sSHeat, nama_User;
    LinearLayout menuSP, menuSPJU, menuSHidro, menuSHeat;
    TextView nama_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        
        menuSP = findViewById(R.id.smartpool_menu);
        menuSPJU = findViewById(R.id.smartpju_menu);
        menuSHidro = findViewById(R.id.smarthidroponik_menu);
        menuSHeat = findViewById(R.id.smartholarheat_menu);
        nama_user = findViewById(R.id.nama_user);

        fbDb = FirebaseDatabase.getInstance();

        //Get the bundle
        Bundle bundle = getIntent().getExtras();

        //Extract the data…
        String stuff = bundle.getString("userid");

        Log.d(TAG, "onCreate: Data ini yang di dapat => "+stuff);

        DatabaseReference statsPool = fbDb.getReference("Smart_waterHeater/"+stuff+"/fitur/smartpool/status");
        DatabaseReference statsPju = fbDb.getReference("Smart_waterHeater/"+stuff+"/fitur/smartpju/status");
        DatabaseReference statsHidro = fbDb.getReference("Smart_waterHeater/"+stuff+"/fitur/smarthidroponik/status");
        DatabaseReference statsSHeat = fbDb.getReference("Smart_waterHeater/"+stuff+"/fitur/smartsolarheat/status");
        DatabaseReference namaUser = fbDb.getReference("Smart_waterHeater/"+stuff+"/nama");

        statsPool.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                sPool = snapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Whoops..Something wrong: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("MyList Activity", error.getDetails() + " " + error.getMessage());
            }
        });

        statsPju.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                sPju = snapshot.getValue(String.class);
                Log.d(TAG, "onDataChange: status PJU => "+sPju);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Whoops..Something wrong: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("MyList Activity", error.getDetails() + " " + error.getMessage());
            }
        });

        statsHidro.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                sHidro = snapshot.getValue(String.class);
                Log.d(TAG, "onDataChange: status Hidro => "+sHidro);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Whoops..Something wrong: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("MyList Activity", error.getDetails() + " " + error.getMessage());
            }
        });

        statsSHeat.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                sSHeat = snapshot.getValue(String.class);
                Log.d(TAG, "onDataChange: status sheat => "+sSHeat);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Whoops..Something wrong: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("MyList Activity", error.getDetails() + " " + error.getMessage());
            }
        });

        namaUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                nama_User = snapshot.getValue(String.class);
                Log.d(TAG, "onCreate: nama user nya ini => "+nama_User);

                //nama user
                nama_user.setText(nama_User);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Whoops..Something wrong: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("MyList Activity", error.getDetails() + " " + error.getMessage());
            }
        });
        
        menuSP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sPool=="on"){
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                }
                else{
                    Toast.makeText(getApplicationContext(), "Fitur belum diaktifkan, silakan hubungi develover", Toast.LENGTH_SHORT).show();           
                }
            }
        });
        
        menuSPJU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sPju=="on"){
                    startActivity(new Intent(getApplicationContext(),pju.class));
                }
                else{
                    Toast.makeText(getApplicationContext(), "Fitur belum diaktifkan, silakan hubungi debeloper", Toast.LENGTH_SHORT).show();
                }
            }
        });
        
        menuSHidro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sHidro=="on"){
                    startActivity(new Intent(getApplicationContext(),hidroponik.class));
                }
                else{
                    Toast.makeText(getApplicationContext(), "Fitur belum diaktifkan, silakan hubungi developer", Toast.LENGTH_SHORT).show();
                }
            }
        });
        
        menuSHeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sSHeat.equals("on")){
                    //startActivity(new Intent(getApplicationContext(),water_heater.class));

                    Intent i = new Intent(getApplicationContext(), water_heater.class);
                    String getrec = nama_User;

                    //Create the bundle
                    Bundle bundle1 = new Bundle();

                    //Add your data to bundle
                    bundle1.putString("userid", getrec);

                    //Add the bundle to the intent
                    i.putExtras(bundle1);

                    //Fire that second activity
                    startActivity(i);
                }
                else {
                    Toast.makeText(getApplicationContext(), "Fitur belum diaktifkan, silakan hubungi developer", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}