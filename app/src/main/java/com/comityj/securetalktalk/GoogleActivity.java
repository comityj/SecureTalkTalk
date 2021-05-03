package com.comityj.securetalktalk;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import de.hdodenhof.circleimageview.CircleImageView;

public class GoogleActivity extends AppCompatActivity {

    private FirebaseAuth mAuth ;
    Button btnlogout, btnrevoke;
    TextView tvname, tvemail;
    CircleImageView imgprofile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.google);

        mAuth = FirebaseAuth.getInstance();

        btnlogout = (Button) findViewById(R.id.logout);
        btnrevoke = (Button) findViewById(R.id.revoke);
        tvname = (TextView) findViewById(R.id.profile_name);
        tvemail = (TextView) findViewById(R.id.profile_email);
        imgprofile = (CircleImageView) findViewById(R.id.profile_img);

        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });

        btnrevoke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                revokeAccess();
            }
        });



        //Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

        //tvname = task.getResult().getUser().getDisplayName();

    }

    private void signOut() {
        FirebaseAuth.getInstance().signOut();
    }

    private void revokeAccess() {
        mAuth.getCurrentUser().delete();
    }

}