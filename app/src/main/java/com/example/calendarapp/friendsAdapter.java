package com.example.calendarapp;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calendarapp.Model.User;
import com.example.calendarapp.calendar.activities.EventActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


// FirebaseRecyclerAdapter is a class provided by
// FirebaseUI. it provides functions to bind, adapt and show
// database contents in a Recycler View
public class friendsAdapter extends FirebaseRecyclerAdapter<
        User, friendsAdapter.personsViewholder> {

    private DatabaseReference userRef,receiverReference;
    private FirebaseAuth mAuth;
    private String senderUserID, receiverUserID;


    public friendsAdapter(
            @NonNull FirebaseRecyclerOptions<User> options)
    {
        super(options);
    }

    // Function to bind the view in Card view(here
    // "person.xml") iwth data in
    // model class(here "person.class")

    @Override
    protected void
    onBindViewHolder(@NonNull final personsViewholder holder,
                     int position, @NonNull final User model)
    {

        // Add username from model class (here
        // "person.class")to appropriate view in Card
        // view (here "person.xml")
        holder.username.setText(model.getUsername());
        holder.status.setText(model.getStatus());

        // Set visibility to add friend
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        mAuth = FirebaseAuth.getInstance();
        receiverReference = FirebaseDatabase.getInstance().getReference().child("Users");
        senderUserID = mAuth.getCurrentUser().getUid();
        holder.unfriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                receiverUserID = model.getId();
                userRef.child(senderUserID).child("friends").child(receiverUserID).removeValue()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                userRef.child(receiverUserID).child("friends").child(senderUserID).removeValue();
                                holder.unfriend.setVisibility(View.GONE);
                            }
                        });
            }
        });






                userRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull final DataSnapshot snapshot) {

                        holder.make_schedule.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(final View v) {
                                receiverUserID = model.getId();
                                final Intent match = new Intent(v.getContext(), MatchedSchedule.class);


                                if(snapshot.child(receiverUserID).hasChild("schedule") && snapshot.child(senderUserID).hasChild("schedule")) {

                            match.putExtra("receiverUserID", receiverUserID);
                            match.putExtra("senderUserID", senderUserID);
                            v.getContext().startActivity(match);

                            Log.d("receiverUserID", receiverUserID);
                            Log.d("senderUserID", senderUserID);
                        }
                        else {
                            Toast.makeText(v.getContext(), "This user doesn't have schedule", Toast.LENGTH_LONG).show();
                        }
                            }
                        });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



    }



    // Function to tell the class about the Card view (here
    // "person.xml")in
    // which the data will be shown
    @NonNull
    @Override
    public personsViewholder
    onCreateViewHolder(@NonNull ViewGroup parent,
                       int viewType)
    {
        View view
                = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.friend, parent, false);
        return new friendsAdapter.personsViewholder(view);
    }

    // Sub Class to create references of the views in Crad
    // view (here "person.xml")
    class personsViewholder
            extends RecyclerView.ViewHolder {
        TextView username, status;
        Button unfriend, make_schedule;
        //ImageView imageView;
        public personsViewholder(@NonNull View itemView)
        {
            super(itemView);

            username = itemView.findViewById(R.id.username);
            status = itemView.findViewById(R.id.status);
            unfriend = itemView.findViewById(R.id.unfriend_button);
            make_schedule = itemView.findViewById(R.id.make_schedule_button);
        }
    }
}