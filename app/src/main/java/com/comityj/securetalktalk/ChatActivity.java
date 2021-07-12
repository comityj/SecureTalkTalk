package com.comityj.securetalktalk;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/*import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;*/
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;

import javax.crypto.spec.IvParameterSpec;

import javax.crypto.spec.SecretKeySpec;



import android.app.Activity;

import android.os.Bundle;

import android.util.Base64;

import android.util.Log;

import android.view.Menu;

import android.view.MenuItem;

import org.w3c.dom.CDATASection;

public class ChatActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private String chatname, username;
    private boolean encryptox;
    private List<String> chatlist = new ArrayList<>();
    //ArrayList<ChatDTO> chatlist;

/*    public ChatActivity(ArrayList<ChatDTO> chatlist) {
        this.chatlist = chatlist;
    }*/

    ListView listmsg;
    EditText editmsg;
    Button send, aessend;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    //public static String secretKey = "비밀키";
    private static final String KEY = "aes256";
    //String aestxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        listmsg = (ListView) findViewById(R.id.msglist);
        editmsg = (EditText) findViewById(R.id.msget);
        send = (Button) findViewById(R.id.sendbtn);
        aessend = (Button) findViewById(R.id.aesbtn);

        Intent intent = getIntent();
        chatname = intent.getStringExtra("chatName");
        username = user.getDisplayName();

        firebaseDatabase.getReference().child("message").child(chatname).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatlist.clear();
                for(DataSnapshot dataSnapshots : dataSnapshot.getChildren()){
                    ChatDTO chatDTO = dataSnapshot.getValue(ChatDTO.class);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //openChat(chatname);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                encryptox = false;
                ChatDTO chatDTO = new ChatDTO(username, editmsg.getText().toString(), encryptox);
                databaseReference.child("message").child(chatname).push().setValue(chatDTO);
                editmsg.setText("");
                //Toast.makeText(ChatActivity.this,databaseReference.child("message").child(chatname).child(chatlist.get(position)).toString(), Toast.LENGTH_SHORT).show();
            }
        });

        aessend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ChatDTO chatDTO = new ChatDTO(username, AES256.AES_Encode("가나다"));
                try {
                    String key = "key";
                    String aesen;
                    //String aesde;
                    aesen = Encrypt(editmsg.getText().toString(), key);
                    //aesde = Decrypt(aesen, key);
                    encryptox = true;
                    ChatDTO chatDTO = new ChatDTO(username, aesen, encryptox);
                    databaseReference.child("message").child(chatname).push().setValue(chatDTO);
                    editmsg.setText("");
                } catch (Exception e){
                    e.printStackTrace();
                }
/*                ChatDTO chatDTO = new ChatDTO(username, editmsg.getText().toString());
                databaseReference.child("message").child(chatname).push().setValue(chatDTO);
                Toast.makeText(ChatActivity.this, "AES암호화 : " + editmsg.getText().toString(), Toast.LENGTH_LONG).show();
//                Toast.makeText(ChatActivity.this, "AES복호화 : " + AES256.AES_Decode("aaaas"), Toast.LENGTH_LONG).show();
                editmsg.setText("");*/
            }
        });

        //채팅 길게 누르면 popup메뉴
        listmsg.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {
                PopupMenu popup = new PopupMenu(ChatActivity.this, view);
                getMenuInflater().inflate(R.menu.chat_popup, popup.getMenu());
                //Toast.makeText(ChatActivity.this,"삭제 되었습니다.", Toast.LENGTH_SHORT).show();
                Toast.makeText(ChatActivity.this, String.valueOf(position), Toast.LENGTH_SHORT).show();
                //Toast.makeText(ChatActivity.this,databaseReference.child("message").child(chatname).child(chatlist.get(position)).toString(), Toast.LENGTH_SHORT).show();
                //final String chatkey = chatlist.get(position);
                //final FirebaseDatabase chatmsg = databaseReference.child("message").child("chatName").child(chatkey).child("msg").getValue();
                //databaseReference.child("message").child(chatname).child(chatkey).child("msg")
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.encryt:
                                //Toast.makeText(ChatActivity.this,"암호화 되었습니다.", Toast.LENGTH_SHORT).show();
                                //Toast.makeText(ChatActivity.this,databaseReference.child("message").child(chatname).child(chatlist.get(position)).toString(), Toast.LENGTH_SHORT).show();
                                //Toast.makeText(ChatActivity.this, chatkey, Toast.LENGTH_SHORT).show();
                                break;

                            case R.id.decryt:
                                Toast.makeText(ChatActivity.this,"복호화 되었습니다.", Toast.LENGTH_SHORT).show();
                                //Toast.makeText(ChatActivity.this,position, Toast.LENGTH_SHORT).show();
                                //chatlist.remove(position);
                                break;

                            case R.id.copy:
                                Toast.makeText(ChatActivity.this, databaseReference.child("message").child("chatName").child(chatlist.get(position)).toString(), Toast.LENGTH_SHORT).show();
                                //Toast.makeText(ChatActivity.this,"복사 되었습니다.", Toast.LENGTH_SHORT).show();
                                //Toast.makeText(, Toast.LENGTH_SHORT).show();
                                break;

                            case R.id.delete:
                                //new openChat().new onChildRemoved();

                                //final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1);
/*                                databaseReference.child("message").child("chatName").child(chatlist.get(position)).addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(DataSnapshot snapshot, String s) {      }

                                    @Override
                                    public void onChildChanged(DataSnapshot snapshot, String s) {        }

                                    @Override
                                    public void onChildRemoved(DataSnapshot snapshot) {        }

                                    @Override
                                    public void onChildMoved(DataSnapshot snapshot, String s) {      }

                                    @Override
                                    public void onCancelled(DatabaseError error) {         }
                                });*/
                                //deletechat(position);
                                //databaseReference.child("message").child("chatName").child(chatlist.get(position)).removeValue();
                                //Toast.makeText(ChatActivity.this,"삭제 되었습니다.", Toast.LENGTH_SHORT).show();
/*                                databaseReference.child("message").child("chatName").child(chatkey).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(ChatActivity.this,"삭제 성공", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(ChatActivity.this,"삭제 실패.", Toast.LENGTH_SHORT).show();
                                    }
                                });*/
                                //chatlist.remove(position);
                                //deletechat(position);


                                break;

                        }
                        return false;
                    }
                });
                popup.show();
                return true;
            }
        });

        openChat(chatname);
    }
    private void deletechat(int postion){
        firebaseDatabase.getReference().child("messgae").child(chatname).child(chatlist.get(postion)).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(ChatActivity.this, "삭제", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ChatActivity.this, "실패", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public static String Encrypt(String text, String key) throws Exception

    {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] keyBytes= new byte[16];
        byte[] b= key.getBytes("UTF-8");
        int len= b.length;
        if (len > keyBytes.length) len = keyBytes.length;
        System.arraycopy(b, 0, keyBytes, 0, len);
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(keyBytes);
        cipher.init(Cipher.ENCRYPT_MODE,keySpec,ivSpec);

        byte[] results = cipher.doFinal(text.getBytes("UTF-8"));

        return Base64.encodeToString(results, 0);
    }

    public static String Decrypt(String text, String key) throws Exception

    {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] keyBytes= new byte[16];
        byte[] b= key.getBytes("UTF-8");
        int len= b.length;
        if (len > keyBytes.length) len = keyBytes.length;
        System.arraycopy(b, 0, keyBytes, 0, len);
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(keyBytes);
        cipher.init(Cipher.DECRYPT_MODE,keySpec,ivSpec);

        byte [] results = cipher.doFinal(Base64.decode(text, 0));

        return new String(results,"UTF-8");
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
                String chatKey = dataSnapshot.getKey();
                chatlist.add(chatKey);
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
