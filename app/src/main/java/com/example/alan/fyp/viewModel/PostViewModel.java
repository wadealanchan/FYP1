package com.example.alan.fyp.viewModel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.ObservableField;

import com.example.alan.fyp.model.Post;

/**
 * Created by wadealanchan on 11/11/2017.
 */

public class PostViewModel {

    public final ObservableField<String> Title = new ObservableField<String>();
    public final ObservableField<String> Description = new ObservableField<String>();
    public final ObservableField<String> image = new ObservableField<String>();
    public final ObservableField<String> UserName = new ObservableField<String>();

    public PostViewModel() {
    }


    public PostViewModel(String Title,String Description,String image, String UserName)
    {
        this.Title.set(Title);
        this.Description.set(Description);
        this.image.set(image);
        this.UserName.set(UserName);
    }

}
