package com.example.smartpool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class water_heater extends AppCompatActivity {
    private static final String TAG = "TAG";
    TextView suhu, volume_air, am_switch;
    Switch onofSwitch;
    CardView elemen;
    LinearLayout layoutHeater;
    Object stateSwitch, stateHeater;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_heater);

        suhu = findViewById(R.id.suhu_air);
        volume_air = findViewById(R.id.volume);
        onofSwitch = findViewById(R.id.on_off_switch);
        am_switch = findViewById(R.id.am_switch);
        elemen = findViewById(R.id.elemen);
        layoutHeater = findViewById(R.id.layoutHeater);

        //Get the bundle
        Bundle bundle1 = getIntent().getExtras();

        //Extract the data…
        String stuff1 = bundle1.getString("userid");
        Log.d(TAG, "onCreate: Stuff=> "+stuff1);

        DatabaseReference refUser = FirebaseDatabase.getInstance().getReference().child("Smart_waterHeater").orderByChild();
        refUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    uid = ds.getKey();
                    Log.d(TAG, "onDataChange: UID= "+uid);

                    FirebaseDatabase fb = FirebaseDatabase.getInstance();

                    Log.e(TAG, "onCreate: UID"+uid);
                    //nanti user name nya di ganti ya biar otomatis
                    DatabaseReference refSuhu = fb.getReference("Smart_waterHeater/"+uid+"/fitur/smartsolarheat/suhu_air");
                    DatabaseReference refVolume = fb.getReference("Smart_waterHeater/"+uid+"/fitur/smartsolarheat/volume_air");
                    DatabaseReference refSwitch = fb.getReference("Smart_waterHeater/"+uid+"/fitur/smartsolarheat/heater/mode");
                    DatabaseReference refHeater = fb.getReference("Smart_waterHeater/"+uid+"/fitur/smartsolarheat/heater/status");

                    refSuhu.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Integer x = snapshot.getValue(Integer.class);
                            Log.d(TAG, "onDataChange: suhu => "+x+" derajat");
                            suhu.setText(x.toString());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(getApplicationContext(), "Whoops..Something wrong: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.e("MyList Activity", error.getDetails() + " " + error.getMessage());
                        }
                    });

                    refVolume.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Integer y = snapshot.getValue(Integer.class);
                            Log.d(TAG, "onDataChange: Volume => "+y+"%");
                            volume_air.setText(y.toString());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(getApplicationContext(), "Whoops..Something wrong: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.e("MyList Activity", error.getDetails() + " " + error.getMessage());

                        }
                    });

                    refSwitch.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            stateSwitch = snapshot.getValue(String.class);
                            Log.d(TAG, "onDataChange: Switch= "+stateSwitch);
                            if (stateSwitch.equals("manual")){
                                onofSwitch.setChecked(false);
                                Log.d(TAG, "onDataChange: Mode elemen manual");
                            }else {
                                onofSwitch.setChecked(true);
                                Log.d(TAG, "onDataChange: Mode elemen auto");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(getApplicationContext(), "Whoops..Something wrong: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.e("MyList Activity", error.getDetails() + " " + error.getMessage());
                        }
                    });

                    onofSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            Log.v("Switch State=", ""+isChecked);
                            if (isChecked){
                                am_switch.setText("AUTO");
                                refSwitch.setValue("auto");

                                //disini masukan logika untuk membandingkan waktu ketika switch elemen auto HH:mm:ss
                                DateFormat df = new SimpleDateFormat("HH:mm");
                                String date = df.format(Calendar.getInstance().getTime());
                                Log.d(TAG, "onCheckedChanged: Time => "+date);

                                String dtStart = "15:00";
                                String dtEnd = "06:00";
                                SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
                                SimpleDateFormat dateFormat2 = new SimpleDateFormat("HH:mm");
                                try {
                                    Date date1 = dateFormat.parse(dtStart);
                                    String out = dateFormat2.format(date1);
                                    Log.e("Time", out);

                                    Date date2 = dateFormat.parse(dtEnd);
                                    String out1 =dateFormat2.format(date2);
                                    Log.e("onCheckedChanged: Out1",out1);

                                    Date date0 = dateFormat.parse(date);

                                    if (date0.after(date1)&&date0.before(date2)){
                                        refHeater.setValue("on");
                                        Log.e(TAG, "onCheckedChanged: Heater ON");
                                    }else {
                                        refHeater.setValue("off");
                                        Log.e(TAG, "onCheckedChanged: Heater OFF");
                                    }
                                } catch (ParseException e) {
                                }

                                layoutHeater.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Toast.makeText(getApplicationContext(), "Heater dalam mode Auto", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }else{
                                am_switch.setText("MANUAL");
                                refSwitch.setValue("manual");

                                layoutHeater.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if (stateHeater.equals("off")){
                                            layoutHeater.setBackground(getDrawable(R.drawable.rounder_corner_lightyellow));
                                            refHeater.setValue("on");
                                        }
                                        else {
                                            layoutHeater.setBackground(getDrawable(R.drawable.rounderd_grey_full));
                                            refHeater.setValue("off");
                                        }
                                    }
                                });
                            }
                        }
                    });

                    refHeater.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            stateHeater = snapshot.getValue(String.class);
                            Log.d(TAG, "onDataChange: Heater= "+stateHeater);
                            if (stateHeater.equals("on")){
                                layoutHeater.setBackground(getDrawable(R.drawable.rounder_corner_lightyellow));
                                Log.d(TAG, "onDataChange: Elemen nyala");
                            }else {
                                layoutHeater.setBackground(getDrawable(R.drawable.rounderd_grey_full));
                                Log.d(TAG, "onDataChange: Elemen mati");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(getApplicationContext(), "Whoops..Something wrong: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.e("MyList Activity", error.getDetails() + " " + error.getMessage());
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}