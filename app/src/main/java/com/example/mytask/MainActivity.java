package com.example.mytask;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;


import com.example.mytask.fragments.EventsFragment;
import com.example.mytask.fragments.TasksFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

//    FloatingActionButton addTaskBtn;
    ImageButton addTaskBtn;
//    RecyclerView recyclerView;
    ImageButton menuBtn;

    RelativeLayout userInfosLayout;

//    TaskAdapter taskAdapter;

    FirebaseUser firebaseUser;

    Uri uri;

    CircleImageView imageView;


    Button myListButton, eventsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        replaceFragment(new TasksFragment());

        imageView = findViewById(R.id.profile_image);

        addTaskBtn = findViewById(R.id.add_task_btn);
//        recyclerView = findViewById(R.id.recycler_view);
        menuBtn = findViewById(R.id.menu_btn);
        userInfosLayout = findViewById(R.id.user_info);

        myListButton = findViewById(R.id.my_tasks_btn);
        eventsButton = findViewById(R.id.events_btn);

        myListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_layout, TasksFragment.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack("name") // Name can be null
                        .commit();
            }
        });


        eventsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_layout, EventsFragment.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack("name") // Name can be null
                        .commit();
            }
        });

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        uri = firebaseUser.getPhotoUrl();
        //image view set image uri should not be used with regular URIs. So we are using Picasso

        if(uri != null){
            Picasso.get().load(uri).into(imageView);
        }

        addTaskBtn.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, TaskDetailsActivity.class)));
        menuBtn.setOnClickListener(v -> showMenu());
        userInfosLayout.setOnClickListener( v -> {
            startActivity(new Intent(MainActivity.this,ProfileActivity.class));
        });

//        setupRecyclerView();

        // Find the TasksFragment using FragmentManager
//        TasksFragment tasksFragment = (TasksFragment) getSupportFragmentManager().findFragmentById(R.id.main_fragment_container);

    }

    void showMenu() {
        PopupMenu popupMenu = new PopupMenu(MainActivity.this,menuBtn);
        popupMenu.getMenu().add("Logout");
        popupMenu.getMenu().add("Profile");
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if(menuItem.getTitle() == "Logout"){
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(MainActivity.this,SignInActivity.class));
                    finish();
                    return true;
                }
                if(menuItem.getTitle() == "Profile"){
                    startActivity(new Intent(MainActivity.this,ProfileActivity.class));
                    return true;
                }
                return false;
            }
        });
    }

//    void setupRecyclerView(){
//        Query query = Utility.getCollectionReferenceForTask().orderBy("timestamp",Query.Direction.DESCENDING);
//        FirestoreRecyclerOptions<Task> options = new FirestoreRecyclerOptions.Builder<Task>()
//                .setQuery(query,Task.class).build();
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        taskAdapter = new TaskAdapter(options,this);
//        recyclerView.setAdapter(taskAdapter);
//    }


//    @Override
//    protected void onStart() {
//        super.onStart();
////        taskAdapter.startListening();
//    }

//    @Override
//    protected void onStop() {
//        super.onStop();
////        taskAdapter.stopListening();
//    }

//    @Override
//    protected void onResume() {
//        super.onResume();
////        taskAdapter.notifyDataSetChanged();
//    }

    private void replaceFragment(Fragment fragment){

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.commit();

    }
}