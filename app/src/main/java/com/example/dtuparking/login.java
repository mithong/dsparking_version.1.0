package com.example.dtuparking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
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

        tiedtdn.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    btnDY.setClickable(true);
                    return true;
                }
                return false;
            }
        });

        Intent inten = this.getIntent();
        boolean ktra= inten.getBooleanExtra("ktra",false);

        String TenDangNhap = edtTdn.getEditText().getText().toString();
        String MatKhau = edtMk.getEditText().getText().toString();

        // tạo sự kiện khi nhấn vào nút đăng nhập
        btnDY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View view ) {
                ktraDangNhap();
            }
        });

        if(TenDangNhap.equals("") || MatKhau.equals("")){

        }
        else {
            if(ktra){
                ktraDangNhap();
            }

        }

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

    public  void ktraDangNhap(){
        if(CheckInternet.isConnect(getBaseContext())){
            final String TenDangNhap = edtTdn.getEditText().getText().toString();
            final String MatKhau = edtMk.getEditText().getText().toString();
            edtTdn.setError(null);
            edtMk.setError(null);
            if(TenDangNhap.equals("")){
                edtTdn.setError(getResources().getString(R.string.nhapusername));
                // xóa dữ liệu đã lưu
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("tendangnhap");
                editor.remove("matkhau");
                editor.remove("check");
                editor.commit();
            }
            else{
                if(MatKhau.equals("")){
                    edtMk.setError(getResources().getString(R.string.nhapmatkhau));
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
                                edtTdn.setError(getResources().getString(R.string.saiusername));
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
                                    Toast.makeText(login.this, getResources().getString(R.string.dangnhapthanhcong), Toast.LENGTH_SHORT).show();
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

                                    edtMk.setError(getResources().getString(R.string.saimatkhau));
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
        else {
            Toast.makeText(login.this, getResources().getString(R.string.ktramang), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        androidx.appcompat.app.AlertDialog.Builder b =new androidx.appcompat.app.AlertDialog.Builder(login.this);
        b.setTitle("Question");
        b.setMessage("Are you sure you want to exit?");
        b.setPositiveButton("Yes", new DialogInterface. OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which){
                finish();
                System.exit(0);
            }
        });
        b.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.cancel();
            }
        });
        b.create().show();
    }
}