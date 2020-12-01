package com.example.calendarapp.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.calendarapp.Adapter.UserAdapter;
import com.example.calendarapp.Model.FriendList;
import com.example.calendarapp.Model.User;
import com.example.calendarapp.R;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;


import com.example.calendarapp.Model.User;
import com.example.calendarapp.friendsAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UsersFragment} factory method to
 * create an instance of this fragment.
 */
public class UsersFragment extends Fragment {

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = inflater.inflate(R.layout.fragment_users, container, false);

        mAuth = FirebaseAuth.getInstance();
        recyclerView = (RecyclerView) view.findViewById(R.id.friends_list);
        setUpRecyclerView();

        return view;
    }


    public void setUpRecyclerView(){
        String self_user_ID = mAuth.getCurrentUser().getUid();
        Query mbase = FirebaseDatabase.getInstance().getReference().child("Users").orderByChild("friends/"+self_user_ID).equalTo("true");

        FirebaseRecyclerOptions<User> options = new FirebaseRecyclerOptions.Builder<User>()
                .setQuery(mbase, User.class)
                .build();

        adapter = new friendsAdapter(options);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
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

    @Override
    public void onStop() {
        super.onStop();

    }

}
