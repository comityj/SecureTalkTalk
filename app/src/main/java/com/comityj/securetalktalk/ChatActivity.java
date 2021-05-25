package com.comityj.securetalktalk;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChatActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private String chatname, username;

    ListView listmsg;
    EditText editmsg;
    Button send;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        listmsg = (ListView) findViewById(R.id.msglist);
        editmsg = (EditText) findViewById(R.id.msget);
        send = (Button) findViewById(R.id.sendbtn);

        Intent intent = getIntent();
        chatname = intent.getStringExtra("chatName");
        username = user.getDisplayName();
        Toast.makeText(ChatActivity.this, (username), Toast.LENGTH_SHORT).show();

        openChat(chatname);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatDTO chatDTO = new ChatDTO(username, editmsg.getText().toString());
                databaseReference.child("message").child(chatname).push().setValue(chatDTO);
                editmsg.setText("");
            }
        });

    }

    private void addMessage(DataSnapshot dataSnapshot, ArrayAdapter<String> adapter) {
        ChatDTO chatDTO = dataSnapshot.getValue(ChatDTO.class);
        adapter.add(chatDTO.getUsername() + " : " + chatDTO.getMsg());
    }
    private void removeMessage(DataSnapshot dataSnapshot, ArrayAdapter<String> adapter) {
        ChatDTO chatDTO = dataSnapshot.getValue(ChatDTO.class);
        adapter.remove(chatDTO.getUsername() + " : " + chatDTO.getMsg());
    }
    private void openChat(String chatName) {
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1);
        listmsg.setAdapter(adapter);
        databaseReference.child("message").child(chatName).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
/*                //ChatActivity chatActivity = dataSnapshot.getValue(ChatActivity.class);
                ChatDTO chatDTO = new ChatDTO(username, editmsg.getText().toString());
                //adapter.add(chatDTO.getUsername() + ": " + chatDTO.getMsg());
                mAuth = FirebaseAuth.getInstance();
                FirebaseUser user = mAuth.getCurrentUser();
                adapter.add(user.getDisplayName() + ": " + chatDTO.getMsg());*/
                addMessage(dataSnapshot, adapter);
                Log.e("LOG", "s:" + s);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) { }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) { removeMessage(dataSnapshot, adapter); }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) { }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });
    }

}
