package com.example.mytask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.example.mytask.dao.FirebaseHelper;
import com.example.mytask.fragments.EventsFragment;
import com.example.mytask.fragments.ProjectFragment;
import com.example.mytask.fragments.TasksFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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


    Button myListButton, eventsButton, projectsButton;


    TextView usernameTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        replaceFragment(new TasksFragment());

        usernameTextView = findViewById(R.id.username_text_view);

        FirebaseHelper firebaseHelper = new FirebaseHelper();
        firebaseHelper.getUserDisplayName(
                new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String username) {
                        // Handle username retrieval success
                        // Update UI with the username
                        usernameTextView.setText(username);
                    }
                },
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle failure
                        // Display an error message or handle the failure appropriately
                    }
                }
        );

        imageView = findViewById(R.id.profile_image);

        addTaskBtn = findViewById(R.id.add_task_btn);
//        recyclerView = findViewById(R.id.recycler_view);
        menuBtn = findViewById(R.id.menu_btn);
        userInfosLayout = findViewById(R.id.user_info);

        myListButton = findViewById(R.id.my_tasks_btn);
        eventsButton = findViewById(R.id.events_btn);
        projectsButton = findViewById(R.id.projects_btn);

        myListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_layout, TasksFragment.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack("name") // Name can be null
                        .commit();
                // Change style for "Tasks" button
                myListButton.setTextColor(getResources().getColor(R.color.primary));
                myListButton.setBackgroundColor(Color.parseColor("#D4E7F5"));
                myListButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#D4E7F5")));

                // Reset style for "Events" button
                eventsButton.setTextColor(getResources().getColor(R.color.gray));
                eventsButton.setBackgroundColor(getResources().getColor(android.R.color.white));

                // Reset style for "Projects" button
                projectsButton.setTextColor(getResources().getColor(R.color.gray));
                projectsButton.setBackgroundColor(getResources().getColor(android.R.color.white));
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
                // Change style for "Events" button
                eventsButton.setTextColor(getResources().getColor(R.color.primary));
                eventsButton.setBackgroundColor(Color.parseColor("#D4E7F5"));
                eventsButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#D4E7F5")));

                // Reset style for "Tasks" button
                myListButton.setTextColor(getResources().getColor(R.color.gray));
                myListButton.setBackgroundColor(getResources().getColor(android.R.color.white));

                // Reset style for "Projects" button
                projectsButton.setTextColor(getResources().getColor(R.color.gray));
                projectsButton.setBackgroundColor(getResources().getColor(android.R.color.white));
            }
        });

        projectsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_layout, ProjectFragment.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack("name") // Name can be null
                        .commit();
                // Change style for "Events" button
                projectsButton.setTextColor(getResources().getColor(R.color.primary));
                projectsButton.setBackgroundColor(Color.parseColor("#D4E7F5"));
                projectsButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#D4E7F5")));

                // Reset style for "Tasks" button
                myListButton.setTextColor(getResources().getColor(R.color.gray));
                myListButton.setBackgroundColor(getResources().getColor(android.R.color.white));

                // Reset style for "Events" button
                eventsButton.setTextColor(getResources().getColor(R.color.gray));
                eventsButton.setBackgroundColor(getResources().getColor(android.R.color.white));
            }
        });

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        uri = firebaseUser.getPhotoUrl();
        //image view set image uri should not be used with regular URIs. So we are using Picasso

        if(uri != null){
            Picasso.get().load(uri).into(imageView);
        }

        addTaskBtn.setOnClickListener(v -> {
            // Check if the currently active fragment is the TasksFragment
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.frame_layout);
            if (currentFragment instanceof TasksFragment) {
                // If the current fragment is TasksFragment, navigate to TaskDetailsActivity
                startActivity(new Intent(MainActivity.this, TaskDetailsActivity.class));
            }else if (currentFragment instanceof ProjectFragment) {
                // If the current fragment is TasksFragment, navigate to TaskDetailsActivity
                startActivity(new Intent(MainActivity.this, ProjectDetailsActivity.class));
            } else {
                // If the current fragment is not TasksFragment, navigate to EventDetailsActivity
                startActivity(new Intent(MainActivity.this, EventDetailsActivity.class));
            }
        });
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