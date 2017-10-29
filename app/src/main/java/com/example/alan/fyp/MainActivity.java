package com.example.alan.fyp;

import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

public class MainActivity extends AppCompatActivity implements View.OnClickListener  {
    Button login_bt;
    Button google_login;
    public final String TAG ="MainClass:Database";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String token = FirebaseInstanceId.getInstance().getToken();
                Log.e("tokenFromAct", token);
            }
        }, 7000);

        findViewById(R.id.btn_login).setOnClickListener(this);
        findViewById(R.id.btn_gsignin).setOnClickListener(this);
        findViewById(R.id.btn_chat).setOnClickListener(this);


    }

    private static Handler mHandler = new Handler();


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_login) {
            Intent newAct = new Intent(MainActivity.this,AuthClass.class);
            startActivity(newAct);
        }else if(i == R.id.btn_gsignin)
        {
            Log.e("Did i click here?", "googlesigin");
            Intent newAct = new Intent(MainActivity.this,googleauth.class);
            startActivity(newAct);
        }
        else if(i == R.id.btn_chat)
        {
            Intent newAct = new Intent(MainActivity.this,chat_main.class);
            startActivity(newAct);
        }
//        else if(i == R.id.btn_profile)
//        {
//            Intent newAct = new Intent(MainActivity.this,Profile.class);
//            startActivity(newAct);
//        }
    }


    }
