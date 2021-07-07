package com.comityj.securetalktalk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.SignInButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    //EditText t1;
/*    TextInputLayout t2;
    EditText t3;
    TextView tv;
    Button gologinbtn;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Handler hand = new Handler();

        hand.postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        }, 3000);

        //t1 = findViewById(R.id.test1);
/*        t2 = findViewById(R.id.test2_ti);
        t3 = findViewById(R.id.test2_tiet);*/

/*        gologinbtn = (Button) findViewById(R.id.gologinbtn);

        gologinbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });*/

    }


/*    public void Test(View view){

        //텍스트 얻어오기
        //String data1 = t1.getText().toString();
        Editable eat3 = t3.getText();
        String data2 = eat3.toString();

        //Firebase 데이터 베이스 저장
        //Firebase 실시간 DB 관리 객체 얻어오기
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        //저장시킬 노드 참조객체 가져오기
        DatabaseReference rootRef = firebaseDatabase.getReference();
        //별도 키[Key:식별자]없이 값만 저장장
        //rootRef.setValue(data1);
        rootRef.setValue(data2);

*//*
        rootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String data2 = (String) snapshot.getValue();
                //String data2 = snapshot.getValue(String.class);
                tv.setText(data2);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
*//*


    }*/

}
