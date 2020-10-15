package com.example.dtuparking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

    private ImageView imgQr,imgback;
    private DatabaseReference mDatabase;
    private Button btnDoiMa;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ma_q_r_code);

        // ánh xạ
        imgQr = (ImageView) findViewById(R.id.imgQrcode);
        imgback = (ImageView) findViewById(R.id.back2);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        btnDoiMa =(Button) findViewById(R.id.btnChangeCode);

        //lấy dữ liệu id từ home
        Intent intent = getIntent();
        final String id = intent.getStringExtra("idSinhVien");

        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View view ) {
                onBackPressed();
            }
        });

        // truy ấn tới mã QRCODE
        mDatabase.child("User/parkingMan/information/").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange( @NonNull DataSnapshot snapshot ) {
                // kiểm tra có phải sinh viên
                if(snapshot.child("position").getValue().toString().equals("1")){

                    // set dữ liệu string để tạo mã Qr dựa vào thông tin sinh viên
                    String id = snapshot.getKey().toString();

                    String adress = snapshot.child("adress").getValue().toString();
                    String birthday = snapshot.child("birthday").getValue().toString();
                    String classs = snapshot.child("class").getValue().toString();
                    String idStudent = snapshot.child("idStudent").getValue().toString();
                    String name = snapshot.child("name").getValue().toString();
                    String sex = snapshot.child("sex").getValue().toString();
                    String secretNum = snapshot.child("secretNum").getValue().toString();

                    String adres = snapshot.child("adress").getKey().toString();
                    String birthda = snapshot.child("birthday").getKey().toString();
                    String clas = snapshot.child("class").getKey().toString();
                    String idStuden = snapshot.child("idStudent").getKey().toString();
                    String nam = snapshot.child("name").getKey().toString();
                    String se = snapshot.child("sex").getKey().toString();
                    String secretNu = snapshot.child("secretNum").getKey().toString();

                    // tổng hợp các dữ liệu
                    String dulieu ="{\n" + "id : '" + id + "',\n" + adres +": '" + adress + "',\n" + birthda +": '"+ birthday + "',\n"
                            + clas +": '"+ classs + "',\n" + idStuden +": "+ idStudent + ",\n"
                            + nam +": '"+ name + "',\n" + se +": '"+ sex + "',\n"
                            + secretNu +": "+ secretNum +"\n}";

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
                else if(snapshot.child("position").getValue().toString().equals("0")){
                    // set dữ liệu string để tạo mã Qr dựa vào thông tin giảng viên
                    String id = snapshot.getKey().toString();

                    String adress = snapshot.child("adress").getValue().toString();
                    String birthday = snapshot.child("birthday").getValue().toString();
                    String idLecturers = snapshot.child("idLecturers").getValue().toString();
                    String name = snapshot.child("name").getValue().toString();
                    String sex = snapshot.child("sex").getValue().toString();
                    String secretNum = snapshot.child("secretNum").getValue().toString();

                    String adres = snapshot.child("adress").getKey().toString();
                    String birthda = snapshot.child("birthday").getKey().toString();
                    String idLecturer = snapshot.child("idLecturers").getKey().toString();
                    String nam = snapshot.child("name").getKey().toString();
                    String se = snapshot.child("sex").getKey().toString();
                    String secretNu = snapshot.child("secretNum").getKey().toString();

                    // tổng hợp các dữ liệu
                    String dulieu ="{\n" + "id : '" + id + "',\n" + adres +": '" + adress + "',\n"
                            + birthda +": '"+ birthday + "',\n" + idLecturer +": "+ idLecturers + ",\n"
                            + nam +": '"+ name + "',\n" + se +": '"+ sex + "',\n"
                            + secretNu +": "+ secretNum +"\n}";

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

        btnDoiMa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View view ) {
                XacNhanDoi();
            }
        });
    }

    private  void XacNhanDoi(){
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(MaQRCode.this);
        alertDialog.setTitle("Thông Báo!!!");
        alertDialog.setIcon(R.mipmap.ic_launcher);
        alertDialog.setMessage("Bạn có muốn thay đổi mã QR CODE hiện tại không ?");

        // lấy dữ liệu id từ home
        Intent intent = getIntent();
        final String id = intent.getStringExtra("idSinhVien");

        alertDialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick( DialogInterface dialogInterface, int i ) {
                mDatabase.child("User/parkingMan/information/").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange( @NonNull DataSnapshot snapshot ) {
                        // kiểm tra có phải sinh viên
                        if(snapshot.child("position").getValue().toString().equals("1")){
                            // get dữ liệu string để tạo mã Qr dựa vào thông tin sinh viên
                            String id = snapshot.getKey().toString();

                            String adress = snapshot.child("adress").getValue().toString();
                            String birthday = snapshot.child("birthday").getValue().toString();
                            String classs = snapshot.child("class").getValue().toString();
                            String idStudent = snapshot.child("idStudent").getValue().toString();
                            String name = snapshot.child("name").getValue().toString();
                            String sex = snapshot.child("sex").getValue().toString();

                            String adres = snapshot.child("adress").getKey().toString();
                            String birthda = snapshot.child("birthday").getKey().toString();
                            String clas = snapshot.child("class").getKey().toString();
                            String idStuden = snapshot.child("idStudent").getKey().toString();
                            String nam = snapshot.child("name").getKey().toString();
                            String se = snapshot.child("sex").getKey().toString();
                            String secretNu = snapshot.child("secretNum").getKey().toString();

                            // tạo dữ liệu secretNum ngẫu nhiên
                            Random random = new Random();
                            int num = random.nextInt(100000);
                            String secretNum = String.valueOf(num);

                            // thay đổi secretNum vừa tạo với cái dữ liệu cũ
                            mDatabase.child("User/parkingMan/information/").child(id).child("secretNum").setValue(num);


                            // tổng hợp các dữ liệu
                            String dulieu ="{\n" + "id : '" + id + "',\n" + adres +": '" + adress + "',\n" + birthda +": '"+ birthday + "',\n"
                                    + clas +": '"+ classs + "',\n" + idStuden +": '"+ idStudent + "',\n"
                                    + nam +": '"+ name + "',\n" + se +": '"+ sex + "',\n"
                                    + secretNu +": '"+ secretNum +"'\n}";

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
                        else if(snapshot.child("position").getValue().toString().equals("0")){

                            // set dữ liệu string để tạo mã Qr dựa vào thông tin sinh viên
                            String id = snapshot.getKey().toString();

                            String adress = snapshot.child("adress").getValue().toString();
                            String birthday = snapshot.child("birthday").getValue().toString();
                            String idLecturers = snapshot.child("idLecturers").getValue().toString();
                            String name = snapshot.child("name").getValue().toString();
                            String sex = snapshot.child("sex").getValue().toString();

                            String adres = snapshot.child("adress").getKey().toString();
                            String birthda = snapshot.child("birthday").getKey().toString();
                            String idLecturer = snapshot.child("idLecturers").getKey().toString();
                            String nam = snapshot.child("name").getKey().toString();
                            String se = snapshot.child("sex").getKey().toString();
                            String secretNu = snapshot.child("secretNum").getKey().toString();

                            Random random = new Random();
                            int num = random.nextInt(100000);
                            String secretNum = String.valueOf(num);

                            // thay đổi secretNum vừa tạo với cái dữ liệu cũ
                            mDatabase.child("User/parkingMan/information/").child(id).child("secretNum").setValue(num);

                            // tổng hợp các dữ liệu
                            String dulieu ="{\n" + "id : '" + id + "',\n" + adres +": '" + adress + "',\n"
                                    + birthda +": '"+ birthday + "',\n" + idLecturer +": "+ idLecturers + ",\n"
                                    + nam +": '"+ name + "',\n" + se +": '"+ sex + "',\n"
                                    + secretNu +": "+ secretNum +"\n}";

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

        alertDialog.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick( DialogInterface dialogInterface, int i ) {

            }
        });
        alertDialog.show();
    }
}