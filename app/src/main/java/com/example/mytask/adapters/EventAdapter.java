package com.example.mytask.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mytask.EventDetailsActivity;
import com.example.mytask.R;
import com.example.mytask.Utility;
import com.example.mytask.models.Event;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.squareup.picasso.Picasso;

public class EventAdapter extends FirestoreRecyclerAdapter<Event, EventAdapter.EventViewHolder> {

    private Context context;

    public EventAdapter(@NonNull FirestoreRecyclerOptions<Event> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull EventViewHolder holder, int position, @NonNull Event event) {
        holder.titleTextView.setText(event.getTitle());
        holder.descriptionTextView.setText(event.getDescription());
        holder.timestampTextView.setText(Utility.timestampToString(event.getTimestamp()));

        // Load image using Picasso library
        Picasso.get().load(event.getImageUrl()).placeholder(R.drawable.image).into(holder.eventImageView);

        // Set click listener to open EventDetailsActivity
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, EventDetailsActivity.class);
            intent.putExtra("title", event.getTitle());
            intent.putExtra("description", event.getDescription());
            intent.putExtra("docId", getSnapshots().getSnapshot(position).getId());
            context.startActivity(intent);
        });
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_event_item, parent, false);
        return new EventViewHolder(view);
    }

    static class EventViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, descriptionTextView, timestampTextView;
        ImageView eventImageView;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.event_title_text_view);
            descriptionTextView = itemView.findViewById(R.id.event_description_text_view);
            timestampTextView = itemView.findViewById(R.id.event_timestamp_text_view);
            eventImageView = itemView.findViewById(R.id.event_image_view);
        }
    }
}

