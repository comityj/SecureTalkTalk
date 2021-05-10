package com.comityj.securetalktalk;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import de.hdodenhof.circleimageview.CircleImageView;

public class GoogleActivity extends AppCompatActivity {

    private FirebaseAuth mAuth ;
    Button btnlogout, btnrevoke, btmchat;
    TextView tvname, tvemail;
    //CircleImageView imgprofile;
    ImageView imgprofile;
    private SignupActivity signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.google);

        final Intent intent_L = new Intent(getApplicationContext(), LoginActivity.class);
        final Intent intent_C = new Intent(getApplicationContext(), ChatActivity.class);
        mAuth = FirebaseAuth.getInstance();

        btmchat = (Button) findViewById(R.id.chatbtn);
        btnlogout = (Button) findViewById(R.id.logout);
        btnrevoke = (Button) findViewById(R.id.revoke);
        tvname = (TextView) findViewById(R.id.profile_name);
        tvemail = (TextView) findViewById(R.id.profile_email);
        //imgprofile = (CircleImageView) findViewById(R.id.profile_img);
        imgprofile = (ImageView) findViewById(R.id.profile_img);

        FirebaseUser user = mAuth.getCurrentUser();

//        signup = (SignupActivity)getApplicationContext();
//        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//        tvname = task.getResult().getUser().getDisplayName();

        tvname.setText(user.getDisplayName());
        imgprofile.setImageURI(user.getPhotoUrl());

        btmchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { startActivity(intent_C); }
        });

        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
                startActivity(intent_L);
            }
        });

        btnrevoke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                revokeAccess();
                startActivity(intent_L);
            }
        });
    }

    private void signOut() {
        FirebaseAuth.getInstance().signOut();
    }

    private void revokeAccess() {
        mAuth.getCurrentUser().delete();
    }

}