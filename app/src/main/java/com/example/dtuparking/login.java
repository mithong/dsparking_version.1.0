package com.example.dtuparking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class login extends AppCompatActivity {

    DatabaseReference mDatabase;
    TextInputLayout edtTdn,edtMk;
    TextInputEditText tiedtdn,tiedtmk;
    Button btnDY;
    CheckBox cbxluu;
    SharedPreferences sharedPreferences;
    EditText editText;
    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        anhxa();


        // set chiều dài tên đăng nhập là 10 mật khẩu là 15 kí tự
        edtTdn.setCounterMaxLength(20);
        edtMk.setCounterMaxLength(15);

        // set dữ liệu tên đăng nhập và mật khẩu đã lưu ở lần đăng nhập trước cho các editext
        tiedtdn.setText(sharedPreferences.getString("tendangnhap",""));
        tiedtmk.setText(sharedPreferences.getString("matkhau",""));
        cbxluu.setChecked(sharedPreferences.getBoolean("check",false));

        // tạo sự kiện khi nhấn vào nút đăng nhập
        btnDY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View view ) {
                final String TenDangNhap = edtTdn.getEditText().getText().toString();
                final String MatKhau = edtMk.getEditText().getText().toString();
                edtTdn.setError(null);
                edtMk.setError(null);
                if(TenDangNhap.equals("")){
                    edtTdn.setError("Vui Lòng Nhập Tên Đăng Nhập");
                    // xóa dữ liệu đã lưu
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.remove("tendangnhap");
                    editor.remove("matkhau");
                    editor.remove("check");
                    editor.commit();
                }
                else{
                    if(MatKhau.equals("")){
                        edtMk.setError("Vui Lòng Nhập Mật Khẩu");
                        // xóa dữ liệu đã lưu
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.remove("tendangnhap");
                        editor.remove("matkhau");
                        editor.remove("check");
                        editor.commit();
                    }
                    else{
                        mDatabase.child("User/account/").child(TenDangNhap).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange( @NonNull DataSnapshot snapshot ) {
                                // kiểm tra tên đăng nhập
                                if(snapshot.getValue()==null){
                                    edtTdn.setError("Sai Tên Đăng Nhập ");
                                    // xóa dữ liệu đã lưu
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.remove("tendangnhap");
                                    editor.remove("matkhau");
                                    editor.remove("check");
                                    editor.commit();
                                }
                                else{
                                    // kiểm tra mật khẩu
                                    if(snapshot.child("pwd").getValue().toString().equals(MatKhau)){
                                        Toast.makeText(login.this, "Đăng Nhập Thành Công", Toast.LENGTH_SHORT).show();
                                        // lưu lại tên đăng nhập và mật khẩu của người dùng
                                        if(cbxluu.isChecked()){
                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            editor.putString("tendangnhap",TenDangNhap);
                                            editor.putString("matkhau",MatKhau);
                                            editor.putBoolean("check",true);
                                            editor.commit();
                                        }
                                        // gửi dữ liệu mã id người dùng qua class home bằng intent
                                        String id = snapshot.child("id").getValue().toString();
                                        Intent intent = new Intent(login.this, home.class);
                                        intent.putExtra("idSinhVien",id);
                                        startActivity(intent);

                                    }
                                    else{

                                        edtMk.setError("Sai Mật Khẩu");
                                        // xóa dữ liệu đã lưu
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.remove("tendangnhap");
                                        editor.remove("matkhau");
                                        editor.remove("check");
                                        editor.commit();
                                    }
                                }
                            }

                            @Override
                            public void onCancelled( @NonNull DatabaseError error ) {

                            }
                        });
                    }
                }

            }
        });
    }

    private void anhxa() {
        //ánh xạ các đối tượng
        edtTdn = (TextInputLayout) findViewById(R.id.tendangnhap);
        edtMk = (TextInputLayout) findViewById(R.id.matkhau);
        btnDY = (Button) findViewById(R.id.btndangnhap);
        cbxluu = (CheckBox) findViewById(R.id.checkboxRemember);
        tiedtdn = (TextInputEditText) findViewById(R.id.TIedtDn);
        tiedtmk = (TextInputEditText) findViewById(R.id.TIedtMk);
        sharedPreferences = getSharedPreferences("datalogin",MODE_PRIVATE);
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }
}