package com.videocall.datingapp.Settings;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.videocall.datingapp.R;

public class EmailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.email_activity);



    }

    @Override
    protected void onStart() {
        super.onStart();

        Toast.makeText(this, "email", Toast.LENGTH_SHORT).show();


    }
}
