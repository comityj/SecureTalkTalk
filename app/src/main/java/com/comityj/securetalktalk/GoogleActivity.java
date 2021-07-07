package com.comityj.securetalktalk;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Html;
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
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;

public class GoogleActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    Button btnchat;
    TextView tvname, tvemail, tvlogout, tvrevoke;
    CircleImageView imgprofile;
    //ImageView imgprofile;
    private SignupActivity signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.google);

        final Intent intent_L = new Intent(getApplicationContext(), LoginActivity.class);
        //final Intent intent_C = new Intent(getApplicationContext(), ChatMenu.class);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        btnchat = (Button) findViewById(R.id.chatbtn);
        //btnlogout = (Button) findViewById(R.id.logout);
        //btnrevoke = (Button) findViewById(R.id.revoke);
        tvlogout = (TextView) findViewById(R.id.logout);
        tvrevoke = (TextView) findViewById(R.id.revoke);
        tvname = (TextView) findViewById(R.id.profile_name);
        tvemail = (TextView) findViewById(R.id.profile_email);
        imgprofile = (CircleImageView) findViewById(R.id.profile_img);
        //imgprofile = (ImageView) findViewById(R.id.profile_img);
        //String imgprofileUrl = user.getPhotoUrl().toString();

        //tvlogout.setText(Html.fromHtml("<u>"));
        tvlogout.setPaintFlags(tvlogout.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tvrevoke.setPaintFlags(tvrevoke.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);



//        signup = (SignupActivity)getApplicationContext();
//        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//        tvname = task.getResult().getUser().getDisplayName();

        tvemail.setText(user.getEmail());
        tvname.setText(user.getDisplayName());
        //imgprofile.setImageURI(user.getPhotoUrl());
        Picasso.get().load(user.getPhotoUrl().toString()).into(imgprofile);

        btnchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(intent_C);
                Intent intent_c = new Intent(getApplicationContext(), ChatMenu.class);
                startActivity(intent_c);
                overridePendingTransition(0, 0);
            }
        });

        tvlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
                startActivity(intent_L);
            }
        });

        tvrevoke.setOnClickListener(new View.OnClickListener() {
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