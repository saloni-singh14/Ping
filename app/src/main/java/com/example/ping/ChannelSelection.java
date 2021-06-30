package com.example.ping;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;

public class ChannelSelection extends AppCompatActivity {
    //TextView topBar = findViewById(R.id.selection_title);
    String name,email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel_selection);
        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
        if (signInAccount!=null)
        {
            name=signInAccount.getDisplayName();
            email=signInAccount.getEmail();
            //topBar.setText("Hello "+name);
            Toast.makeText(this,"Your name is "+name,Toast.LENGTH_SHORT).show();
            Toast.makeText(this,"Your email is "+email,Toast.LENGTH_SHORT).show();
        }
    }

    public void onClickCall(View view) {
        Intent intent = new Intent(getApplicationContext(),BasicVideoCall.class);
        startActivity(intent);
    }

    public void onClickChat(View view) {
    }

    public void onClickFinish(View view) {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getApplicationContext(),SignInActivity.class);
        startActivity(intent);
    }
}