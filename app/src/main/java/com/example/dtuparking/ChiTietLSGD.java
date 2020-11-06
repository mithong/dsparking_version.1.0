package com.example.dtuparking;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ChiTietLSGD extends AppCompatActivity {

    private DatabaseReference mDatabase;
    TextView txtTb,txtTien,txtmagd,txtngaygd,txttgian,txtkieugd,txtmanguoichuyentien,txtbiensoxe,txtrenamemdngchuyen,txtrenamebiensoxe,txtngguixe,txtngaynhanxe;
    ImageView imgback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_l_s_g_d);

        // nhận dữ liệu từ history
        Intent intent = getIntent();
        final String MaGD = intent.getStringExtra("MaGD");
        final String id = intent.getStringExtra("id");
        anhxa();

        mDatabase.child("History/parkingMan").child("moneyIn").child(id).child(MaGD).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    String tien = snapshot.child("payMoney").getValue().toString();
                    String IdSender = snapshot.child("idSender").getValue().toString();
                    String datePay = snapshot.child("dateSend").getValue().toString();
                    String Method = snapshot.child("method").getValue().toString();
                    String idPay = snapshot.child("idPay").getValue().toString();

                    Integer tien2 = Integer.parseInt(tien);

                    SimpleDateFormat sdfgoc = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    SimpleDateFormat sdfngay = new SimpleDateFormat("yyyy-MM-dd");
                    SimpleDateFormat sdfgio = new SimpleDateFormat("HH:mm:ss");
                    Date date1 = null;

                    try {
                        date1 = sdfgoc.parse(datePay);
                    }catch (Exception e) {
                        e.printStackTrace();
                    }


                    txtTb.setText("NẠP TIỀN VÀO VÍ");
                    txtTien.setTextColor(Color.parseColor("#4CAF50"));
                    txtTien.setText("+ "+(tien2/1000)+".000 VNĐ");
                    txtmagd.setText(idPay);
                    txtngaygd.setText(sdfngay.format(date1));
                    txttgian.setText(sdfgio.format(date1));
                    if(Method.equals("0")){
                        txtkieugd.setText("Nhận tiền từ bảo vệ");
                    }
                    txtmanguoichuyentien.setText(IdSender);
                    txtrenamebiensoxe.setVisibility(View.INVISIBLE);

                }catch (Exception e){
                    mDatabase.child("History/parkingMan").child("moneyOut").child(id).child(MaGD).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            try {
                                String tien = snapshot.child("payMoney").getValue().toString();
                                String maGD = snapshot.getKey().toString();
                                String ngaygui = snapshot.child("dateGet").getValue().toString();
                                String ngaynhan = snapshot.child("dateSend").getValue().toString();
                                String method = snapshot.child("method").getValue().toString();
                                String coso = snapshot.child("place").getValue().toString();
                                String bienso = snapshot.child("plateLicense").getValue().toString();
                                String idPay = snapshot.child("idPay").getValue().toString();

                                Integer tien2 = Integer.parseInt(tien);

                                SimpleDateFormat sdfgoc = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                Date date1 = null;
                                Date date2 = null;
                                try {
                                    date1 = sdfgoc.parse(ngaygui);
                                    date2 = sdfgoc.parse(ngaynhan);
                                }catch (Exception e) {
                                    e.printStackTrace();
                                }

                                txtTb.setText("THANH TOÁN TIỀN GỬI XE");
                                txtTien.setTextColor(Color.parseColor("#F44336"));
                                txtTien.setText("- "+(tien2/1000)+".000 VNĐ");
                                txtmagd.setText(idPay);
                                txtngguixe.setText("Ngày giờ gửi xe: ");
                                txtngaynhanxe.setText("Ngày giờ nhận xe: ");
                                txtngaygd.setText(sdfgoc.format(date1));
                                txttgian.setText(sdfgoc.format(date2));
                                if(method.equals("0")){
                                    txtkieugd.setText("Thanh toán tiền tại bảo vệ");
                                }
                                txtrenamemdngchuyen.setText("Tại Cơ Sở: ");
                                if(coso.equals("0")){
                                    txtmanguoichuyentien.setText("Cơ sở Quang Trung");
                                }
                                else  if(coso.equals("1")){
                                    txtmanguoichuyentien.setText("Cơ sở Hòa Khánh");
                                }
                                if(coso.equals("2")){
                                    txtmanguoichuyentien.setText("Cơ sở Nguyễn Văn Linh");
                                }
                                txtbiensoxe.setText(bienso);
                            }catch (Exception e1){
                                mDatabase.child("History/guard/").child(id).child(MaGD).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        try {
                                            String magd = snapshot.child("idPay").getValue().toString();
                                            String price = snapshot.child("payMoney").getValue().toString();
                                            String idStudent = snapshot.child("idStudent").getValue().toString();
                                            String dated = snapshot.child("dateSend").getValue().toString();

                                            Integer tien2 = Integer.parseInt(price);

                                            SimpleDateFormat sdfgoc = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                            SimpleDateFormat sdfngay = new SimpleDateFormat("yyyy-MM-dd");
                                            SimpleDateFormat sdfgio = new SimpleDateFormat("HH:mm:ss");
                                            Date date1 = null;

                                            try {
                                                date1 = sdfgoc.parse(dated);
                                            }catch (Exception e) {
                                                e.printStackTrace();
                                            }

                                            txtTb.setText("CHUYỂN KHOẢN CHO SINH VIÊN");
                                            txtTien.setTextColor(Color.parseColor("#4CAF50"));
                                            txtTien.setText("- "+(tien2/1000)+".000 VNĐ");
                                            txtmagd.setText(magd);
                                            txtngaygd.setText(sdfngay.format(date1));
                                            txttgian.setText(sdfgio.format(date1));
                                            txtkieugd.setText("Chuyền tiền cho sinh viên");
                                            txtrenamemdngchuyen.setText("Mã sinh viên nhận tiền: ");
                                            txtmanguoichuyentien.setText(idStudent);
                                            txtrenamebiensoxe.setText("Mã Bảo Vệ Chuyển Tiền: ");
                                            mDatabase.child("User/information/guard/").child(id).addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    txtbiensoxe.setText(snapshot.child("idGuard").getValue().toString());
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });

                                        }catch (Exception e){

                                        }

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

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void anhxa() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        imgback = findViewById(R.id.back2);
        txtTb = findViewById(R.id.txt_chitietkieunap);
        txtTien = findViewById(R.id.txt_chitiettien);
        txtmagd = findViewById(R.id.txt_chitietmagd);
        txtngaygd = findViewById(R.id.txt_chitietngaygd);
        txttgian = findViewById(R.id.txt_chitiettggd);
        txtkieugd = findViewById(R.id.txt_chitietkieugd);
        txtmanguoichuyentien = findViewById(R.id.txt_chitietmanct);
        txtbiensoxe = findViewById(R.id.txt_biensoxe);
        txtrenamemdngchuyen = findViewById(R.id.txt_change_coso);
        txtrenamebiensoxe = findViewById(R.id.txt_changebienso);
        txtngguixe = findViewById(R.id.txt_ngaygioguixe);
        txtngaynhanxe = findViewById(R.id.txt_ngaygionhanxe);
    }
}