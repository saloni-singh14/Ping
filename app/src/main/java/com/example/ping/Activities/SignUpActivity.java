package com.example.ping.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ping.Activities.PhoneNumberActivity;
import com.example.ping.Activities.SignInActivity;
import com.example.ping.R;
import com.facebook.CallbackManager;
import com.facebook.login.widget.LoginButton;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class SignUpActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "-1000";
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private LoginButton fbLoginButton;
    private CallbackManager mCallbackManager;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user!=null)
        {
            sendUsertoActivity(user);

        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        changeStatusBarColor();
        createGoogleSignInRequest();
        mAuth = FirebaseAuth.getInstance();

    }
    private void createGoogleSignInRequest() {
        /// Configure sign-in to request the user's ID, email address, and basic
        /// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);


    }
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(Color.TRANSPARENT);
            window.setStatusBarColor(getResources().getColor(R.color.register_bk_color));
        }
    }
    public void onLoginClick(View view){
        startActivity(new Intent(this, SignInActivity.class));
        overridePendingTransition(R.anim.slide_in_left,android.R.anim.slide_out_right);
    }

    public void onRegisterClick(View view) {

        EditText userEmail = findViewById(R.id.editTextEmail);
        String email = userEmail.getText().toString();
        /*if(userName.equals("")) {
            Toast.makeText(this, "com.example.ping.Models.User Name cannot be empty", Toast.LENGTH_SHORT).show();
        }else {
            //Intent intent = new Intent(this, BasicVideoCall.class);
            Intent intent = new Intent(getApplicationContext(), PhoneNumberActivity.class);
            //Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("userName", userName);
            startActivity(intent);
        }*/
    }

    public void onGoogleSignClicked(View view) {
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
                                ///If user has already made an account, he will be directed to the ChatActivity.
                                ///Otherwise he will be directed to SetUpProfile Activity
                                sendUsertoActivity(user);
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(SignUpActivity.this,"Google sign-in failed. Try signing in through phone number",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void sendUsertoActivity(FirebaseUser user) {
        ///<summary>
        ///
        /// Is user has already created a profile, then send him to Chat Activity, else send him to
        ///SetupProfile Activity
        ///@param user: gives userID from FirebaseAuth, and if the user has created a profile
        ///then userID from auth will exist in Firebase Database, else, we have to set up profile
        ///and store data
        ///</summary>
        String userID = user.getUid();
        Toast.makeText(getApplicationContext(),"Sign In Success!",Toast.LENGTH_SHORT).show();
        Query query = FirebaseDatabase.getInstance().getReference().child("users").orderByChild("uid").equalTo(userID);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() > 0) {
                    Log.d(TAG, "User already exists");
                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    // 1 or more users exist which have the username property "usernameToCheckIfExists"
                } else {
                    Log.d(TAG, "User does not exist");
                    Intent intent = new Intent(SignUpActivity.this, SetupProfileActivity.class);
                    startActivity(intent);
                    finish();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "getUser:onCancelled", databaseError.toException());

            }
        });
    }
}