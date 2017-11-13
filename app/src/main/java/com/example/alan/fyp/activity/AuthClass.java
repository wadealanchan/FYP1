package com.example.alan.fyp.activity;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.alan.fyp.CustomDialogFragment;
import com.example.alan.fyp.CustomDialogListener;
import com.example.alan.fyp.R;
import com.example.alan.fyp.model.User;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by wadealanchan on 4/9/2017.
 */

public class AuthClass extends BaseActivity implements  GoogleApiClient.OnConnectionFailedListener,CustomDialogListener,View.OnClickListener{


    private FirebaseAuth mAuth;

    private EditText mEmail;
    private EditText mPw;
    String Name;
    String Email;
    String Password;
    private DatabaseReference mDatabase;
    public final String TAG ="AuthClass";
    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9001;

    DatabaseReference mDatabaseRefUser;
    ProgressDialog progressDialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_login);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        progressDialog = new ProgressDialog(this);
        mEmail= (EditText) findViewById(R.id.field_email);
        mPw=  (EditText) findViewById(R.id.field_pw);

        findViewById(R.id.btn_createac).setOnClickListener(this);
        findViewById(R.id.btn_signin).setOnClickListener(this);
        findViewById(R.id.btn_signout).setOnClickListener(this);
        findViewById(R.id.btn_googlesignin).setOnClickListener(this);
        mDatabaseRefUser = FirebaseDatabase.getInstance().getReference().child("users");


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();



    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

//    private void createAccount(String email, String password) {
//        Log.d("Create Account:", "createAccount:" + email);
//        if (!validateForm(2)) {
//            return;
//        }
//
//        showProgressDialog();
//        Log.d(TAG,"get it here?");
//        // [START create_user_with_email]
//        mAuth.createUserWithEmailAndPassword(email, password)
//
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                      @Override
//                      public void onComplete(@NonNull Task<AuthResult> task) {
//                          if (task.isSuccessful()) {
//                              // Sign in success, update UI with the signed-in user's information
//                              Log.d("", "createUserWithEmail:success");
//                              FirebaseUser user = mAuth.getCurrentUser();
//
//                              updateUI(user);
//                              setName(Name);
//
//
//                          } else if (!task.isSuccessful()) {
//                              // If sign in fails, display a message to the user.
//                              FirebaseAuthException e = (FirebaseAuthException) task.getException();
//                              Log.w("", "createUserWithEmail:failure", task.getException());
//                              Log.e("LoginActivity", "Failed Registration", e);
//                              Toast.makeText(AuthClass.this, "Authentication failed.",
//                                      Toast.LENGTH_SHORT).show();
//
//                          }
//
//                          // [START_EXCLUDE]
//                          hideProgressDialog();
//                          // [END_EXCLUDE]
//                      }
//
//                  });
//              }
//        // [END create_user_with_email]




    private void createAccount(String email, String password) {


        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {

            progressDialog.setMessage("Signing Up...");
            progressDialog.show();

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseuser = mAuth.getCurrentUser();

                        updateUI(firebaseuser);
                        setName(Name);
                        String userId = mAuth.getCurrentUser().getUid();

                        User user = new User();
                        user.setName(Name);
//                        user.setImage("default");

                        mDatabaseRefUser.child(userId).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                progressDialog.dismiss();

                                if (task.isSuccessful()) {
//                                    Toast.makeText(SignUpActivity.this, "You need to setup your account.", Toast.LENGTH_LONG).show();
//                                    Intent intent = new Intent(SignUpActivity.this, SettingsActivity.class);
//                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                    startActivity(intent);
                                } else {
//                                    Snackbar.make(view, "Sign Up Failed! Try again.", Snackbar.LENGTH_SHORT)
//                                            .setAction("Action", null)
//                                            .show();
                                }
                            }
                        });

                    } else {
                        progressDialog.dismiss();

//                        Snackbar.make(view, "Sign Up Failed! Try again.", Snackbar.LENGTH_SHORT)
//                                .setAction("Action", null)
//                                .show();
                    }
                }
            });

        } else {
//            Snackbar.make(view, "Fill all fields", Snackbar.LENGTH_SHORT)
//                    .setAction("Action", null)
//                    .show();
        }
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
            findViewById(R.id.btn_googlesignin).setVisibility(View.GONE);
        }
        else
        {
            Toast.makeText(AuthClass.this, R.string.signedout,
                    Toast.LENGTH_SHORT).show();
            findViewById(R.id.btn_createac).setVisibility(View.VISIBLE);
            findViewById(R.id.btn_signin).setVisibility(View.VISIBLE);
            findViewById(R.id.btn_signout).setVisibility(View.GONE);
            findViewById(R.id.btn_googlesignin).setVisibility(View.VISIBLE);
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
             signIn(mEmail.getText().toString(), mPw.getText().toString());
                break;

            case R.id.btn_signout:
                signOut();
                break;
            case R.id.btn_googlesignin:
                googlesignIn();
                break;

        }

    }

   private void signOut() {
       mAuth.signOut();
       Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
               new ResultCallback<Status>() {
                   @Override
                   public void onResult(@NonNull Status status) {
                       updateUI(null);
                   }
               });
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
    public void onDialogPositive(String Name,String Email, String Pw) {
        Toast.makeText(this,"Custom Dialog "+  getString(R.string.createac)+" "+Email+ " " +Pw,Toast.LENGTH_SHORT).show();
        this.Name=Name;
        this.Email= Email;
        this.Password=Pw;
        Log.d(TAG+"Email:",Email);
        Log.d(TAG+"Password:",Password);
        createAccount(Email,Pw);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Log.d("Name: ", Name);
//        if(user!=null)
//       {
//           Log.d("setName", "gethere");
//           setName(Name);
//       }
    }

    @Override
    public void onDialogNegative(Object object) {
        //Toast.makeText(this,"Custom Dialog "+getString(R.string.cancel),Toast.LENGTH_SHORT).show();
    }

    private void setName(String Name){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(Name)
                // .setPhotoUri(Uri.parse("https://example.com/jane-q-user/profile.jpg"))
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User profile updated.");
                        }
                    }
                });
    }





    // google//





    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed, update UI appropriately
                // [START_EXCLUDE]
                updateUI(null);
                // [END_EXCLUDE]
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        // [START_EXCLUDE silent]
        showProgressDialog();
        // [END_EXCLUDE]

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(AuthClass.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // [START_EXCLUDE]
                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d("", "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }
    private void googlesignIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

}


