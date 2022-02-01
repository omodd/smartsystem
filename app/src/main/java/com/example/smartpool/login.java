package com.example.smartpool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class login extends AppCompatActivity {
    private static final String TAG = "TAG";
    EditText user_id;
    Button login_button;

    FirebaseDatabase fbDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        user_id = findViewById(R.id.user_id);
        login_button = findViewById(R.id.login_button);

        fbDb = FirebaseDatabase.getInstance();

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userid = user_id.getText().toString().trim();

                if (TextUtils.isEmpty(userid)){
                    user_id.setError("Masukan User ID");
                    return;
                }
                Log.d(TAG, "onClick: UserID => "+userid);

                DatabaseReference firebaseRef = fbDb.getReference("Smart_waterHeater");

                firebaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChild(userid)){
                            Log.d(TAG, "onDataChange: Ada anjir wkwkwkwk");
                            //startActivity(new Intent(getApplicationContext(),home.class));
                            Intent i = new Intent(getApplicationContext(), home.class);
                            String getrec = userid;

                            //Create the bundle
                            Bundle bundle = new Bundle();

                            //Add your data to bundle
                            bundle.putString("userid", getrec);

                            //Add the bundle to the intent
                            i.putExtras(bundle);

                            //Fire that second activity
                            startActivity(i);
                        }
                        else{
                            Log.d(TAG, "onDataChange: haduh anjir gimana sih ini");
                            Toast.makeText(getApplicationContext(), "User ID tidak terdaftar", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }
}