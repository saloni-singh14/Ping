package com.example.ping;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        changeStatusBarColor();

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
        startActivity(new Intent(this,SignInActivity.class));
        overridePendingTransition(R.anim.slide_in_left,android.R.anim.slide_out_right);
    }

    public void onRegisterClick(View view) {
        EditText Name = findViewById(R.id.editTextName);
        String userName = Name.getText().toString();
        EditText userEmail = findViewById(R.id.editTextEmail);
        String email = userEmail.getText().toString();
        if(userName.equals("")) {
            Toast.makeText(this, "com.example.ping.User Name cannot be empty", Toast.LENGTH_SHORT).show();
        }else {
            //Intent intent = new Intent(this, BasicVideoCall.class);
            Intent intent = new Intent(getApplicationContext(),PhoneNumberActivity.class);
            //Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("userName", userName);
            startActivity(intent);
        }
    }
}