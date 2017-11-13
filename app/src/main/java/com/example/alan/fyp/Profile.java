package com.example.alan.fyp;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.alan.fyp.databinding.ProfilepageBinding;
import com.example.alan.fyp.viewModel.UserViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by wadealanchan on 25/10/2017.
 */

public class Profile extends AppCompatActivity{

    ProfilepageBinding binding;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.profilepage);

        FirebaseAuth.getInstance().addAuthStateListener((firebaseAuth) -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                UserViewModel userViewModel = new UserViewModel(user.getDisplayName(), "", user.getEmail());
                binding.setUser(userViewModel);
                // User is signed in
                //Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
            } else {
               binding.setUser(null);
                // User is signed out
                //Log.d(TAG, "onAuthStateChanged:signed_out");
            }
        });





    }





}
