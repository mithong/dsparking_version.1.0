package com.example.dtuparking;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;

public class HomeFragment extends Fragment {

    TextView txttien,txttrangthai,txtsoluot,txtlanguixe;
    private DatabaseReference mDatabase;
    SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView( @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState ) {
        View view = inflater.inflate(R.layout.fragmenthome,container,false);
        // ánh xạ
        txttien = (TextView) view.findViewById(R.id.txtsotien);
        txttrangthai = (TextView) view.findViewById(R.id.txttrangthai);
        txtsoluot = (TextView) view.findViewById(R.id.txtluotguixe);
        txtlanguixe = (TextView) view.findViewById(R.id.txtlanguixe);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        sharedPreferences = getContext().getSharedPreferences("datalogin",MODE_PRIVATE);

        home hom = (home) getActivity();
        final String id = hom.getData();

        setTien();
        setLuotguixevaLuotgui();
        setTrangthai();

        return view;
    }
    // hàm lấy thông tin tiền trong firebase
    private  void setTien(){
        home hom = (home) getActivity();
        final String id = hom.getData();

        if(CheckInternet.isConnect(getContext())){
            mDatabase.child("User/information/parkingMan/").child(id).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange( @NonNull DataSnapshot snapshot ) {
                    // kiểm tra có phải sinh viên
                    if(snapshot.child("position").getValue().toString().equals("3")){
                        String tien = snapshot.child("money").getValue().toString();
                        Integer tien2 = Integer.parseInt(tien);
                        // định dạng kiểu tiền
                        DecimalFormat formatter = new DecimalFormat("###,###,###");
                        txttien.setText(formatter.format(tien2)+" VNĐ");

                        // lưu lại dữ liệu
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("tien",formatter.format(tien2)+" VNĐ");
                        editor.commit();
                    }
                    // kiểm tra có phải giảng viên
                    else if(snapshot.child("position").getValue().toString().equals("2")){
                        txttien.setText("null");

                        // lưu lại dữ liệu
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("tien","null");
                        editor.commit();
                    }
                }

                @Override
                public void onCancelled( @NonNull DatabaseError error ) {

                }
            });
        }
        else {
            txttien.setText(sharedPreferences.getString("tien",""));
        }


    }
    // hàm đếm số lượt gửi xe và lần gửi xe gần nhất
    private void setLuotguixevaLuotgui(){
        home hom = (home) getActivity();
        final String id = hom.getData();
        if(CheckInternet.isConnect(getContext())){
            final int[] i = {0};
            Date dateLast = null;
            final String[] coso = {""};
            // chuyển string thành date
            SimpleDateFormat sdfgoc = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                dateLast = sdfgoc.parse("2020-01-01 00:00:00");
            } catch (ParseException e) {
                e.printStackTrace();
            }
            // kiểm tra  dữ liệu trong firebase
            final Date[] finalDateLast = {dateLast};
            mDatabase.child("History/parkingMan/").child("moneyOut").child(id).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    String dateGet = snapshot.child("dateGet").getValue().toString();

                    // khai báo hàm ngày
                    final Calendar cal = Calendar.getInstance();

                    // chuyển string thành date
                    SimpleDateFormat sdfgoc = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    SimpleDateFormat sdf = new SimpleDateFormat("ww");
                    SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss dd/MM ");
                    Date date1 = null;
                    Date date2 = null;

                    try {
                        date1 = sdfgoc.parse(dateGet);
                        date2 = sdf.parse(""+(cal.get(Calendar.WEEK_OF_YEAR)));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    if(sdf.format(date1).compareTo(sdf.format(date2))==0){
                        i[0] = i[0] + 1;

                    }
                    // kiểm tra cơ sở
                    if(date1.compareTo(finalDateLast[0])>=0){
                        String place = snapshot.child("place").getValue().toString();
                        if(place.equals("0")){
                            coso[0] = "Quang Trung";
                        }
                        else if(place.equals("1")){
                            coso[0] = "Hòa Khánh";
                        }
                        else if(place.equals("2")){
                            coso[0] = "Nguyễn Văn Linh";
                        }
                        finalDateLast[0] = date1;
                    }
                    // gán dữ liệu
                    txtsoluot.setText((i[0])+" "+getResources().getString(R.string.languixe));
                    txtlanguixe.setText(getResources().getString(R.string.vaongay)+" "+sdf2.format(finalDateLast[0])+getResources().getString(R.string.taicosonao)+" "+coso[0]);

                    // lưu lại dữ liệu
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("soluot",(i[0])+" "+getResources().getString(R.string.languixe));
                    editor.putString("languixe",getResources().getString(R.string.vaongay)+" "+sdf2.format(finalDateLast[0])+" "+getResources().getString(R.string.taicosonao)+" "+coso[0]);
                    editor.commit();
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
        else {
            txtsoluot.setText(sharedPreferences.getString("soluot",""));
            txtlanguixe.setText(sharedPreferences.getString("languixe",""));
        }

    }
    // kiểm tra có đang giữ xe tại trường hay không
    private void setTrangthai(){
        home hom = (home) getActivity();
        final String id = hom.getData();

        if(CheckInternet.isConnect(getContext())){
            try {
                mDatabase.child("APIParking/Parking/IdList/").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        try {
                            if(snapshot.child("0").child(id).getValue().toString() != null){
                                txttrangthai.setText(getResources().getString(R.string.danggiuxetaiqt));

                                // lưu lại dữ liệu
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("trangthai",getResources().getString(R.string.danggiuxetaiqt));
                                editor.commit();
                            }
                        }catch (Exception e){
                            try {
                                if(snapshot.child("1").child(id).getValue().toString() != null){
                                    txttrangthai.setText(getResources().getString(R.string.danggiuxetaihk));

                                    // lưu lại dữ liệu
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("trangthai",getResources().getString(R.string.danggiuxetaihk));
                                    editor.commit();
                                }
                            }catch (Exception e1){
                                try {
                                    if(snapshot.child("2").child(id).getValue().toString() != null){
                                        txttrangthai.setText(getResources().getString(R.string.danggiuxetainvl));

                                        // lưu lại dữ liệu
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("trangthai",getResources().getString(R.string.danggiuxetainvl));
                                        editor.commit();
                                    }
                                }catch (Exception e2){
                                    txttrangthai.setText(getResources().getString(R.string.khonggiuxe));

                                    // lưu lại dữ liệu
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("trangthai",getResources().getString(R.string.khonggiuxe));
                                    editor.commit();
                                }
                            }
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }catch (Exception e){

            }
        }
        else {
            txttrangthai.setText(sharedPreferences.getString("trangthai",""));
        }

    }


}
