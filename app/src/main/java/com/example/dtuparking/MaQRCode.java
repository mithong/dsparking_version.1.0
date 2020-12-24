package com.example.dtuparking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.zxing.WriterException;

import java.util.Random;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class MaQRCode extends AppCompatActivity {

    private ImageView imgQr;
    private LinearLayout imgback;
    private DatabaseReference mDatabase;
    private Button btnDoiMa;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ma_q_r_code);

        // ánh xạ
        imgQr = (ImageView) findViewById(R.id.imgQrcode);
        imgback =  findViewById(R.id.back2);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        btnDoiMa =(Button) findViewById(R.id.btnChangeCode);
        sharedPreferences = getSharedPreferences("dataQR",MODE_PRIVATE);

        //lấy dữ liệu id từ home
        Intent intent = getIntent();
        final String id = intent.getStringExtra("idSinhVien");



        if(CheckInternet.isConnect(getBaseContext())){
            // truy ấn tới mã QRCODE
            mDatabase.child("User/information/parkingMan/").child(id).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange( @NonNull DataSnapshot snapshot ) {
                    // kiểm tra có phải sinh viên
                    if(snapshot.child("position").getValue().toString().equals("3")){

                        // set dữ liệu string để tạo mã Qr dựa vào thông tin sinh viên
                        String id = snapshot.getKey().toString();
                        String idStudent = snapshot.child("idStudent").getValue().toString();
                        String name = snapshot.child("name").getValue().toString();
                        String secretNum = snapshot.child("secretNum").getValue().toString();

                        String dulieu = id+"|"+idStudent+"|"+name+"|"+secretNum;

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("id",id);
                        editor.putString("idT",idStudent);
                        editor.putString("name",name);
                        editor.putString("secretNum",secretNum);
                        editor.commit();

                        // tạo mã QR từ các dữ liệu trên
                        QRGEncoder qrgEncoder = new QRGEncoder(dulieu,null, QRGContents.Type.TEXT,800);
                        try {
                            Bitmap bitmap = qrgEncoder.encodeAsBitmap();
                            imgQr.setImageBitmap(bitmap);
                        } catch ( WriterException e) {
                            e.printStackTrace();
                        }
                    }

                    // kiểm tra có phải giảng viên
                    else if(snapshot.child("position").getValue().toString().equals("2")){
                        // set dữ liệu string để tạo mã Qr dựa vào thông tin giảng viên
                        String id = snapshot.getKey().toString();
                        String name = snapshot.child("name").getValue().toString();
                        String idLecturers = snapshot.child("idLecturers").getValue().toString();
                        String secretNum = snapshot.child("secretNum").getValue().toString();

                        // tổng hợp các dữ liệu
                        String dulieu = id+"|"+idLecturers+"|"+name+"|"+secretNum;

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("id",id);
                        editor.putString("idT",idLecturers);
                        editor.putString("name",name);
                        editor.putString("secretNum",secretNum);
                        editor.commit();

                        // tạo mã QR từ các dữ liệu trên
                        QRGEncoder qrgEncoder = new QRGEncoder(dulieu,null, QRGContents.Type.TEXT,800);
                        try {
                            Bitmap bitmap = qrgEncoder.encodeAsBitmap();
                            imgQr.setImageBitmap(bitmap);
                        } catch (WriterException e) {
                            e.printStackTrace();
                        }

                    }


                }

                @Override
                public void onCancelled( @NonNull DatabaseError error ) {

                }
            });

            imgback.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick( View view ) {
                    onBackPressed();
                }
            });
        }
        else {
            String id1 = sharedPreferences.getString("id","");
            String idT = sharedPreferences.getString("idT","");
            String name = sharedPreferences.getString("name","");
            String secretNum = sharedPreferences.getString("secretNum","");
            // tổng hợp các dữ liệu
            String dulieu =id1+"|"+idT+"|"+name+"|"+secretNum;
            // tạo mã QR từ các dữ liệu trên
            QRGEncoder qrgEncoder = new QRGEncoder(dulieu,null, QRGContents.Type.TEXT,800);
            try {
                Bitmap bitmap = qrgEncoder.encodeAsBitmap();
                imgQr.setImageBitmap(bitmap);
            } catch (WriterException e) {
                e.printStackTrace();
            }

            imgback.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick( View view ) {
                    Intent intent = new Intent(MaQRCode.this,home.class);
                    startActivity(intent);
                    finish();
                }
            });
        }


        btnDoiMa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View view ) {
                XacNhanDoi();
            }
        });
    }

    private  void XacNhanDoi(){
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(MaQRCode.this);
        alertDialog.setTitle(getResources().getString(R.string.thongbao));
        alertDialog.setIcon(R.mipmap.ic_launcher);
        alertDialog.setMessage(getResources().getString(R.string.bancomuondoi));

        // lấy dữ liệu id từ home
        Intent intent = getIntent();
        final String id = intent.getStringExtra("idSinhVien");

        if(CheckInternet.isConnect(getBaseContext())){
            alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick( DialogInterface dialogInterface, int i ) {
                    mDatabase.child("User/information/parkingMan/").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange( @NonNull DataSnapshot snapshot ) {
                            // kiểm tra có phải sinh viên
                            if(snapshot.child("position").getValue().toString().equals("3")){
                                // set dữ liệu string để tạo mã Qr dựa vào thông tin sinh viên
                                String id = snapshot.getKey().toString();
                                String idStudent = snapshot.child("idStudent").getValue().toString();
                                String name = snapshot.child("name").getValue().toString();

                                // tạo dữ liệu secretNum ngẫu nhiên
                                Random random = new Random();
                                int num = random.nextInt(100000);
                                String secretNu = String.valueOf(num);

                                // thay đổi secretNum vừa tạo với cái dữ liệu cũ
                                mDatabase.child("User/information/parkingMan/").child(id).child("secretNum").setValue(num);

                                // Tổng hợp dữ liệu
                                String dulieu = id+"|"+idStudent+"|"+name+"|"+secretNu;

                                // tạo mã QR từ các dữ liệu trên
                                QRGEncoder qrgEncoder = new QRGEncoder(dulieu,null, QRGContents.Type.TEXT,800);
                                try {
                                    Bitmap bitmap = qrgEncoder.encodeAsBitmap();
                                    imgQr.setImageBitmap(bitmap);
                                } catch (WriterException e) {
                                    e.printStackTrace();
                                }
                            }

                            // kiểm tra có phải giảng viên
                            else if(snapshot.child("position").getValue().toString().equals("2")){

                                // set dữ liệu string để tạo mã Qr dựa vào thông tin sinh viên
                                String id = snapshot.getKey().toString();
                                String name = snapshot.child("name").getValue().toString();
                                String idLecturers = snapshot.child("idLecturers").getValue().toString();

                                Random random = new Random();
                                int num = random.nextInt(100000);
                                String secretNum = String.valueOf(num);

                                // thay đổi secretNum vừa tạo với cái dữ liệu cũ
                                mDatabase.child("User/information/parkingMan/").child(id).child("secretNum").setValue(num);

                                // tổng hợp các dữ liệu
                                String dulieu = id+"|"+idLecturers+"|"+name+"|"+secretNum;

                                // tạo mã QR từ các dữ liệu trên
                                QRGEncoder qrgEncoder = new QRGEncoder(dulieu,null, QRGContents.Type.TEXT,800);
                                try {
                                    Bitmap bitmap = qrgEncoder.encodeAsBitmap();
                                    imgQr.setImageBitmap(bitmap);
                                } catch (WriterException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onCancelled( @NonNull DatabaseError error ) {

                        }
                    });
                }
            });

            alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick( DialogInterface dialogInterface, int i ) {

                }
            });
            alertDialog.show();
        }
        else {
            Toast.makeText(MaQRCode.this, getResources().getString(R.string.ktramang), Toast.LENGTH_SHORT).show();
        }

    }
}