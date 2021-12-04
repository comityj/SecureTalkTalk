package com.comityj.securetalktalk;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.TextView;
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
import com.squareup.picasso.Picasso;

/*import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;*/
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    //private String chatname, username, profileurl;
    public static String chatname, username, profileurl, useremail, aeskey;
    private boolean encryptox;
    private List<String> chatlist = new ArrayList<>();
    //ArrayList<ChatDTO> chatlist;
    //ArrayList<String> chatlist = new ArrayList<>();

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

    private ArrayList<ChatDTO> chatdto = new ArrayList<>();
    ChatAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);

        //getSupportActionBar().setTitle(chatname);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        listmsg = (ListView) findViewById(R.id.msglist);
        editmsg = (EditText) findViewById(R.id.msget);
        send = (Button) findViewById(R.id.sendbtn);
        aessend = (Button) findViewById(R.id.aesbtn);
        CircleImageView youpro = findViewById(R.id.chat_profile);
/*        TextView youname = findViewById(R.id.you_name);
        TextView youmsg = findViewById(R.id.you_msg);
        TextView mename = findViewById(R.id.me_name);
        TextView memsg = findViewById(R.id.me_msg);*/

        adapter = new ChatAdapter(chatdto, getLayoutInflater());
        listmsg.setAdapter(adapter);

        Intent intent = getIntent();
        chatname = intent.getStringExtra("roomName");
        username = user.getDisplayName();
        profileurl = user.getPhotoUrl().toString();
        useremail = user.getEmail();

        firebaseDatabase.getReference().child("message").child(chatname).child("chat").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //chatdto.clear();
                chatlist.clear();
                for (DataSnapshot dataSnapshots : dataSnapshot.getChildren()) {
                    ChatDTO chatDTO = dataSnapshot.getValue(ChatDTO.class);
                    //String chatkey = dataSnapshot.child(chatname).getKey();


                    //chatdto.add(chatDTO);
                    //chatlist.add(chatkey);
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
                aeskey = "";
                ChatDTO chatDTO = new ChatDTO(username, editmsg.getText().toString(), encryptox, profileurl, useremail, aeskey);
                databaseReference.child("message").child(chatname).child("chat").push().setValue(chatDTO);
                editmsg.setText("");
                updatechat();
                //Toast.makeText(ChatActivity.this,databaseReference.child("message").child(chatname).child(chatlist.get(position)).toString(), Toast.LENGTH_SHORT).show();
            }
        });

        aessend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ChatDTO chatDTO = new ChatDTO(username, AES256.AES_Encode("가나다"));
                final EditText key = new EditText(ChatActivity.this);
                final AlertDialog.Builder dekey = new AlertDialog.Builder(ChatActivity.this)
                        .setTitle("암호화")
                        .setMessage("KEY를 입력하세요.")
                        .setView(key)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                //String aesde;
                                try {
                                    String aesen;
                                    aesen = Encrypt(editmsg.getText().toString(), key.getText().toString());
                                    aeskey = key.getText().toString();
                                    //aesde = Decrypt(aesen, key);
                                    encryptox = true;
                                    ChatDTO chatDTO = new ChatDTO(username, aesen, encryptox, profileurl, useremail, aeskey);
                                    databaseReference.child("message").child(chatname).child("chat").push().setValue(chatDTO);
                                    editmsg.setText("");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                dekey.show();
                updatechat();

/*                try {
                    final EditText key = new EditText(ChatActivity.this);
                    final AlertDialog.Builder dekey = new AlertDialog.Builder(ChatActivity.this)
                            .setTitle("암호화")
                            .setMessage("KEY를 입력하세요.")
                            .setView(key)
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                    //String key = "key";
                    String aesen;
                    //String aesde;
                    aesen = Encrypt(editmsg.getText().toString(), key.getText().toString());
                    aeskey = key.getText().toString();
                    //aesde = Decrypt(aesen, key);
                    encryptox = true;
                    ChatDTO chatDTO = new ChatDTO(username, aesen, encryptox, profileurl, useremail, aeskey);
                    databaseReference.child("message").child(chatname).push().setValue(chatDTO);
                    editmsg.setText("");
                } catch (Exception e){
                    e.printStackTrace();
                }*/
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
                //final String chatpos = databaseReference.child("message").child(chatname).child(chatlist.get(position)).toString();
                PopupMenu popup = new PopupMenu(ChatActivity.this, view);
                getMenuInflater().inflate(R.menu.chat_popup, popup.getMenu());
                //Toast.makeText(ChatActivity.this,"삭제 되었습니다.", Toast.LENGTH_SHORT).show();
                //Toast.makeText(ChatActivity.this, String.valueOf(position), Toast.LENGTH_SHORT).show();
                //Toast.makeText(ChatActivity.this,databaseReference.child("message").child(chatname).child(chatlist.get(position)).toString(), Toast.LENGTH_SHORT).show();
                //final String chatkey = chatlist.get(position);
                //final FirebaseDatabase chatmsg = databaseReference.child("message").child("chatName").child(chatkey).child("msg").getValue();
                //databaseReference.child("message").child(chatname).child(chatkey).child("msg")
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.encryptmsg:
                                if(useremail.equals(chatdto.get(position).getUserEmail())) {
                                    if(chatdto.get(position).getEncrypt() == false) {
                                        final EditText key = new EditText(ChatActivity.this);
                                        final AlertDialog.Builder enkey = new AlertDialog.Builder(ChatActivity.this)
                                                .setTitle("암호화")
                                                .setView(key)
                                                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        try {
                                                            String enmsg = Encrypt(chatdto.get(position).getMsg(), key.getText().toString());
                                                            Map<String, Object> aesupdate = new HashMap<>();
                                                            aesupdate.put("aesKey", key.getText().toString());
                                                            aesupdate.put("encrypt", true);
                                                            aesupdate.put("msg", enmsg);

                                                            databaseReference.child("message").child(chatname).child("chat").child(chatlist.get(position)).updateChildren(aesupdate);
                                                            updatechat();
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                });
                                        enkey.show();
                                    }else {
                                        Toast.makeText(ChatActivity.this,"이미 암호화된 메시지 입니다.", Toast.LENGTH_SHORT).show();
                                    }
                                }else {
                                    Toast.makeText(ChatActivity.this,"다른 사람의 메시지입니다." ,Toast.LENGTH_LONG).show();
                                }


/*                                if(chatdto.get(position).getEncrypt() == false) {
                                    final EditText key = new EditText(ChatActivity.this);
                                    final AlertDialog.Builder enkey = new AlertDialog.Builder(ChatActivity.this)
                                            .setTitle("암호화")
                                            .setView(key)
                                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    try {
                                                        String enmsg = Encrypt(chatdto.get(position).getMsg(), key.getText().toString());
                                                        Map<String, Object> aesupdate = new HashMap<>();
                                                        aesupdate.put("aesKey", key.getText().toString());
                                                        aesupdate.put("encypt", true);
                                                        aesupdate.put("msg", enmsg);

                                                        databaseReference.child("message").child(chatname).child(chatlist.get(position)).updateChildren(aesupdate);
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                    //chatdto.get(position).setAesKey(key.getText().toString());
                                                    //Log.i("주소4", chatdto.get(position).getAesKey())
                                                    //firebaseDatabase.getReference().child("message").child(chatname).child(chatlist.get(postion)).setValue(null);
                                                    //databaseReference.child("message").child(chatname).child(chatlist.get(position)).child()
                                                    //Log.i("주소3", databaseReference.child("message").child(chatname).child(chatdto.get(position)).toString());
                                                }
                                            });
                                    enkey.show();
                                }else {
                                    Toast.makeText(ChatActivity.this,"이미 암호화된 메시지 입니다.", Toast.LENGTH_SHORT).show();
                                }*/

                                //String str = databaseReference.child("message").child(chatname).child(chatlist.get(position)).
                                //Toast.makeText(ChatActivity.this,"암호화 되었습니다.", Toast.LENGTH_SHORT).show();
                                //Toast.makeText(ChatActivity.this,databaseReference.child("message").child(chatname).child(chatlist.get(position)).toString(), Toast.LENGTH_SHORT).show();
                                //Log.i("주소1", databaseReference.child("message").child(chatname).child(chatlist.get(position)).toString());
                                //Log.i("주소2", databaseReference.child("message").child(chatlist.get(position)).toString());
                                //Log.i("주소", databaseReference.child("message").child(chatname).child(chatlist.get(position)).toString());
                                //Log.i("주소", databaseReference.child("message").child(chatname).child(chatdto.get(position)).toString());
                                //Toast.makeText(ChatActivity.this,databaseReference.child("message").child(chatname).child(chatlist.get(position)).toString(), Toast.LENGTH_SHORT).show();
                                //Toast.makeText(ChatActivity.this,databaseReference.child("message").child(chatname).child(chatlist.get(position)).child("msg").toString(), Toast.LENGTH_SHORT).show();
                                //Toast.makeText(ChatActivity.this,databaseReference.child("message").child(chatname).child(chatdto.get(position)).toString(), Toast.LENGTH_SHORT).show();
                                //Toast.makeText(ChatActivity.this,databaseReference.child("message").child(chatname).child(String.valueOf(chatdto.get(position))).toString(), Toast.LENGTH_SHORT).show();
                                //Toast.makeText(ChatActivity.this, chatkey, Toast.LENGTH_SHORT).show();
                                break;

                            case R.id.decryptmsg:
                                if (chatdto.get(position).getEncrypt() == true) {
                                    final EditText key = new EditText(ChatActivity.this);
                                    final AlertDialog.Builder dekey = new AlertDialog.Builder(ChatActivity.this)
                                            .setTitle("복호화")
                                            .setView(key)
                                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    try {
                                                        if (key.getText().toString().equals(chatdto.get(position).getAesKey())) {
                                                            String demsg = Decrypt(chatdto.get(position).getMsg(), key.getText().toString());
                                                            Log.i("주소2", demsg);
                                                            AlertDialog.Builder dem = new AlertDialog.Builder(ChatActivity.this)
                                                                .setTitle("복호화 메시지")
                                                                .setMessage(demsg)
                                                                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialog, int which) {

                                                                    }
                                                                });
                                                            dem.show();
                                                        }else {
                                                            Toast.makeText(ChatActivity.this,"KEY 값이 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                                                        }
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                Log.i("주소1", key.getText().toString());
                                                Log.i("주소1", chatdto.get(position).getAesKey());
                                                //Toast.makeText(ChatActivity.this, key.getText().toString(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                    dekey.show();
                                }else {
                                    Toast.makeText(ChatActivity.this,"암호화 메시지가 아닙니다.", Toast.LENGTH_SHORT).show();
                                }

/*                                if (chatdto.get(position).getEncrypt() == true) {
                                    final EditText key = new EditText(ChatActivity.this);
                                    final AlertDialog.Builder dekey = new AlertDialog.Builder(ChatActivity.this);
                                    dekey.setTitle("복호화");
                                    dekey.setView(key);
                                    //demad.setMessage(demsg);
                                    dekey.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Log.i("주소1", key.getText().toString());
                                            //Toast.makeText(ChatActivity.this, key.getText().toString(), Toast.LENGTH_SHORT).show();
                                            try {
                                                String demsg = Decrypt(chatdto.get(position).getMsg().toString(), key.getText().toString());
                                                Log.i("주소2", demsg);
                                                AlertDialog.Builder dem = new AlertDialog.Builder(ChatActivity.this);
                                                dem.setTitle("복호화 메시지");
                                                dem.setMessage(demsg);
                                                dem.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {

                                                    }
                                                });
                                                dem.show();
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                                    dekey.show();
                                }else {
                                    Toast.makeText(ChatActivity.this,"암호화 메시지가 아닙니다.", Toast.LENGTH_SHORT).show();
                                }*/

                                //Toast.makeText(ChatActivity.this,"복호화 되었습니다.", Toast.LENGTH_SHORT).show();
                                //Toast.makeText(ChatActivity.this,position, Toast.LENGTH_SHORT).show();
                                //chatlist.remove(position);
                                break;

                            case R.id.copy:
                                //Toast.makeText(ChatActivity.this, databaseReference.child("message").child("chatName").child(chatlist.get(position)).toString(), Toast.LENGTH_SHORT).show();
                                //Toast.makeText(ChatActivity.this, databaseReference.child("message").child("chatName").child(String.valueOf(chatdto.get(position))).toString(), Toast.LENGTH_SHORT).show();
                                ////Toast.makeText(ChatActivity.this, databaseReference.child("message").child("chatName").child(chatdto.get(position)).toString(), Toast.LENGTH_SHORT).show();
                                //Toast.makeText(ChatActivity.this, databaseReference.child("message").child("chatName").child(chatdto.get(position).toString(), Toast.LENGTH_SHORT).show();
                                //Toast.makeText(ChatActivity.this,"복사 되었습니다.", Toast.LENGTH_SHORT).show();
                                //Toast.makeText(, Toast.LENGTH_SHORT).show();
                                break;

                            case R.id.delete:
                                Log.i("주소1", useremail);
                                Log.i("주소2", chatdto.get(position).getUserEmail());
                                if(useremail.equals(chatdto.get(position).getUserEmail())){
                                    deletechat(position);
                                    updatechat();
                                    Toast.makeText(ChatActivity.this,"삭제완료" ,Toast.LENGTH_LONG).show();
                                    Log.i("주소3", "삭제완료");
                                } else{
                                    Toast.makeText(ChatActivity.this,"다른 사람의 메시지입니다." ,Toast.LENGTH_LONG).show();
                                    Log.i("주소3", "다른 사람의 메시지입니다.");
                                }
                                
/*                                firebaseDatabase.getReference().child("messge").child("chatName").child(String.valueOf(chatdto.get(position))).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(ChatActivity.this, "삭제 성공", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull @org.jetbrains.annotations.NotNull Exception e) {
                                        Toast.makeText(ChatActivity.this, "삭제 실패", Toast.LENGTH_SHORT).show();
                                    }
                                });*/
/*                                firebaseDatabase.getReference().child("messge").child("chatName").child(chatlist.get(position)).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(ChatActivity.this, "삭제 성공", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull @org.jetbrains.annotations.NotNull Exception e) {
                                        Toast.makeText(ChatActivity.this, "삭제 실패", Toast.LENGTH_SHORT).show();
                                    }
                                });*/

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
    public void onBackPressed(){
        Intent intent = new Intent(this, ChatMenu.class);
        startActivity(intent);
    }

    private void deletechat(int postion){
        databaseReference.child("message").child(chatname).child("chat").child(chatlist.get(postion)).removeValue();
        //firebaseDatabase.getReference().child("message").child(chatname).child(chatlist.get(postion)).setValue(null);
        //firebaseDatabase.getReference().child("message").child(chatname).child(chatlist.get(postion)).toString();
        Log.i("주소1", firebaseDatabase.getReference().child("message").child(chatname).child("chat").child(chatlist.get(postion)).toString());

        /*Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
        intent.putExtra("roomName",chatname);
        startActivity(intent);*/

/*        Intent intent = new Intent(this, ChatActivity.class);
        startActivity(intent);*/
        //firebaseDatabase.getReference().child("message").child(chatname).child(chatlist.get(postion)).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
/*        firebaseDatabase.getReference().child("message").child(chatname).child(String.valueOf(chatdto.get(postion))).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(ChatActivity.this, "삭제", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ChatActivity.this, "실패", Toast.LENGTH_SHORT).show();
            }
        });*/
    }

    private void updatechat(){
        Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
        intent.putExtra("roomName",chatname);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    //AES128 암호화
    public static String Encrypt(String text, String key) throws Exception {
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
    //AES128 복호화
    public static String Decrypt(String text, String key) throws Exception {
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

    private void addMessage(DataSnapshot dataSnapshot) {
        ChatDTO chatDTO = dataSnapshot.getValue(ChatDTO.class);

        //adapter.add(chatDTO.getUsername() + " : " + chatDTO.getMsg());\
/*        adapter.add(chatDTO.getUsername());
        adapter.add(chatDTO.getMsg());*/

/*        Picasso.get().load(chatDTO.getProfileUrl().toString()).into(youpro);
        youname.setText(chatDTO.getUsername());
        youmsg.setText(chatDTO.getMsg());*/

        //youname.setText(chatDTO.getUsername());
        //adapter.add(youname.toString());

/*        if(chatDTO.getUsername().equals(username)){

        }
        else{

        }*/

/*        if(chatDTO.getUsername().equals(username)){
            adapter.add(chatDTO.getMsg()  + " : " + chatDTO.getUsername());
        }
        else{
            adapter.add(chatDTO.getUsername() + " : " + chatDTO.getMsg());
        }*/
    }
/*    private void removeMessage(DataSnapshot dataSnapshot, ArrayAdapter<String> adapter) {
        ChatDTO chatDTO = dataSnapshot.getValue(ChatDTO.class);
        adapter.remove(chatDTO.getUsername() + " : " + chatDTO.getMsg());
    }*/

    private void openChat(String roomName) {
        //final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1); //R.layout.chat_you //android.R.layout.simple_list_item_1, android.R.id.text1
        //final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.chat_you, youpro, youname, youmsg);
        //adapter = new ChatAdapter()
        //adapter = new ChatAdapter(chatdto, getLayoutInflater());
        //listmsg.setAdapter(adapter);
        databaseReference.child("message").child(roomName).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
/*                String chatKey = dataSnapshot.getKey();
                chatlist.add(chatKey);
                ChatDTO chatDTO = dataSnapshot.getValue(ChatDTO.class);
                chatdto.add(chatDTO);
                adapter.notifyDataSetChanged();
                Log.d("CHATDATA" ,dataSnapshot.getValue().toString());*/

                String chatKey = dataSnapshot.getKey();
                chatlist.add(chatKey);
                ChatDTO chatDTO = dataSnapshot.getValue(ChatDTO.class);
                chatdto.add(chatDTO);
                //adapter.addItem(chatDTO);
                adapter.notifyDataSetChanged();
                listmsg.setSelection(adapter.getCount() - 1);


                //Log.i("주소0", chatlist.toString());
                //listmsg.setSelection(chatdto.size()-1);

/*                ChatDTO chatDTO = dataSnapshot.getValue(ChatDTO.class);
                chatdto.add(chatDTO);
                adapter.notifyDataSetChanged();
                listmsg.setSelection(chatdto.size()-1);*/

/*                String chatKey = dataSnapshot.getKey();
                chatlist.add(chatKey);*/
                //addMessage(dataSnapshot, adapter);
                //Log.e("LOG", "s:" + s);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
/*                String chatKey = dataSnapshot.getKey();
                //chatlist.clear();
                ChatDTO chatDTO = dataSnapshot.getValue(ChatDTO.class);*/
                //chatdto.clear();
                //adapter.notifyDataSetInvalidated();
/*                ChatDTO chatDTO = dataSnapshot.getValue(ChatDTO.class);
                chatdto.clear();
                chatdto.addAll((Collection<? extends ChatDTO>) chatDTO);

                adapter.notifyDataSetChanged();*/
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                /*String chatKey = dataSnapshot.getKey();
                chatlist.remove(chatKey);
                ChatDTO chatDTO = dataSnapshot.getValue(ChatDTO.class);
                chatdto.remove(chatDTO);
                //adapter.notifyDataSetInvalidated();

/*                ChatDTO chatDTO = dataSnapshot.getValue(ChatDTO.class);
                adapter.removeItem(chatDTO);
                adapter.notifyDataSetChanged();*/

                ChatDTO chatDTO = dataSnapshot.getValue(ChatDTO.class);
                chatdto.remove(chatDTO);
                adapter.notifyDataSetChanged();
                Log.i("주소5" ,"바로 삭제가 되었는가?");
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) { }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });
    }

}
