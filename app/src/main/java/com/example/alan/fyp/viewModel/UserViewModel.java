package com.example.alan.fyp.viewModel;

import android.databinding.ObservableField;

/**
 * Created by wadealanchan on 30/10/2017.
 */

public class UserViewModel {
    public ObservableField<String> displayName = new ObservableField<String>();
    public ObservableField<String> password = new ObservableField<String>();
    public ObservableField<String> email = new ObservableField<String>();


    public UserViewModel() {

    }

    public UserViewModel(String displayName, String password, String email) {
        this.displayName.set(displayName);
        this.password.set(password);
        this.email.set(email);
    }
}
