package com.example.smartpool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.github.angads25.toggle.interfaces.OnToggledListener;
import com.github.angads25.toggle.model.ToggleableView;
import com.github.angads25.toggle.widget.LabeledSwitch;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "TAG";

    LinearLayout layoutLampu, layoutPompa;
    ImageView lampu, pompa;

    Object stateLampu, statePompa;

    //    LabeledSwitch labeledSwitch;
//    boolean state;

    //FirebaseDatabase fb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layoutLampu= findViewById(R.id.layout_lampu);
        layoutPompa= findViewById(R.id.layout_pompa);
        lampu = findViewById(R.id.lampu);
        pompa = findViewById(R.id.pompa);

        FirebaseDatabase fb = FirebaseDatabase.getInstance();
        DatabaseReference refLampu = fb.getReference("lampu");
        DatabaseReference refPompa = fb.getReference("pompa");

        refLampu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                stateLampu = snapshot.getValue(String.class);
                Log.d(TAG, "onDataChange: Lampu= "+stateLampu);
                if (stateLampu.equals("ON")){
                    layoutLampu.setBackground(getDrawable(R.color.light_yellow));
                    lampu.setImageDrawable(getDrawable(R.drawable.on));
                    Log.d(TAG, "onDataChange: lampu nyala");
                }else {
                    layoutLampu.setBackground(getDrawable(R.color.black));
                    lampu.setImageDrawable(getDrawable(R.drawable.off));
                    Log.d(TAG, "onDataChange: lampu mati");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Whoops..Something wrong: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("MyList Activity", error.getDetails() + " " + error.getMessage());
            }
        });

        refPompa.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                statePompa = snapshot.getValue(String.class);
                Log.d(TAG, "onDataChange: Pompa= "+statePompa);
                if (statePompa.equals("ON")){
                    layoutPompa.setBackground(getDrawable(R.color.light_yellow));
                    pompa.setImageDrawable(getDrawable(R.drawable.pompa_off));
                    Log.d(TAG, "onDataChange: pompa nyala");
                }else {
                    layoutPompa.setBackground(getDrawable(R.color.black));
                    pompa.setImageDrawable(getDrawable(R.drawable.pompa_on));
                    Log.d(TAG, "onDataChange: pompa mati");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Whoops..Something wrong: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("MyList Activity", error.getDetails() + " " + error.getMessage());
            }
        });

        layoutLampu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (stateLampu.equals("OFF")){
                    layoutLampu.setBackground(getDrawable(R.color.light_yellow));
                    lampu.setImageDrawable(getDrawable(R.drawable.on));
                    refLampu.setValue("ON");
                }
                else {
                    layoutLampu.setBackground(getDrawable(R.color.black));
                    lampu.setImageDrawable(getDrawable(R.drawable.off));
                    refLampu.setValue("OFF");
                }
            }
        });

        layoutPompa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (statePompa.equals("OFF")){
                    layoutPompa.setBackground(getDrawable(R.color.light_yellow));
                    pompa.setImageDrawable(getDrawable(R.drawable.pompa_on));
                    refPompa.setValue("ON");
                }
                else {
                    layoutPompa.setBackground(getDrawable(R.color.black));
                    pompa.setImageDrawable(getDrawable(R.drawable.pompa_off));
                    refPompa.setValue("OFF");
                }
            }
        });
    }

    public void add_fitur(View view) {
        Toast.makeText(getApplicationContext(), "Silahkan hubungi developer untuk menambahkan fitur", Toast.LENGTH_SHORT).show();
    }
}