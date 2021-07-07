package com.comityj.securetalktalk;

import android.app.AlertDialog;
import android.app.AppComponentFactory;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChatMenu extends AppCompatActivity {

    private FirebaseAuth mAuth;
    EditText findchatet;
    Button findbtn, mybtn;
    ListView chatlist;

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_menu);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        findchatet = (EditText) findViewById(R.id.findchat);
        findbtn = (Button) findViewById(R.id.findbtn);
        mybtn = (Button) findViewById(R.id.mybtn);
        chatlist = (ListView) findViewById(R.id.chatlist);



        mybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatMenu.this, GoogleActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });

        findbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ChatMenu.this, ChatActivity.class);
                intent.putExtra("chatName", findchatet.getText().toString());
                startActivity(intent);
            }
        });
        showChatList();
    }

    private void showChatList() {
        //리스트 어댑터 생성 및 셋팅
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1);
        chatlist.setAdapter(adapter);

        //데이터 받아오기 및 어댑터 데이터 추가, 삭제 등 리스너 관리
        databaseReference.child("message").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ChatDTO chatDTO = dataSnapshot.getValue(ChatDTO.class);
                Log.e("LOG", "dataSnapshot.getKey() : " + dataSnapshot.getKey());
                adapter.add(dataSnapshot.getKey()); // + " " + chatDTO.getMsg()

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {            }

            @Override
            public void onCancelled(DatabaseError databaseError) {            }
        });

        chatlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                intent.putExtra("chatName" ,((TextView) view).getText().toString());
                startActivity(intent);
            }
        });
        chatlist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //String diiamsg = "삭제하시겠습니까?<br />" + "position : " + position;
                /*                DialogInterface.OnClickListener deleteListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        databaseReference.child("message").child("chatName").removeValue();
                    }
                };*/
                AlertDialog.Builder alertDialogBulder = new AlertDialog.Builder(ChatMenu.this);
                alertDialogBulder.setMessage("삭제");
                alertDialogBulder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(ChatMenu.this, "삭제되었습니다.", Toast.LENGTH_SHORT);
                        databaseReference.child("message").child("chatName").removeValue();
                    }
                });
                AlertDialog alertDialog = alertDialogBulder.create();
                alertDialog.show();
                return true;
            }
        });

    }

}
