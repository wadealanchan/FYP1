package com.example.alan.fyp;


import android.app.Dialog;
import android.content.DialogInterface;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by wadealanchan on 4/9/2017.
 */

public class AuthClass extends BaseActivity implements CustomDialogListener,View.OnClickListener{


    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private EditText mEmail;
    private EditText mPw;
    String Email;
    String Password;
    CustomDialogFragment C_Dialog = new CustomDialogFragment();
    public final String TAG ="AuthClass";
    Button signout;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_login);

        mEmail= (EditText) findViewById(R.id.field_email);
        mPw=  (EditText) findViewById(R.id.field_pw);

        findViewById(R.id.btn_createac).setOnClickListener(this);
        findViewById(R.id.btn_signin).setOnClickListener(this);
        findViewById(R.id.btn_signout).setOnClickListener(this);
        //Email= C_Dialog.getArguments().getString("email");


    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void createAccount(String email, String password) {
        Log.d("Create Account:", "createAccount:" + email);
        if (!validateForm(2)) {
            return;
        }

        showProgressDialog();
        Log.d(TAG,"get it here?");
        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            //updateUI(user);
                        } else if(!task.isSuccessful()){
                            // If sign in fails, display a message to the user.
                            FirebaseAuthException e = (FirebaseAuthException )task.getException();
                            Log.w("", "createUserWithEmail:failure", task.getException());
                            Log.e("LoginActivity", "Failed Registration", e);
                            Toast.makeText(AuthClass.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // [START_EXCLUDE]
                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
        // [END create_user_with_email]
    }




    private void signIn(String email, String password) {
        Log.d("", "signIn:" + email);
        if (!validateForm(1)) {
            return;
        }

        showProgressDialog();

        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            firebase();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("", "signInWithEmail:failure", task.getException());
                            Toast.makeText(AuthClass.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // [START_EXCLUDE]
                        if (!task.isSuccessful()) {
                            //mStatusTextView.setText(R.string.auth_failed);
                        }
                        //hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
        // [END sign_in_with_email]
    }


    private void firebase(){

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        myRef.setValue("Hello, World!");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }





    private boolean validateForm(int Case) {
      boolean valid = true;
        switch (Case) {
            case 1:
                String email = mEmail.getText().toString();
                if (TextUtils.isEmpty(email)) {
                    mEmail.setError("Required.");
                    valid = false;
                } else {
                    mEmail.setError(null);
                }

                String password = mPw.getText().toString();
                if (TextUtils.isEmpty(password)) {
                    mPw.setError("Required.");
                    valid = false;
                } else {
                    mPw.setError(null);
                }

                return valid;


            case 2:
                if (TextUtils.isEmpty(this.Email)) {
                    Toast.makeText(getApplicationContext(),"Email Required",Toast.LENGTH_LONG).show();
                    valid = false;
                }

                if (TextUtils.isEmpty(this.Password)) {
                    Toast.makeText(getApplicationContext(),"Password Required",Toast.LENGTH_LONG).show();
                    valid = false;
                }
                return valid;

        }
        return valid;


    }





    private void updateUI(FirebaseUser currentUser) {
        hideProgressDialog();
        if (currentUser != null) {
            Toast.makeText(AuthClass.this, currentUser.getEmail(),
                    Toast.LENGTH_SHORT).show();

            findViewById(R.id.btn_createac).setVisibility(View.GONE);
            findViewById(R.id.btn_signin).setVisibility(View.GONE);
            findViewById(R.id.btn_signout).setVisibility(View.VISIBLE);
        }
        else
        {
            Toast.makeText(AuthClass.this, R.string.signedout,
                    Toast.LENGTH_SHORT).show();
            findViewById(R.id.btn_createac).setVisibility(View.VISIBLE);
            findViewById(R.id.btn_signin).setVisibility(View.VISIBLE);
            findViewById(R.id.btn_signout).setVisibility(View.GONE);
        }


    }

    @Override
    public void onClick(View v) {
        int i = v.getId();

        switch (i) {
            case R.id.btn_createac:
                showCustomDialog();
                 break;
            case R.id.btn_signin:
                Log.e("Did i click here?", "signin");
             signIn(mEmail.getText().toString(), mPw.getText().toString());
                break;

            case R.id.btn_signout:
                Log.e("Did i click here?", "signout");
                signOut();
                break;
        }

    }

   private void signOut() {
       mAuth.signOut();
       updateUI(null);
   }


    private void showCustomDialog() {
        CustomDialogFragment dialog = new CustomDialogFragment();
        dialog.show(getSupportFragmentManager(), "CustomDialogFragment");

    }
    @Override
    public void onAction(Object object) {

    }

    @Override
    public void onDialogPositive(String Email, String Pw) {
        Toast.makeText(this,"Custom Dialog "+  getString(R.string.createac)+" "+Email+ " " +Pw,Toast.LENGTH_SHORT).show();
        this.Email= Email;
        this.Password=Pw;
        Log.d(TAG+"Email:",Email);
        Log.d(TAG+"Password:",Password);
        createAccount(Email,Pw);

    }

    @Override
    public void onDialogNegative(Object object) {
        //Toast.makeText(this,"Custom Dialog "+getString(R.string.cancel),Toast.LENGTH_SHORT).show();
    }



}


