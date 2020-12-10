package com.example.dtuparking;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Calendar;
import java.util.List;


public class ThongTinSV extends AppCompatActivity {

    private DatabaseReference mDatabase;
    TextView txttenngdung,txtngaysinh,txtgioitinh,txtdiachi,txtchangema,txtmasv,txtchangelop,txtlop ;
    Button btndoi;
    ImageView imgback, imgttavatar;
    private static final int REQUEST_CODE_STORAGE_PERMISSION = 1;
    private static final int REQUEST_CODE_SELECT_IMAGE = 2;
    FirebaseStorage storage ;
    StorageReference forder;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_tin_s_v);

        anhxa();

        // nhận dữ liệu từ home
        Intent intent = getIntent();
        id = intent.getStringExtra("idSinhVien");

        // truy vấn đến thông tin trong firebase
        mDatabase.child("User/information/parkingMan/").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try{
                    // kiểm tra có phải sinh viên
                    if(snapshot.child("position").getValue().toString().equals("3")){
                        txttenngdung.setText(snapshot.child("name").getValue().toString());
                        txtngaysinh.setText(snapshot.child("birthday").getValue().toString());
                        // kiểm tra nam or nữ
                        if(snapshot.child("sex").getValue().toString().equals("1")){
                            txtgioitinh.setText(getResources().getString(R.string.nam));
                        }
                        else
                            txtgioitinh.setText(getResources().getString(R.string.nu));

                        txtdiachi.setText(snapshot.child("adress").getValue().toString());
                        txtmasv.setText(snapshot.child("idStudent").getValue().toString());
                        txtlop.setText(snapshot.child("classS").getValue().toString());
                        // gọi hàm log ảnh
                        new ThongTinSV.LoadImage().execute(snapshot.child("avatar").getValue().toString());
                    }
                    // kiểm tra có phải giảng viên
                    else if(snapshot.child("position").getValue().toString().equals("2")){
                        txttenngdung.setText(snapshot.child("name").getValue().toString());
                        txtngaysinh.setText(snapshot.child("birthday").getValue().toString());
                        if(snapshot.child("sex").getValue().toString().equals("1")){
                            txtgioitinh.setText(getResources().getString(R.string.nam));
                        }
                        else
                            txtgioitinh.setText(getResources().getString(R.string.nu));

                        txtdiachi.setText(snapshot.child("adress").getValue().toString());
                        txtchangema.setText(getResources().getString(R.string.magiangvien));
                        txtmasv.setText(snapshot.child("idLecturers").getValue().toString());
                        txtlop.setVisibility(View.INVISIBLE);
                        txtchangelop.setVisibility(View.INVISIBLE);
                        new ThongTinSV.LoadImage().execute(snapshot.child("avatar").getValue().toString());
                    }
                }catch (Exception e){
                    // kiểm tra có phải bảo vệ
                    mDatabase.child("User/information/guard/").child(id).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            txttenngdung.setText(snapshot.child("name").getValue().toString());
                            txtngaysinh.setText(snapshot.child("birthday").getValue().toString());
                            if(snapshot.child("sex").getValue().toString().equals("1")){
                                txtgioitinh.setText(getResources().getString(R.string.nam));
                            }
                            else
                                txtgioitinh.setText(getResources().getString(R.string.nu));

                            txtdiachi.setText(snapshot.child("adress").getValue().toString());
                            txtchangema.setText(getResources().getString(R.string.mabaove));
                            txtmasv.setText(snapshot.child("idGuard").getValue().toString());
                            // ẩn các dòng không phù hợp
                            txtlop.setVisibility(View.INVISIBLE);
                            txtchangelop.setVisibility(View.INVISIBLE);
                            new ThongTinSV.LoadImage().execute(snapshot.child("avatar").getValue().toString());
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // xử lí việc truy cập vào thư viện điện thoại
        btndoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(ThongTinSV.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_CODE_STORAGE_PERMISSION);
                }
                else {
                    selectImage();
                }
            }
        });

        // quay lại trang trước
        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    // viết hàm tạo class để đọc link hình
    private class LoadImage extends AsyncTask<String, Void , Bitmap> {
        Bitmap bitmaphinh = null;
        @Override
        protected Bitmap doInBackground( String... strings ) {
            try{
                URL url = new URL(strings[0]);

                InputStream inputStream = url.openConnection().getInputStream();

                bitmaphinh = BitmapFactory.decodeStream(inputStream);



            }catch (Exception e){
                e.printStackTrace();
            }
            return bitmaphinh;
        }

        @Override
        protected void onPostExecute( Bitmap bitmap ) {
            super.onPostExecute(bitmap);
            imgttavatar.setImageBitmap(bitmap);
        }
    }
    // viết hàm xử lí khi chọn hình
    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if(intent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(intent,REQUEST_CODE_SELECT_IMAGE);
        }
    }

    // kiểm tra ảnh được chọn
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        
        if(requestCode == REQUEST_CODE_STORAGE_PERMISSION && grantResults.length>0){
            selectImage();
        }else {
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
        }
    }

    // thay đổi ảnh được chọn và gửi lên firebase
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SELECT_IMAGE && resultCode == RESULT_OK){
            if(data != null){
                Uri selectedImageUri = data.getData();
                if(selectedImageUri != null){
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        imgttavatar.setImageBitmap(bitmap);

                        final StorageReference imagename = forder.child("image"+selectedImageUri.getLastPathSegment());
                        imagename.putFile(selectedImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Toast.makeText(ThongTinSV.this, "Đổi Avatar Thành Công", Toast.LENGTH_SHORT).show();
                                imagename.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(final Uri uri) {
                                        mDatabase.child("User/information/parkingMan/").child(id).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                try {
                                                    if(snapshot.child("position").getValue().toString().equals("3")){
                                                        mDatabase.child("User/information/parkingMan/").child(id).child("avatar").setValue(String.valueOf(uri));
                                                    }
                                                    else if(snapshot.child("position").getValue().toString().equals("2")){
                                                        mDatabase.child("User/information/parkingMan/").child(id).child("avatar").setValue(String.valueOf(uri));
                                                    }

                                                }
                                                catch (Exception e){
                                                    mDatabase.child("User/information/guard/").child(id).child("avatar").setValue(String.valueOf(uri));
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    }
                                });
                            }
                        });
                        luufirebase();

                    } catch (FileNotFoundException e) {
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void luufirebase() {

    }

    // ánh xạ các đối tượng
    private void anhxa() {
        storage = FirebaseStorage.getInstance();
        forder = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        imgback = findViewById(R.id.back2);
        imgttavatar = findViewById(R.id.img_TTavata);
        btndoi = findViewById(R.id.btn_doiava);
        txttenngdung = findViewById(R.id.txt_tenngdung);
        txtngaysinh = findViewById(R.id.txt_ngaysinh);
        txtgioitinh = findViewById(R.id.txt_gioitinh);
        txtdiachi = findViewById(R.id.txt_diachi);
        txtchangema = findViewById(R.id.txt_Chagemasv);
        txtmasv = findViewById(R.id.txt_masv);
        txtchangelop = findViewById(R.id.txt_class);
        txtlop = findViewById(R.id.txt_lophoc);
    }
}