package com.example.mytask.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mytask.R;
import com.example.mytask.TaskDetailsActivity;
import com.example.mytask.Utility;
import com.example.mytask.models.Event;
import com.example.mytask.models.Task;
import com.example.mytask.models.User;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.LinkedList;
import java.util.Objects;

public class UserAdapter extends FirestoreRecyclerAdapter<User, UserAdapter.UserViewHolder> {

    Context context;

    public UserAdapter(@NonNull FirestoreRecyclerOptions<User> options, Context context) {
        super(options);
        this.context = context;
        Log.d("UserAdapter", "Binding view holder: constructor");
    }

    @Override
    protected void onBindViewHolder(@NonNull UserViewHolder holder, int position, @NonNull User user) {
        Log.d("UserAdapter", "Binding view holder: userrrr");
        holder.email.setText(user.getEmail());
        holder.username.setText(user.getUsername());

        holder.isSelected.setOnCheckedChangeListener(null); // Prevent unwanted listener calls during recycling
        holder.isSelected.setChecked(user.isSelected()); // Correctly reflect the current state
        holder.isSelected.setOnCheckedChangeListener((buttonView, isChecked) -> {
            user.setSelected(isChecked); // Update user object or handle selection state persistence
        });

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, TaskDetailsActivity.class);
            intent.putExtra("email", user.getEmail());
            intent.putExtra("username", user.getUsername());
            String docId = getSnapshots().getSnapshot(position).getId();
            intent.putExtra("docId", docId);
            context.startActivity(intent);
        });
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("UserAdapter", "Binding view holder: Creatttte");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_user_item,parent,false);
        Log.d("UserAdapter", "Binding view holder: passed Creatttte");
        return new UserViewHolder(view);
    }

    static class UserViewHolder extends RecyclerView.ViewHolder{
        TextView email,username;
        CheckBox isSelected;
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            Log.d("UserAdapter", "Binding view holder: Enter to static");
            email = itemView.findViewById(R.id.user_email_text_view);
            username = itemView.findViewById(R.id.user_username_text_view);
            isSelected = itemView.findViewById(R.id.selected_checkbox);
            Log.d("UserAdapter", "Binding view holder: passed params");
        }

    }

}
