package com.example.ping.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.ping.databinding.ActivityPhoneNumberBinding;
import com.google.firebase.auth.FirebaseAuth;

public class PhoneNumberActivity extends AppCompatActivity {

    ActivityPhoneNumberBinding binding;
    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityPhoneNumberBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.phoneBox.requestFocus();
        //getSupportActionBar().hide();

        auth = FirebaseAuth.getInstance();

        if(auth.getCurrentUser() != null) {
            Intent intent = new Intent(PhoneNumberActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        getSupportActionBar().hide();


        binding.continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNo=(binding.phoneBox.getText().toString());
                if (phoneNo.trim().length()==13)
                {
                    Intent intent=new Intent(getApplicationContext(), OTPActivity.class);
                    intent.putExtra("phoneNumber",binding.phoneBox.getText().toString());
                    startActivity(intent);
                }
                else {
                    Toast.makeText(getApplicationContext(),"Enter correct phone number",Toast.LENGTH_SHORT).show();
                }


            }
        });
    }
}