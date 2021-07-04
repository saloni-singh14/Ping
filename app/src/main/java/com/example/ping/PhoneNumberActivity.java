package com.example.ping;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.ping.databinding.ActivityPhoneNumberBinding;

public class PhoneNumberActivity extends AppCompatActivity {

    ActivityPhoneNumberBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityPhoneNumberBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.phoneBox.requestFocus();


        binding.continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNo=(binding.phoneBox.getText().toString());
                if (phoneNo.trim().length()==13)
                {
                    Intent intent=new Intent(getApplicationContext(),OTPActivity.class);
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