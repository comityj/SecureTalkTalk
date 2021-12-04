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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatMenu extends AppCompatActivity {

    private FirebaseAuth mAuth;
    public static String useruid, chat;
    private List<String> roomlist = new ArrayList<>();
    EditText findroomet;
    FloatingActionButton roombtn;
    Button findbtn, mybtn;
    ListView listroom;

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_menu);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        roombtn = (FloatingActionButton) findViewById(R.id.roomadd);
        findroomet = (EditText) findViewById(R.id.findroom);
        findbtn = (Button) findViewById(R.id.findbtn);
        mybtn = (Button) findViewById(R.id.mybtn);
        listroom = (ListView) findViewById(R.id.roomlist);

        useruid = user.getUid();

        firebaseDatabase.getReference().child("message").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                roomlist.clear();
                for (DataSnapshot dataSnapshots : dataSnapshot.getChildren()) {
                    //ChatDTO chatDTO = dataSnapshot.getValue(ChatDTO.class);
                    RoomDTO roomDTO = dataSnapshot.getValue(RoomDTO.class);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        roombtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //final
                final EditText roomname = new EditText(ChatMenu.this);
                AlertDialog.Builder newroomad = new AlertDialog.Builder(ChatMenu.this)
                    .setTitle("채팅방 생성")
                    .setMessage("생성할 채팅방 이름을 쓰세요.")
                    .setView(roomname)
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(ChatMenu.this, ChatActivity.class);
                        intent.putExtra("roomName", roomname.getText().toString());
                        startActivity(intent);
                    }
                })
                    .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                newroomad.show();
                RoomDTO roomDTO = new RoomDTO(useruid, chat);
                databaseReference.child("message").child(roomname.getText().toString()).push().setValue(roomDTO);

/*                Intent intent = new Intent(ChatMenu.this, ChatActivity.class);
                intent.putExtra("roomName", roomname.getText().toString());*/

            }
        });
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
                //Query findQuery = databaseReference.orderByChild("message").equalTo(String.valueOf(findroomet));
/*                if (){
                    Intent intent = new Intent(ChatMenu.this, ChatActivity.class);
                    intent.putExtra("roomName", findroomet.getText().toString());
                    startActivity(intent);
                } else {
                    Toast.makeText(ChatMenu.this, "해당 채팅방이 없습니다.", Toast.LENGTH_LONG);
                }*/
                Log.i("확인", databaseReference.orderByChild("message").equalTo("검색용").toString());

                //Intent intent = new Intent(ChatMenu.this, ChatActivity.class);
                //intent.putExtra("roomName", findroomet.);
                //startActivity(intent);
            }
        });
        showChatList();
    }

    private void showChatList() {
        //리스트 어댑터 생성 및 셋팅
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1);
        listroom.setAdapter(adapter);

        //데이터 받아오기 및 어댑터 데이터 추가, 삭제 등 리스너 관리
        databaseReference.child("message").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ChatDTO chatDTO = dataSnapshot.getValue(ChatDTO.class);
                //RoomDTO roomDTO = dataSnapshot.getValue(RoomDTO.class);
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

        listroom.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                intent.putExtra("roomName" ,((TextView) view).getText().toString());
//                Log.i("주소a", "chatName" );
//                Log.i("주소b", ((TextView) view).getText().toString());
                startActivity(intent);
            }
        });
        listroom.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
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
                        Toast.makeText(ChatMenu.this, "삭제되었습니다.", Toast.LENGTH_SHORT).show();
                        //databaseReference.child("message").child(roomlist.get(position)).removeValue();
                        //Log.i("주소00", databaseReference.child("message").child(roomlist.get(position)).toString());
                        //databaseReference.child("message").child("roomName").removeValue();
                    }
                });
                AlertDialog alertDialog = alertDialogBulder.create();
                alertDialog.show();
                return true;
            }
        });

    }

}
/*


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
    EditText findroomet;
    Button findbtn, mybtn;
    ListView roomlist;

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_menu);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        findroomet = (EditText) findViewById(R.id.findroomet);
        findbtn = (Button) findViewById(R.id.findbtn);
        mybtn = (Button) findViewById(R.id.mybtn);
        roomlist = (ListView) findViewById(R.id.roomlist);



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
                intent.putExtra("chatName", findroomet.getText().toString());
                startActivity(intent);
            }
        });
        showChatList();
    }

    private void showChatList() {
        //리스트 어댑터 생성 및 셋팅
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1);
        roomlist.setAdapter(adapter);

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

        roomlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                intent.putExtra("chatName" ,((TextView) view).getText().toString());
//                Log.i("주소a", "chatName" );
//                Log.i("주소b", ((TextView) view).getText().toString());
                startActivity(intent);
            }
        });
        roomlist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //String diiamsg = "삭제하시겠습니까?<br />" + "position : " + position;
                */
/*                DialogInterface.OnClickListener deleteListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        databaseReference.child("message").child("chatName").removeValue();
                    }
                };*//*

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

*/
