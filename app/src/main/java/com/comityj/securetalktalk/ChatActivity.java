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

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

public class ChatActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private String chatname, username;
    private boolean encryptox;
    private List<String> chatlist = new ArrayList<>();

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

        //registerForContextMenu(listmsg);

        //aestxt = editmsg.getText().toString();



/*        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(256);
        SecretKey key = keyGenerator.generateKey();

        byte[] IV = new byte[16];
        SecureRandom random = new SecureRandom();
        random.nextBytes(IV);

        byte[] cipherText = encrypt(editmsg.getText().toString().getBytes(),key, IV);
        //System.out.println("Encrypted Text : "+Base64.getEncoder().encodeToString(cipherText));

        //String decryptedText = decrypt(cipherText,key, IV);*/

        openChat(chatname);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                encryptox = false;
                ChatDTO chatDTO = new ChatDTO(username, editmsg.getText().toString(), encryptox);
                databaseReference.child("message").child(chatname).push().setValue(chatDTO);
                editmsg.setText("");
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

        listmsg.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                PopupMenu popup = new PopupMenu(ChatActivity.this, view);
                getMenuInflater().inflate(R.menu.chat_popup, popup.getMenu());
                //Toast.makeText(ChatActivity.this,"삭제 되었습니다.", Toast.LENGTH_SHORT).show();
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.encryt:
                                Toast.makeText(ChatActivity.this,"암호화 되었습니다.", Toast.LENGTH_SHORT).show();
                                break;

                            case R.id.decryt:
                                Toast.makeText(ChatActivity.this,"복호화 되었습니다.", Toast.LENGTH_SHORT).show();
                                break;

                            case R.id.copy:
                                Toast.makeText(ChatActivity.this,"복사 되었습니다.", Toast.LENGTH_SHORT).show();
                                break;

                            case R.id.delete:
                                Toast.makeText(ChatActivity.this,"삭제 되었습니다.", Toast.LENGTH_SHORT).show();
                                databaseReference.child("message").child(chatname).child(chatlist.get(position)).removeValue();
                                break;

                        }
                        return false;
                    }
                });
                popup.show();
                return true;
            }
        });


/*       listmsg.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position) { //public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id)
                PopupMenu popup = new PopupMenu(ChatActivity.this, view);
                getMenuInflater().inflate(R.menu.chat_popup, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        Toast.makeText(ChatActivity.this, "삭제되었습니다.", Toast.LENGTH_SHORT).show();
                        databaseReference.child("message").child(chatname).child(chatlist.get(position)).removeValue();
                        return true;

*//*                        switch (item.getItemId()) {
                            case R.id.encryt:
                                Toast.makeText(ChatActivity.this,"암호화 되었습니다.", Toast.LENGTH_SHORT).show();
                                return true;;

                            case R.id.decryt:
                                Toast.makeText(ChatActivity.this,"복호화 되었습니다.", Toast.LENGTH_SHORT).show();
                                return true;;

                            case R.id.copy:
                                Toast.makeText(ChatActivity.this,"복사 되었습니다.", Toast.LENGTH_SHORT).show();
                                return true;;

                            case R.id.delete:
                                Toast.makeText(ChatActivity.this,"삭제 되었습니다.", Toast.LENGTH_SHORT).show();
                                return true;

                        }
                        return false;*//*
                    }
                });
                popup.show();
                return false;
            }
        });*/

    }

/*
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, v, menuInfo);
        //List<String> chatlist = new ArrayList<>();
        menu.setHeaderTitle("Menu");
        menu.add(0, 1, 0, "암호화");
        menu.add(0, 2, 0, "복호화");
        menu.add(0, 3, 0, "복사");
        MenuItem remove = menu.add(0, 4, 0, "삭제");

        remove.setOnMenuItemClickListener(onMICL);
    }

    private final MenuItem.OnMenuItemClickListener onMICL = new MenuItem.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()){
                case 1 :    //암호화
                    //if ()
                    Toast.makeText(ChatActivity.this,"암호화 되었습니다.", Toast.LENGTH_SHORT).show();
                    return true;
                case 2 :    //복호화
                    Toast.makeText(ChatActivity.this,"복호화 되었습니다.", Toast.LENGTH_SHORT).show();

                    return true;
                case 3 :    //복사
                    Toast.makeText(ChatActivity.this,"복사 되었습니다.", Toast.LENGTH_SHORT).show();

                    return true;
                case 4 :    //삭제
                    //array.remove(position);
                    //firebaseDatabase.getReference().child("message").child()
                    //deleteMessage(position);
                    //databaseReference.child("message").child(chatname).child(chatlist.get(position)).removeValue();
                    //list.remove(position);
                    //databaseReference.child("message").child(chatname).child(uidList.get(listmsg.getSelectedItemPosition()).);

                    //Toast.makeText(ChatActivity.this,"삭제 되었습니다.", Toast.LENGTH_SHORT).show();
                    Toast.makeText(ChatActivity.this, "", Toast.LENGTH_SHORT).show();
                    listmsg.remove

            }
            return true;
        }
    };*/



/*    @Override
    //public boolean onContextItemSelected(MenuItem item, AdapterView<?> parent, View v, int i, long id){
    public boolean onContextItemSelected(MenuItem item){
        //final int position = i;
        switch (item.getItemId()){
            case 1 :    //암호화
                //if ()
                Toast.makeText(ChatActivity.this,"암호화 되었습니다.", Toast.LENGTH_SHORT).show();
                return true;
            case 2 :    //복호화
                Toast.makeText(ChatActivity.this,"복호화 되었습니다.", Toast.LENGTH_SHORT).show();

                return true;
            case 3 :    //복사
                Toast.makeText(ChatActivity.this,"복사 되었습니다.", Toast.LENGTH_SHORT).show();

                return true;
            case 4 :    //삭제
                //array.remove(position);
                //firebaseDatabase.getReference().child("message").child()
                //deleteMessage(position);
                //databaseReference.child("message").child(chatname).child(chatlist.get(position)).removeValue();
                Toast.makeText(ChatActivity.this,"삭제 되었습니다.", Toast.LENGTH_SHORT).show();

                return true;

            default :
                return super.onContextItemSelected(item);

        }
    }*/


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

/*    public static byte[] encrypt (byte[] plaintext,SecretKey key,byte[] IV ) throws Exception
    {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec keySpec = new SecretKeySpec(key.getEncoded(), "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(IV);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
        byte[] cipherText = cipher.doFinal(editmsg.getText().toString());
        return cipherText;
    }

    public static String decrypt (byte[] cipherText, SecretKey key,byte[] IV) throws Exception
    {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec keySpec = new SecretKeySpec(key.getEncoded(), "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(IV);
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
        byte[] decryptedText = cipher.doFinal(cipherText);
        return new String(decryptedText);
    }*/

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
    public void deleteMessage(int position){
        List<String> chatlist = new ArrayList<>();
        databaseReference.child("message").child(chatname).child(chatlist.get(position)).removeValue();
    }
}
