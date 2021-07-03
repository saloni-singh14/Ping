package com.example.ping;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ping.acitivities.SplashActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;


public class SignInActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "-1000";
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    public String name="yolo",email;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user!=null)
        {

            //Intent intent = new Intent(getApplicationContext(),SplashActivity.class);
            Intent intent = new Intent(getApplicationContext(),PhoneNumberActivity.class);
            //Intent intent = new Intent(getApplicationContext(),BasicVideoCall.class);
            intent.putExtra("userName", name);
            startActivity(intent);

        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        createGoogleSignInRequest();
        mAuth = FirebaseAuth.getInstance();
        findViewById(R.id.googleSignInButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

    }

    private void createGoogleSignInRequest() {
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        /*if (account!=null)
        {
            name=account.getDisplayName();
            email=account.getEmail();
            //topBar.setText("Hello "+name);
            Toast.makeText(this,"Your name is "+name,Toast.LENGTH_SHORT).show();
            Toast.makeText(this,"Your email is "+email,Toast.LENGTH_SHORT).show();
        }*/

    }

   

    public void onNewClick(View View){
        startActivity(new Intent(this,SignUpActivity.class));
        overridePendingTransition(R.anim.slide_in_right,R.anim.stay);

    }

    public void onLoginClick(View view) {
        EditText userName;

        userName = findViewById(R.id.editTextUsername);
        String name = userName.getText().toString();
        if(name.equals("")) {
            Toast.makeText(this, "user name cannot be empty", Toast.LENGTH_SHORT).show();
        }else {
            //Intent intent = new Intent(this, BasicVideoCall.class);
            //Intent intent = new Intent(this, MainActivity.class);
            //Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
            Intent intent = new Intent(getApplicationContext(), PhoneNumberActivity.class);
           // Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("userName", name);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right,R.anim.stay);
        }
    }


    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user!=null)
                            {
                               // Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                //Intent intent = new Intent(getApplicationContext(),SplashActivity.class);
                                Intent intent = new Intent(getApplicationContext(),PhoneNumberActivity.class);
                                //Intent intent = new Intent(getApplicationContext(),BasicVideoCall.class);
                                intent.putExtra("userName", "yolo");
                                startActivity(intent);
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(SignInActivity.this,"Google sign-in failed",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }



}