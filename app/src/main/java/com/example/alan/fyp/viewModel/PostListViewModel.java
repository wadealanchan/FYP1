package com.example.alan.fyp.viewModel;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;

import com.example.alan.fyp.BR;
import com.example.alan.fyp.R;
import com.example.alan.fyp.model.Post;

import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * Created by wadealanchan on 13/11/2017.
 */

public class PostListViewModel {

    public final ObservableList<Post> items = new ObservableArrayList<Post>();
    public final ItemBinding<Post> itemBinding = ItemBinding.of(BR.post, R.layout.list_post);
}
