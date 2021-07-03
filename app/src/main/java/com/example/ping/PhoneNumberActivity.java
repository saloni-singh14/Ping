package com.example.ping;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.ping.databinding.ActivityPhoneNumberBinding;

public class PhoneNumberActivity extends AppCompatActivity {

    ActivityPhoneNumberBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityPhoneNumberBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        /*setContentView(R.layout.activity_phone_number);

        Button continueBtn=(Button)findViewById(R.id.continueBtn);
        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),OTPActivity.class);
                startActivity(intent);
            }
        });*/
        binding.continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),OTPActivity.class);
                intent.putExtra("phoneNumber",binding.phoneBox.getText().toString());
                startActivity(intent);

            }
        });
    }
}