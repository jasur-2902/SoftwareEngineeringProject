package com.example.calendarapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.calendarapp.Adapter.UserAdapter;
import com.example.calendarapp.Model.FriendList;
import com.example.calendarapp.Model.User;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.List;

public class UsersActivity extends AppCompatActivity {

    private friendsAdapter adapter;
    private RecyclerView recyclerView;
    private FirebaseAuth mAuth;
    //private DatabaseReference mbase;

    View view;

    private UserAdapter userAdapter;
    private List<User> mUsers;
    private List<FriendList> mFriends;

    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    private Button sendRequest;

    EditText search_users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        mAuth = FirebaseAuth.getInstance();
        recyclerView = findViewById(R.id.friends_list);
        setUpRecyclerView();


    }

    public void setUpRecyclerView(){
        String self_user_ID = mAuth.getCurrentUser().getUid();
        Query mbase = FirebaseDatabase.getInstance().getReference().child("Users").orderByChild("friends/"+self_user_ID).equalTo("true");

        FirebaseRecyclerOptions<User> options = new FirebaseRecyclerOptions.Builder<User>()
                .setQuery(mbase, User.class)
                .build();

        adapter = new friendsAdapter(options);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(UsersActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        setUpRecyclerView();
        if (adapter != null) {
            adapter.startListening();
        }

    }
}