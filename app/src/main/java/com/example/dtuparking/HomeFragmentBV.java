package com.example.dtuparking;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class HomeFragmentBV extends Fragment {
    private DatabaseReference mDatabase;
    Button btn10,btn20,btn50,btnnaptien,btnxoa;
    ImageView btnQr;
    EditText txttien,txtmasv;
    View view;
    String id;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState ) {
        view = inflater.inflate(R.layout.fragmenthomebv, container, false);

        anhxa();

        home hom = (home) getActivity();
        id = hom.getData();

        btn10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txttien.setText("10000");
            }
        });
        btn20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txttien.setText("20000");
            }
        });
        btn50.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txttien.setText("50000");
            }
        });
        btnxoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtmasv.setText(null);
                txttien.setText(null);
            }
        });

        btnQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scancode();
            }
        });

        btnnaptien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CheckInternet.isConnect(getContext())){
                    naptienchosv();
                }
                else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.ktramang), Toast.LENGTH_SHORT).show();
                }

            }
        });

        return view;
    }

    // ánh xạ các đối tượng
    private void anhxa() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        txttien = view.findViewById(R.id.txttiennap);
        txtmasv = view.findViewById(R.id.txtnhapmasv);
        btn10 = view.findViewById(R.id.btn10);
        btn20 = view.findViewById(R.id.btn20);
        btn50 = view.findViewById(R.id.btn50);
        btnnaptien = view.findViewById(R.id.btnnaptien);
        btnQr = view.findViewById(R.id.btnQr);
        btnxoa = view.findViewById(R.id.btnclear);
    }

    // thực hiện việc quyét
    private void scancode() {
        IntentIntegrator integrator = new IntentIntegrator(getActivity());
        integrator.setCaptureActivity(captureActivity.class);
        integrator.setOrientationLocked(false);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.setPrompt("Scanning code");
        integrator.initiateScan();

    }
    // hàm xử lí lúc quét mã qr và đọc dữ liệu
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(result != null){
            String s = result.getContents();
            if(s != null){
                String[] arrS = s.split("\\|");
                txtmasv.setText(arrS[1]);
            }
            else {
                Toast.makeText(getActivity(), "No result", Toast.LENGTH_SHORT).show();
            }
        }
        else
            super.onActivityResult(requestCode, resultCode, data);
    }

    // hàm tạo chuỗi ngẫu nhiên
    public String randomAlphaNumeric(int numberOfCharactor) {
        String alpha = "abcdefghijklmnopqrstuvwxyz";
        String alphaUpperCase = alpha.toUpperCase();
        String digits = "0123456789";
        String ALPHA_NUMERIC = alpha + alphaUpperCase+digits;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < numberOfCharactor; i++) {
            Random random = new Random();
            int num = random.nextInt(((ALPHA_NUMERIC.length() - 1)-0)+1);
            char ch = ALPHA_NUMERIC.charAt(num);
            sb.append(ch);
        }
        return sb.toString();
    }

    // hàm nạp tiền cho sinh viên
    public void naptienchosv(){
        final int[] i = {0};
        mDatabase.child("User/information/parkingMan/").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // kiểm tra có trống các editext hay không
                try{
                    if(txttien.getText().toString().equals("")){
                        Toast.makeText(getActivity(), getResources().getString(R.string.vuilongnnhaptien), Toast.LENGTH_SHORT).show();
                    }
                    else if (txtmasv.getText().toString().equals("")){
                        Toast.makeText(getActivity(), getResources().getString(R.string.vuilongnhapma), Toast.LENGTH_SHORT).show();
                    }
                    else {
                        String masv = txtmasv.getText().toString();
                        if(snapshot.child("idStudent").getValue().toString().equals(masv)){
                            final String ma = snapshot.getKey().toString();

                            // lấy thông tin của bảo vệ từ firebase
                            mDatabase.child("User/information/guard/").child(id).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    final String mabv = snapshot.child("idGuard").getValue().toString();
                                    // tăng tiền cho sinh viên
                                    mDatabase.child("User/information/parkingMan/").child(ma).child("money").addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if(i[0] == 0){
                                                final Integer tien = Integer.parseInt(txttien.getText().toString());
                                                String tienfb = snapshot.getValue().toString();
                                                final Integer tien2 = Integer.parseInt(tienfb);
                                                Integer s = tien + tien2;
                                                mDatabase.child("User/information/parkingMan/").child(ma).child("money").setValue(s.toString());


                                                DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                                dateFormatter.setLenient(false);
                                                Date today = new Date();
                                                String tg = dateFormatter.format(today);

                                                String secretNum = String.valueOf(randomAlphaNumeric(10));

                                                // tạo lịch sử giao dịch đưa lên firebase
                                                NapTienBaoVe napTienBaoVe = new NapTienBaoVe(tg.toString(),txtmasv.getText().toString(),tien.toString(),false,secretNum,"0",mabv);

                                                mDatabase.child("History").child("guard").child(id).push().setValue(napTienBaoVe);
                                                mDatabase.child("History/parkingMan/moneyIn").child(ma).push().setValue(napTienBaoVe);
                                                i[0] = 1;
                                                txtmasv.setText(null);
                                                txttien.setText(null);
                                                Toast.makeText(getActivity(), getResources().getString(R.string.naptienthanhcong), Toast.LENGTH_SHORT).show();
                                                return;

                                            }

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                        }
                        else{
                            Toast.makeText(getActivity(), getResources().getString(R.string.saimasv), Toast.LENGTH_SHORT).show();
                            txtmasv.setText(null);
                        }
                    }
                }catch (Exception e){
                    return;
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}
