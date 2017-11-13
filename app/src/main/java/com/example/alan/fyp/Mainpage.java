package com.example.alan.fyp;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.alan.fyp.databinding.ActivityMainpageBinding;
import com.example.alan.fyp.model.Post;
import com.example.alan.fyp.viewModel.PostListViewModel;
import com.example.alan.fyp.viewModel.UserViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Mainpage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ActivityMainpageBinding binding;
    public final String TAG = "Mainpage: ";
    DatabaseReference mDatabaseRefPosts;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    RecyclerView mPostList;

    PostListViewModel postList = new PostListViewModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_mainpage);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


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

//

        mAuth = FirebaseAuth.getInstance();
        mDatabaseRefPosts = FirebaseDatabase.getInstance().getReference().child("posts");
        mDatabaseRefPosts.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i = 0;
                postList.items.clear();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    // TODO: handle the post
                    Post p = postSnapshot.getValue(Post.class);
                    postList.items.add(p);
                    //postList.items.set(i++, );
                    Log.d("Mainpage", p.getTitle());
                }

                binding.executePendingBindings();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        layoutManager.setReverseLayout(true);
//        layoutManager.setStackFromEnd(true);
//        mPostList.setHasFixedSize(true);
//        mPostList.setLayoutManager(layoutManager);

        binding.setPostList(postList);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.mainpage, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        if (mAuth.getCurrentUser() != null) {
//            fetchBlogPosts();
//        }
//    }
//
//    private void fetchBlogPosts() {
//        FirebaseRecyclerAdapter<Post, PostViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Post, PostViewHolder>(
//                Post.class,
//                R.layout.list_post,
//                PostViewHolder.class,
//                mDatabaseRefPosts
//        ) {
//            @Override
//            protected void populateViewHolder(PostViewHolder viewHolder, Post model, int position) {
//
//
//
//                ListPostBinding binding1 = viewHolder.getBinding();
//                //PostViewModel postViewModel = new PostViewModel(model.getTitle(), model.getDescription(), model.getImage(),model.getUser().getName());
//                PostViewModel postViewModel = new PostViewModel("123", "123", "123","123");
//                 binding1.setPost(postViewModel);
//
//            }
//
//        };
//
//        mPostList.setAdapter(firebaseRecyclerAdapter);
//
//    }
//
//    public static class PostViewHolder extends RecyclerView.ViewHolder {
//        private ListPostBinding binding2;
//
//        public PostViewHolder(View itemView) {
//            super(itemView);
//
//            binding2 = DataBindingUtil.bind(itemView);
//        }
//
//        public ListPostBinding getBinding() {
//            return binding2;
//        }
//
//    }
}
