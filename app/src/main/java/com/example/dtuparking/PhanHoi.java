package com.example.dtuparking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PhanHoi extends AppCompatActivity {

    DatabaseReference mDatabase;
    ImageView back;
    Button btnGui;
    EditText phanhoi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phan_hoi);

        back = findViewById(R.id.backph);
        phanhoi = findViewById(R.id.edt_ttbs);
        btnGui = findViewById(R.id.btn_gui);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        Intent intent = getIntent();
        final String id = intent.getStringExtra("idSinhVien");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btnGui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ph = "";
                if(phanhoi.getText().toString().equals("")){
                    Toast.makeText(PhanHoi.this, "Vui Lòng Ghi Vào Các Ý Kiến Thắc Mắc", Toast.LENGTH_SHORT).show();
                }
                else {
                    ph = phanhoi.getText().toString();
                }
            }
        });
    }
}