package com.example.dtuparking;

import android.app.ActivityOptions;
import android.content.Intent;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class HomeFragment extends Fragment {

    TextView txttien,txttrangthai,txtsoluot,txtlanguixe;
    private DatabaseReference mDatabase;

    @Nullable
    @Override
    public View onCreateView( @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState ) {
        View view = inflater.inflate(R.layout.fragmenthome,container,false);
        txttien = (TextView) view.findViewById(R.id.txtsotien);
        txttrangthai = (TextView) view.findViewById(R.id.txttrangthai);
        txtsoluot = (TextView) view.findViewById(R.id.txtluotguixe);
        txtlanguixe = (TextView) view.findViewById(R.id.txtlanguixe);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        setTien();
        setLuotguixevaLuotgui();
        setTrangthai();


        return view;
    }
    private  void setTien(){
        home hom = (home) getActivity();
        final String id = hom.getData();
        mDatabase.child("User/information/parkingMan/").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange( @NonNull DataSnapshot snapshot ) {
                if(snapshot.child("position").getValue().toString().equals("3")){
                    String tien = snapshot.child("money").getValue().toString();
                    Integer tien2 = Integer.parseInt(tien);
                    txttien.setText(tien2/1000+".000 VNĐ");
                } else if(snapshot.child("position").getValue().toString().equals("2")){
                    txttien.setText("null");
                }
            }

            @Override
            public void onCancelled( @NonNull DatabaseError error ) {

            }
        });
    }
    private void setLuotguixevaLuotgui(){
        home hom = (home) getActivity();
        final String id = hom.getData();
        final int[] i = {0};
        final Date[] dateLast = {null};
        final String[] coso = {""};
        SimpleDateFormat sdfgoc = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            dateLast[0] = sdfgoc.parse("2020-1-1 12:12:12");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        mDatabase.child("History/parkingMan/").child("moneyOut").child(id).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String dateGet = snapshot.child("dateGet").getValue().toString();

                // khai báo hàm ngày
                final Calendar cal = Calendar.getInstance();
                // chuyển string thành date
                SimpleDateFormat sdfgoc = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                SimpleDateFormat sdf = new SimpleDateFormat("ww");
                SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM");
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
                if(date1.compareTo(dateLast[0])>=0){
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
                    dateLast[0] = date1;
                }
                txtsoluot.setText((i[0])+" lần gửi xe ");
                txtlanguixe.setText("Vào ngày "+sdf2.format(dateLast[0])+" tại cơ sở "+coso[0]);

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
    private void setTrangthai(){
        home hom = (home) getActivity();
        final String id = hom.getData();


        try {
            mDatabase.child("APIParking/Data/Location/").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    try {
                        if(snapshot.child("0/idUser/").child(id).getValue().toString() != null){
                            txttrangthai.setText("Bạn Đang Gửi Xe Tại Cơ Sở Quang Trung");
                        }
                    }catch (Exception e){
                        try {
                            if(snapshot.child("1/idUser/").child(id).getValue().toString() != null){
                                txttrangthai.setText("Bạn Đang Gửi Xe Tại Cơ Sở Hòa Khánh");
                            }
                        }catch (Exception e1){
                            try {
                                if(snapshot.child("2/idUser/").child(id).getValue().toString() != null){
                                    txttrangthai.setText("Bạn Đang Gửi Xe Tại Cơ Sở Nguyễn Văn Linh");
                                }
                            }catch (Exception e2){
                                txttrangthai.setText("Bạn Không Gửi Xe Tại Trường");
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
}
