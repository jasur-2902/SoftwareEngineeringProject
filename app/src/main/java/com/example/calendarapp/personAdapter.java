package com.example.calendarapp;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.squareup.picasso.Picasso;


// FirebaseRecyclerAdapter is a class provided by
// FirebaseUI. it provides functions to bind, adapt and show
// database contents in a Recycler View
public class personAdapter extends FirebaseRecyclerAdapter<
        person, personAdapter.personsViewholder> {

    public personAdapter(
            @NonNull FirebaseRecyclerOptions<person> options)
    {
        super(options);
    }

    // Function to bind the view in Card view(here
    // "person.xml") iwth data in
    // model class(here "person.class")
    @Override
    protected void
    onBindViewHolder(@NonNull personsViewholder holder,
                     int position, @NonNull person model)
    {

        // Add username from model class (here
        // "person.class")to appropriate view in Card
        // view (here "person.xml")
        holder.username.setText(model.getUsername());

        // Add status from model class (here
        // "person.class")to appropriate view in Card
        // view (here "person.xml")
        holder.status.setText(model.getStatus());

        // Add image url from model class (here
        // "person.class")to appropriate view in Card
        // view (here "person.xml")
        holder.imageURL.setText(model.getImageURL());
       // Picasso.get().load("@drawable/calendarlogo.jpg").into(holder.imageView);
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
                .inflate(R.layout.person, parent, false);
        return new personAdapter.personsViewholder(view);
    }

    // Sub Class to create references of the views in Crad
    // view (here "person.xml")
    class personsViewholder
            extends RecyclerView.ViewHolder {
        TextView username, status, imageURL;
        //ImageView imageView;
        public personsViewholder(@NonNull View itemView)
        {
            super(itemView);

            username
                    = itemView.findViewById(R.id.username);
            status = itemView.findViewById(R.id.status);
            imageURL = itemView.findViewById(R.id.imageURL);
            //imageView = itemView.findViewById(R.id.imageView);
        }
    }
}